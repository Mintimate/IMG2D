package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ImgCategoryService;
import cn.mintimate.img2d.api.dao.ImgCategoryDao;
import cn.mintimate.img2d.api.dto.CateDTO;
import cn.mintimate.img2d.api.entity.ImgCategory;
import cn.mintimate.img2d.api.until.BeanCopyUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImgCategoryServiceImpl extends ServiceImpl<ImgCategoryDao, ImgCategory> implements ImgCategoryService {
    @Resource
    private ImgCategoryDao mapper;

    @Override
    public List<CateDTO> getAllCateNameAndID() {
        List<ImgCategory> categoryList = mapper.selectList(null);
        return BeanCopyUtil.copyList(categoryList,CateDTO.class);
    }

    @Override
    public List<CateDTO> getAllCateForAdmin() {
        return mapper.listCateForAdmin();
    }
}
