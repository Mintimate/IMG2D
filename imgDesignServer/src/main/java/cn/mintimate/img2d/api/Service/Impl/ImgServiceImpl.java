package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ImgService;
import cn.mintimate.img2d.api.Service.ImgTagService;
import cn.mintimate.img2d.api.Service.ImgTagsService;
import cn.mintimate.img2d.api.dao.ImgDao;
import cn.mintimate.img2d.api.dao.ImgTagsDao;
import cn.mintimate.img2d.api.dto.AdminSingleImageDTO;
import cn.mintimate.img2d.api.dto.ImgTagDTO;
import cn.mintimate.img2d.api.dto.SingleIMG_DTO;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.*;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.until.*;
import cn.mintimate.img2d.api.vo.AdminSingleImageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.mintimate.img2d.api.constant.ImageSavePathConst.*;
import static cn.mintimate.img2d.api.constant.RedisPrefixConst.*;

@Service
public class ImgServiceImpl extends ServiceImpl<ImgDao, Img> implements ImgService {
    @Resource
    private ImgDao imgDao;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ImgTagService imgTagService;
    @Resource
    private ImgTagsDao imgTagsDao;
    @Resource
    private ImgTagsService imgTagsService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 保存图片缩略图
     *
     * @param originalImage      原图存储路径
     * @param imageThumbnailPath 输出缩略图路径
     * @return 是否成功
     */
    private boolean saveThumbnail(String originalImage, String imageThumbnailPath) {
        File thumbnailImageFile = new File(imageThumbnailPath);
        if (!thumbnailImageFile.getParentFile().exists()) {
            thumbnailImageFile.getParentFile().mkdirs();
        }
        try {
            Thumbnails.of(originalImage)
                    .imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
                    .scale(0.5f)
                    .outputQuality(0.25f)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("static/watermark.png")), 0.75f)
                    .outputFormat("jpg")
                    .toFile(imageThumbnailPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveImageHost(String originalImage, String imageImageHostPath) {
        File thumbnailImageFile = new File(imageImageHostPath);
        if (!thumbnailImageFile.getParentFile().exists()) {
            thumbnailImageFile.getParentFile().mkdirs();
        }
        try {
            Thumbnails.of(originalImage)
                    .imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
                    .scale(0.75f)
                    .outputQuality(0.25f)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("static/watermark.png")), 0.75f)
                    .outputFormat("webp")
                    .toFile(imageImageHostPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveOriginal(String originalImage, MultipartFile imageFile) {
        File originalImageFile = new File(originalImage);
        // 判断路径是否存在，如果不存在则创建
        if (!originalImageFile.getParentFile().exists()) {
            originalImageFile.getParentFile().mkdirs();
        }
        try {
            // 保存到服务器中
            imageFile.transferTo(originalImageFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public SingleIMG_DTO saveIMG(Img img, MultipartFile Img_Raw) {
        // 保存上传用户ID
        img.setUserId(String.valueOf(request.getAttribute("userID")));
        // 使用雪花ID避免文件名重复
        img.setId(IdWorker.getId(img));
        String Path_Name = img.getId() + "-" + img.getImgName();
        // 保存原图
        img.setImgPath(ORIGINAL_SQL_PATH + Path_Name);
        if (saveOriginal(ORIGINAL_SAVE_PATH + Path_Name, Img_Raw)) {
            // 设置文件大小
            img.setImgSize(Img_Raw.getSize());
        } else {
            return null;
        }
        // 保存图床图片
        String ImageHost_Path_Name = Path_Name.substring(0, Path_Name.lastIndexOf(".")) + ".webp";
        img.setImgHostPath(HOST_SQL_PATH + ImageHost_Path_Name);
        saveImageHost(ORIGINAL_SAVE_PATH + Path_Name, HOST_SAVE_PATH + ImageHost_Path_Name);
        // 保存缩略图
        String Thumbnail_Path_Name = Path_Name.substring(0, Path_Name.lastIndexOf(".")) + ".jpg";
        img.setImgThumbnailPath(THUMBNAIL_SQL_PATH + Thumbnail_Path_Name);
        if (saveThumbnail(ORIGINAL_SAVE_PATH + Path_Name, THUMBNAIL_SAVE_PATH + Thumbnail_Path_Name)) {
            // 保存标签
            List<String> tagNames = TencentImageUtil.TencentOCR(new File(THUMBNAIL_SAVE_PATH + Thumbnail_Path_Name));
            this.saveImgTags(String.valueOf(img.getId()), (tagNames));
            this.save(img);
            return imgDao.getSingleIMGWithoutAudit(img.getId());
        }
        return null;
    }

    @Override
    public boolean deleteIMG(Long imgId) {
        Img img = this.getOne(new LambdaQueryWrapper<Img>().eq(Img::getId, imgId));
        if (!img.getUserId().equals(String.valueOf(request.getAttribute("userID")))) {
            return false;
        } else {
            img.setDelFlag(1);
            this.updateById(img);
            return true;
        }
    }

    /**
     * 保存图片的标签
     * （Tags->文章和标签对）
     *
     * @param imgID       图片ID
     * @param tagNameList 标签列表
     */
    @Override
    public AdminSingleImageDTO saveImgTags(String imgID, List<String> tagNameList) {
        // 编辑则删除文章所有标签
        if (Objects.nonNull(imgID)) {
            imgTagsDao.delete(new LambdaQueryWrapper<ImgTags>()
                    .eq(ImgTags::getImgId, imgID));
        }
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已经存在的标签◡ ヽ(`Д´)ﾉ ┻━┻
            List<ImgTag> existTagList = imgTagService.list(new LambdaQueryWrapper<ImgTag>()
                    .in(ImgTag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(ImgTag::getTagName)
                    .collect(Collectors.toList());
            List<String> existTagIdList = existTagList.stream()
                    .map(ImgTag::getId)
                    .collect(Collectors.toList());
            // 去除已经存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<ImgTag> tagList = tagNameList.stream().map(item -> ImgTag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 保存未存在的标签
                imgTagService.saveBatch(tagList);
                List<String> tagIdList = tagList.stream()
                        .map(ImgTag::getId)
                        .collect(Collectors.toList());
                // 刚刚保存的标签合并到已经存在的标签数组内
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id和文章进行绑定
            List<ImgTags> articleTagsList = existTagIdList.stream().map(item -> ImgTags.builder()
                            .imgId(imgID)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            imgTagsService.saveBatch(articleTagsList);
        }
        return imgDao.getImageInfo(Long.valueOf(imgID));
    }

    @Override
    public List<WaterFallIMG_DTO> getRandomList(int limitSize) {
        return imgDao.getRandomImgList(limitSize);
    }

    @Override
    public List<String> getPHashList() {
        return imgDao.getHPushList();
    }

    @Override
    public PageResultUntil<WaterFallIMG_DTO> getImgByPage(String category, Integer currentPage) {
        Integer count = 0;
        if (category == null) {
            count = imgDao.selectCount(new LambdaQueryWrapper<Img>()
                    .ne(Img::getDelFlag, 1)
                    .eq(Img::getIsAudit, 1));
        } else {
            count = imgDao.selectCount(new LambdaQueryWrapper<Img>()
                    .ne(Img::getDelFlag, 1)
                    .eq(Img::getIsAudit, 1)
                    .eq(Img::getCateId, category));
        }
        PageUtil.setCurrentPage(new Page<>(currentPage, 9));
        List<WaterFallIMG_DTO> list = imgDao.getImageByPage(category, PageUtil.getLimitCurrent(), PageUtil.getSize());
        return new PageResultUntil(count, list);
    }

    @Override
    public SingleIMG_DTO getSingleIMG_ByID(Long imgId) {
        Integer likeCount = (Integer) redisUtil.hget(IMG2D_IMG_LIKE_COUNT, String.valueOf(imgId));
        Integer collectCount = (Integer) redisUtil.hget(IMG2D_IMG_COLLECT_COUNT, String.valueOf(imgId));
        SingleIMG_DTO singleIMG = imgDao.getSingleIMG(imgId);
        if (Objects.isNull(singleIMG)) {
            return null;
        }
        singleIMG.setLikeCount(likeCount == null ? 0 : likeCount);
        singleIMG.setCollectCount(collectCount == null ? 0 : collectCount);
        return singleIMG;
    }

    @Override
    public List<AdminSingleImageDTO> getImageInfoListForUserCenter() {
        String userId = String.valueOf(request.getAttribute("userID"));
        return imgDao.getImageHistoryForUser(userId);
    }

    @Override
    public List<AdminSingleImageDTO> getImageInfoList() {
        List<AdminSingleImageDTO> adminSingleImageDTOS = imgDao.getImageInfoList();
        for (AdminSingleImageDTO singleImageDTO : adminSingleImageDTOS) {
            singleImageDTO.setLikeCount((Integer) redisUtil.hget(IMG2D_IMG_LIKE_COUNT, singleImageDTO.getId()));
            singleImageDTO.setCollectCount((Integer) redisUtil.hget(IMG2D_IMG_COLLECT_COUNT, singleImageDTO.getId()));
        }
        return adminSingleImageDTOS;
    }

    @Override
    public boolean AuditImageList(AuditBanEnum enumEnum, List<AdminSingleImageVO> adminSingleImageVOList) {
        List<String> idList = adminSingleImageVOList.stream().map(AdminSingleImageVO::getId).collect(Collectors.toList());
        imgDao.updateAudit(enumEnum.getAuditState(), idList);
        return true;
    }

    @Override
    public void saveCollect(long imgId) {
        saveLikeOrCollect(IMG2D_IMG_USER_COLLECT, IMG2D_IMG_COLLECT_COUNT, String.valueOf(imgId));
    }

    @Override
    public void saveLike(long imgId) {
        saveLikeOrCollect(IMG2D_IMG_USER_LIKE, IMG2D_IMG_LIKE_COUNT, String.valueOf(imgId));
    }


    @Override
    public PageResultUntil<WaterFallIMG_DTO> getUserSpaceList(Integer currentPage, String Type) {
        Set<Object> idList = redisUtil.sGet(Type + request.getAttribute("userID"));
        if (idList.size() == 0) {
            return new PageResultUntil(0, null);
        }
        PageUtil.setCurrentPage(new Page<>(currentPage, 8));
        List<WaterFallIMG_DTO> list =
                imgDao.getImageListByIdList(idList, PageUtil.getLimitCurrent(), PageUtil.getSize());
        return new PageResultUntil(idList.size(), list);
    }

    private void saveLikeOrCollect(String USER_DATA, String PUBLIC_DATA, String IMG_ID) {
        // 判断是否点赞
        String commentLikeKey = USER_DATA + request.getAttribute("userID");
        if (redisUtil.sHasKey(commentLikeKey, IMG_ID)) {
            // 点过赞则删除评论id
            redisUtil.setRemove(commentLikeKey, IMG_ID);
            // 评论点赞量-1
            redisUtil.hdecr(PUBLIC_DATA, IMG_ID, 1L);
        } else {
            // 未点赞则增加评论id
            redisUtil.sSet(commentLikeKey, IMG_ID);
            // 评论点赞量+1
            redisUtil.hincr(PUBLIC_DATA, IMG_ID, 1L);
        }
    }
}
