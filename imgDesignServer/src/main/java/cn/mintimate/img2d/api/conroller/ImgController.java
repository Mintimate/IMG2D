package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ImgCategoryService;
import cn.mintimate.img2d.api.Service.ImgService;
import cn.mintimate.img2d.api.Service.UserBalanceService;
import cn.mintimate.img2d.api.Service.UserService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.AdminSingleImageDTO;
import cn.mintimate.img2d.api.dto.SingleIMG_DTO;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.Img;
import cn.mintimate.img2d.api.entity.ImgCategory;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.until.PHashUntil;
import cn.mintimate.img2d.api.until.TencentCOSUtil;
import cn.mintimate.img2d.api.vo.AdminSingleImageVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import cn.mintimate.img2d.api.dto.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_IMG_USER_COLLECT;
import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_IMG_USER_LIKE;

/**
 * 图片控制器
 *
 * @author Mintimate
 */
@Controller
@RequestMapping("/img")
@ResponseBody
public class ImgController {
    @Resource
    private ImgService imgService;
    @Resource
    private UserService userService;
    @Resource
    private UserBalanceService userBalanceService;
    @Resource
    private ImgCategoryService imgCategoryService;

    /**
     * 上传图片
     *
     * @param IMG_Raw  图片原文件
     * @param IMG_Name 图片友好名称
     * @param CateId   图片所属分类
     * @return 状态信息
     */
    @RequiredPermission
    @RequestMapping("/upload")
    public Result<SingleIMG_DTO> uploadIMG(@RequestParam(value = "IMG_Raw") MultipartFile IMG_Raw,
                                           @RequestParam(value = "IMG_Name") String IMG_Name,
                                           @RequestParam(value = "CateId") String CateId) {
        Img img = Img.builder()
                .imgName(IMG_Name)
                .cateId(CateId)
                .build();
        String PHash = PHashUntil.getFeatureValue(IMG_Raw);
        List<String> list = imgService.getPHashList();
        for (String item : list) {
            double Similarity = PHashUntil.calculateSimilarity(PHash, item);
            if (Similarity > 0.98) {
                return Result.fail("图片已存在!!!相似度：" + Similarity);
            }
        }
        img.setPerceptualHash(PHash);
        SingleIMG_DTO singleIMGDto = imgService.saveIMG(img, IMG_Raw);
        return Result.ok(singleIMGDto);
    }


    /**
     * 分页获取图片
     *
     * @param category
     * @param currentPage
     * @return 瀑布流布局内容
     */
    @GetMapping("/getImgByPage/{category}/{currentPage}")
    public Result<PageResultUntil<WaterFallIMG_DTO>> getAllIMG(@PathVariable(value = "category") String category,
                                                               @PathVariable(value = "currentPage") Integer currentPage) {
        if (category.equals("All")) {
            return Result.ok(imgService.getImgByPage(null, currentPage));
        } else {
            return Result.ok(imgService.getImgByPage(category, currentPage));
        }
    }

    /**
     * 随机获取图片
     *
     * @param limitSize 随机数量
     * @return 瀑布流布局内容
     */
    @RequestMapping("/getRandomList/{limitSize}")
    public Result<List<WaterFallIMG_DTO>> getRandomList(@PathVariable(value = "limitSize") Integer limitSize) {
        return Result.ok(imgService.getRandomList(limitSize));
    }

    /**
     * 获取单张图片详情
     *
     * @param imgID 图片ID
     * @return SingleIMG_DTO
     */
    @GetMapping("/getSingleIMG/{imgId}")
    public Result<SingleIMG_DTO> getSingleIMG(@PathVariable(value = "imgId") long imgID) {
        return Result.ok(imgService.getSingleIMG_ByID(imgID));
    }


    /**
     * 用户手动删除已经上传到图床的图片
     * @param imgID
     * @return
     */
    @RequiredPermission
    @PostMapping("/deleteIMG/{imgId}")
    public Result deleteIMG(@PathVariable(value = "imgId") long imgID){
        if (imgService.deleteIMG(imgID)){
            return Result.ok();
        }
        else {
            return Result.fail("操作失败：权限不足");
        }
    }

    /**
     * 用户点赞图片
     *
     * @param imgId
     * @return 瀑布流布局内容
     */
    @RequiredPermission
    @PostMapping("/clickLike/{imgId}")
    public Result clickLikeById(@PathVariable(value = "imgId") long imgId) {
        imgService.saveLike(imgId);
        return Result.ok();
    }


    /**
     * 获取用户最新上传的8张图片
     *
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/userRecentImages/{userId}")
    public Result<List<WaterFallIMG_DTO>> getRecentImages(
            @PathVariable(value = "userId") String userId) {
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            return Result.ok();
        }
        List<Img> imgList = imgService.list(
                new LambdaQueryWrapper<Img>()
                .eq(Img::getUserId, userId)
                .eq(Img::getIsAudit, 1)
                .eq(Img::getDelFlag, 0)
                .orderByDesc(Img::getCreateTime)
                .last("limit 8"));
        if (Objects.isNull(imgList)) {
            return Result.ok();
        } else {
            List<WaterFallIMG_DTO> list = new ArrayList<>();
            imgList.forEach(item -> {
                        list.add(WaterFallIMG_DTO.builder()
                                .id(String.valueOf(item.getId()))
                                .imgName(item.getImgName())
                                .imgThumbnailPath(item.getImgThumbnailPath())
                                .userId(item.getUserId())
                                .username(user.getUsername())
                                .userAvatarPath(user.getUserAvatarPath())
                                .build());
                    }
            );
            return Result.ok(list);
        }
    }

    /**
     * 用户中心：统计当前用户获取已经点赞/收藏的图片
     *
     * @return 瀑布流布局内容
     */
    @RequiredPermission
    @GetMapping({"/userSpace/{getType}/{page}", "/userSpace/{getType}"})
    public Result<PageResultUntil<WaterFallIMG_DTO>> getLikeList(
            @PathVariable(value = "getType") String getType,
            @PathVariable(value = "page", required = false) Integer page) {
        if (Objects.isNull(page)) {
            page = 1;
        }
        // 点赞图片?返回点赞图片:返回收藏图片
        if (getType.equals("LikeIMG")) {
            return Result.ok(imgService.getUserSpaceList(page, IMG2D_IMG_USER_LIKE));
        } else {
            return Result.ok(imgService.getUserSpaceList(page, IMG2D_IMG_USER_COLLECT));
        }
    }

    /**
     * 用户收藏图片
     *
     * @param imgId
     * @return
     */
    @RequiredPermission
    @PostMapping("/clickCollect/{imgId}")
    public Result clickCollectById(@PathVariable(value = "imgId") long imgId) {
        imgService.saveCollect(imgId);
        return Result.ok();
    }


    @RequiredPermission(admin = true)
    @PostMapping(
            value = "/img/test",
            produces = {"application/json;charset=UTF-8"}
    )
    public ResponseEntity test(@RequestParam(value = "IMG_Raw") MultipartFile IMG_Raw,
                               @RequestParam(value = "IMG_Name") String IMG_Name) {
        return ResponseEntity.ok("返回值为：" + TencentCOSUtil.UploadIMG(IMG_Raw, IMG_Name));

    }

    /**
     * 根据图片ID获取原图
     *
     * @param id
     * @param response
     * @return
     */
    @RequiredPermission
    @PostMapping("/Original/{id}")
    public Result download(@PathVariable(value = "id") String id, HttpServletResponse response) {
        Img img = imgService.getById(id);
        File file = new File(System.getProperty("user.dir") + img.getImgPath());
        if (!file.exists()) {
            return Result.fail("Can't Find The File!!!");
        }
        if (!userBalanceService.enoughBalance(20)) {
            return Result.fail("Don't Have Enough Balance");
        }
        response.setContentLength((int) file.length());
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(img.getImgName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-Disposition", "attachment;filename=" + img.getImgName());
            e.printStackTrace();
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            return ResponseEntity.badRequest().body("IO Error!!!\n" + e);
            return Result.fail("IO ERROR");
        }
        userBalanceService.deductBalance(20,img.getUserId());
        return Result.ok("Download Success!!!");
    }


    /**
     * 管理员获取图片基本信息
     *
     * @return
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getBaseInfo")
    public Result<List<AdminSingleImageDTO>> getBaseInfoForUserCenter() {
        return Result.ok(imgService.getImageInfoListForUserCenter());
    }

    /**
     * 管理员获取图片全部信息
     *
     * @return 图片基本信息
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getAllInfo")
    public Result<List<AdminSingleImageDTO>> getAllInfoForAdmin() {
        return Result.ok(imgService.getImageInfoList());
    }

    /**
     * 管理员审核/反审核图片
     *
     * @param adminSingleImageVOS
     * @param isAudit
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/auditImage/{auditType}")
    public Result auditImage(@RequestBody List<AdminSingleImageVO> adminSingleImageVOS,
                             @PathVariable(value = "auditType") long isAudit) {
        if (isAudit == AuditBanEnum.PASS.getAuditState()) {
            imgService.AuditImageList(AuditBanEnum.PASS, adminSingleImageVOS);
        } else {
            imgService.AuditImageList(AuditBanEnum.NOT_PASS, adminSingleImageVOS);
        }
        return Result.ok();
    }

    /**
     * 更新图片信息
     *
     * @param adminSingleImageVO
     * @param updateWhat
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/updateImage/{updateWhat}")
    public Result updateImage(@RequestBody AdminSingleImageVO adminSingleImageVO,
                              @PathVariable(value = "updateWhat") String updateWhat) {
        if(Objects.isNull(updateWhat)){
            updateWhat="tagNames";
        }
        Img img=imgService.getOne(new LambdaQueryWrapper<Img>()
                .eq(Img::getId,adminSingleImageVO.getId()));
        if (Objects.isNull(img)){
            return Result.fail("对象错误");
        }
        if (updateWhat.equals("cate")&&Objects.nonNull(adminSingleImageVO.getCateId())){
            img.setCateId(adminSingleImageVO.getCateId());
            return Result.ok(imgService.updateById(img));
        }
        else if(updateWhat.equals("imgName")&&Objects.nonNull(adminSingleImageVO.getImgName())){
            img.setImgName(adminSingleImageVO.getImgName());
            return Result.ok(imgService.updateById(img));
        }
        else{
            List<String> tagNameList = new ArrayList<>();
            Collections.addAll(tagNameList, adminSingleImageVO.getTagNames());
            return Result.ok(imgService.saveImgTags(adminSingleImageVO.getId(), tagNameList));
        }
    }
}
