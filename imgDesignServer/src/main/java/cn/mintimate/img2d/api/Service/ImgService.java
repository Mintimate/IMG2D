package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.AdminSingleImageDTO;
import cn.mintimate.img2d.api.dto.SingleIMG_DTO;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.Img;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.vo.AdminSingleImageVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImgService extends IService<Img> {
    /**
     * 存储图片
     * @param img 图片对象
     * @param Img_Raw 图片原文件
     * @return
     */
    SingleIMG_DTO saveIMG(Img img, MultipartFile Img_Raw);

    /**
     * 删除图片（逻辑删除）
     * @param imgId
     * @return
     */
    boolean deleteIMG(Long imgId);


    /**
     * 首页获取随机图片
     * @param limitSize 获取几张随机图片
     * @return 瀑布流布局页面
     */
    List<WaterFallIMG_DTO> getRandomList(int limitSize);


    /**
     * 获取图片感知Hash列表
     * @return
     */
    List<String> getPHashList();

    /**
     * 分页获取瀑布流图片数据（分类）
     * @param category 图片分类
     * @param currentPage 页面
     * @return 分页数据
     */
    PageResultUntil<WaterFallIMG_DTO> getImgByPage(String category, Integer currentPage);


    /**
     * 获取单张图片信息
     * @param imgId 图片ID
     * @return 单张图片
     */
    SingleIMG_DTO getSingleIMG_ByID(Long imgId);

    /**
     * 获取图片基本信息=>用户中心
     * @return 图片对象列表
     */
    List<AdminSingleImageDTO> getImageInfoListForUserCenter();

    /**
     * 管理员获取图片基本信息
     * @return 图片对象列表
     */
    List<AdminSingleImageDTO> getImageInfoList();

    /**
     * 审核或反审核图片（列表）
     * @param adminSingleImageVOList 图片列表
     * @return 是否成功
     */
    boolean AuditImageList(AuditBanEnum enumEnum, List<AdminSingleImageVO> adminSingleImageVOList);

    /**
     * 保存或更新文章标签
     * @param imgID 图片ID
     * @param tagNameList 图片标签
     */
    AdminSingleImageDTO saveImgTags(String imgID, List<String> tagNameList);

    /**
     * 收藏图片
     */
    void saveCollect(long imgId);

    /**
     * 点赞图片
     * @param imgId 图片ID
     */
    void saveLike(long imgId);

    /**
     * 获取用户点赞/收藏的图片数据
     * @return 瀑布流布局图片
     */
    PageResultUntil<WaterFallIMG_DTO> getUserSpaceList(Integer currentPage, String Type);
}
