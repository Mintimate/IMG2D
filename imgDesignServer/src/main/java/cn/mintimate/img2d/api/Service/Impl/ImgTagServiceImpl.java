package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ImgTagService;
import cn.mintimate.img2d.api.dao.ImgTagDao;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.ImgTag;
import cn.mintimate.img2d.api.until.PageUtil;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class ImgTagServiceImpl extends ServiceImpl<ImgTagDao, ImgTag> implements ImgTagService {
    @Resource
    private ImgTagDao imgTagDao;

    @Override
    public PageResultUntil<WaterFallIMG_DTO> getImgByPage(Integer currentPage, List<String> tagNames) {
        PageUtil.setCurrentPage(new Page<>(currentPage, 9));
        List list=imgTagDao.getImageByPage(PageUtil.getLimitCurrent(), PageUtil.getSize(),tagNames);
        Integer count = imgTagDao.getImageByPageCount(tagNames);
        return new PageResultUntil(count,list);
    }
}
