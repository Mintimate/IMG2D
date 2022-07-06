package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.AdminSingleImageDTO;
import cn.mintimate.img2d.api.dto.SingleIMG_DTO;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.Img;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ImgDao extends BaseMapper<Img> {
    List<WaterFallIMG_DTO> getRandomImgList(int limitSize);
    List<String> getHPushList();

    /**
     * 获取单张图片信息
     * @param imgId：图片ID
     * @return SingleIMG
     */
    SingleIMG_DTO getSingleIMG(@Param("imgId") Long imgId);

    /**
     * 获取单张图片信息(无视审核和删除）
     * @param imgId：图片ID
     * @return SingleIMG
     */
    SingleIMG_DTO getSingleIMGWithoutAudit(@Param("imgId") Long imgId);

    /**
     * 获取瀑布流布局图片（分页）
     * @param cateId 分类ID
     * @param current 页面
     * @param size 每页几张图片
     * @return 瀑布流布局
     */
    List<WaterFallIMG_DTO> getImageByPage(@Param("cateId")String cateId,
                                          @Param("current") long current,
                                          @Param("size") Long size);

    /**
     * 获取瀑布流布局图片（根据图片ID）（分页）
     * @param idList 图片List
     * @param current 页面
     * @param size 每页几张图片
     * @return 瀑布流布局
     */
    List<WaterFallIMG_DTO> getImageListByIdList(@Param("idList") Set idList,
                                                @Param("current") long current,
                                                @Param("size") Long size);

    /**
     * 管理员获取图片单信息列表
     * @return 管理员图片信息
     */
    AdminSingleImageDTO getImageInfo(@Param("imgId") Long imgId);


    /**
     * 用户个人中心获取历史图片
     * @param userId
     * @return 用户上传的图片信息列表
     */
    List<AdminSingleImageDTO> getImageHistoryForUser(@Param("userId") String userId);

    /**
     * 管理员获取图片基本信息列表
     * @return 管理员图片信息
     */
    List<AdminSingleImageDTO> getImageInfoList();

    /**
     * 管理员审核图片
     * @param isAudit 模式（审核/反审核）
     * @param idList 图片ID List
     */
    void updateAudit(@Param("isAudit") long isAudit,@Param("idList") List idList);
}
