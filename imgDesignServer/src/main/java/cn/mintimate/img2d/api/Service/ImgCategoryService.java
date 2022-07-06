package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.CateDTO;
import cn.mintimate.img2d.api.entity.ImgCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ImgCategoryService extends IService<ImgCategory> {
    /**
     * 获取分类ID和分类昵称
     * @return
     */
    List<CateDTO> getAllCateNameAndID();
    // 管理员访问
    List<CateDTO> getAllCateForAdmin();
}
