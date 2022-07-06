package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.CateDTO;
import cn.mintimate.img2d.api.entity.ImgCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface ImgCategoryDao extends BaseMapper<ImgCategory> {
    /**
     * 管理员查询所有分类
     * @return
     */
    List<CateDTO> listCateForAdmin();
}
