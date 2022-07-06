package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ArticleCategoryService;
import cn.mintimate.img2d.api.dao.ArticleCategoryDao;
import cn.mintimate.img2d.api.entity.ArticleCategory;
import cn.mintimate.img2d.api.exception.dataException;
import cn.mintimate.img2d.api.vo.ArticleCategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryDao, ArticleCategory> implements ArticleCategoryService {
    @Resource
    private ArticleCategoryDao articleCategoryDao;
    @Override
    public void saveOrUpdateCategory(ArticleCategoryVO articleCategoryVO) {
        // 判断分类是否重名
        ArticleCategory existArticleCategory=articleCategoryDao.selectOne(new LambdaQueryWrapper<ArticleCategory>()
                .select(ArticleCategory::getId)
                .eq(ArticleCategory::getCateName,articleCategoryVO.getCategoryName()));
        if(Objects.nonNull(existArticleCategory)&&!existArticleCategory.getId().equals(articleCategoryVO.getId())){
            throw new dataException("分类已经存在啦");
        }
        ArticleCategory articleCategory=ArticleCategory.builder()
                .id(articleCategoryVO.getId())
                .cateName(articleCategoryVO.getCategoryName())
                .build();
        this.saveOrUpdate(articleCategory);
    }
}
