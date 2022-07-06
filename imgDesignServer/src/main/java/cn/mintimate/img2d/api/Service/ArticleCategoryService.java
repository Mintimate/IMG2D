package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.entity.ArticleCategory;
import cn.mintimate.img2d.api.vo.ArticleCategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {
    /**
     * 添加分类
     */
    void saveOrUpdateCategory(ArticleCategoryVO articleCategoryVO);
}
