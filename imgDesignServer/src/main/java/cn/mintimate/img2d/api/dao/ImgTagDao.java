package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.ImgTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
@Mapper
public interface ImgTagDao extends BaseMapper<ImgTag> {
    /**
     * 获取瀑布流布局图片（分页）
     *
     * @param current 页数
     * @param size    每页几项
     * @param tagList 查找的标签
     * @return
     */
    List<WaterFallIMG_DTO> getImageByPage(@Param("current") long current,
                                          @Param("size") Long size,
                                          @Param("tagList") List<String> tagList);

    Integer getImageByPageCount(@Param("tagList") List<String> tagList);
}
