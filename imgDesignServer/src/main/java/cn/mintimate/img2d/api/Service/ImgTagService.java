package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.ImgCategory;
import cn.mintimate.img2d.api.entity.ImgTag;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Desc 图片标签
 *
 * @author Mintimate
 */
public interface ImgTagService extends IService<ImgTag> {
    /**
     * 分页获取瀑布流图片数据（标签）
     * @param tagNames 图片标签
     * @return 分页数据
     */
    PageResultUntil<WaterFallIMG_DTO> getImgByPage(Integer currentPage, List<String> tagNames);
}
