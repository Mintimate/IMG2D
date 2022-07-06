package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ArticleService;
import cn.mintimate.img2d.api.Service.ArticleTagService;
import cn.mintimate.img2d.api.Service.ArticleTagsService;
import cn.mintimate.img2d.api.dao.ArticleCategoryDao;
import cn.mintimate.img2d.api.dao.ArticleDao;
import cn.mintimate.img2d.api.dao.ArticleTagsDao;
import cn.mintimate.img2d.api.dto.ArticleDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeRightDTO;
import cn.mintimate.img2d.api.entity.*;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.exception.dataException;
import cn.mintimate.img2d.api.until.BeanCopyUtil;
import cn.mintimate.img2d.api.until.PageUtil;
import cn.mintimate.img2d.api.until.RedisUtil;
import cn.mintimate.img2d.api.vo.ArticleHomeRightVO;
import cn.mintimate.img2d.api.vo.ArticleVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Desc 文章
 *
 * @author Mintimate
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {
    @Resource
    private ArticleCategoryDao articleCategoryDao;
    @Resource
    private ArticleDao articleDao;
    @Resource
    private ArticleTagsDao articleTagsDao;
    @Resource
    private ArticleService articleService;
    @Resource
    private ArticleTagService articleTagService;
    @Resource
    private ArticleTagsService articleTagsService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 从VO对象里提取Article对象
        ArticleCategory articleCategory = articleCategoryDao.
                selectOne(new LambdaQueryWrapper<ArticleCategory>()
                .eq(ArticleCategory::getCateName, articleVO.getCateName()));
        Article article = BeanCopyUtil.copyObject(articleVO, Article.class);
        if (Objects.isNull(articleCategory)) {
            throw new dataException("分类不存在");
        }
        article.setCategoryId(articleCategory.getId());
        // 保存文章
        articleService.saveOrUpdate(article);
        saveArticleTags(article.getId(),articleVO.getTagNameList());
    }

    @Override
    public PageResultUntil<ArticleHomeDTO> listHomeArticle(Integer page) {
        Integer count = articleDao.selectCount(null);
        PageUtil.setCurrentPage(new Page<>(page, 4));
        List<ArticleHomeDTO> list = articleDao.listArticlesHome(PageUtil.getLimitCurrent(), PageUtil.getSize());
        return new PageResultUntil(count, list);
    }

    @Override
    public PageResultUntil<ArticleHomeDTO> listHomeArticleByType(String type, Integer page) {
        ArticleCategory articleCategory=articleCategoryDao.selectOne(new LambdaQueryWrapper<ArticleCategory>()
                .eq(ArticleCategory::getCateName,type));
        if (Objects.isNull(articleCategory)){
            return new PageResultUntil(0,null);
        }
        Integer count = articleDao.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, articleCategory.getId()));
        PageUtil.setCurrentPage(new Page<>(page, 4));
        List<ArticleHomeDTO> list = articleDao.listArticlesHomeByType(articleCategory.getId(), PageUtil.getLimitCurrent(), PageUtil.getSize());
        return new PageResultUntil(count,list);
    }

    @Override
    public List<ArticleHomeDTO> getHomeArticleByKeyWord(String keyWord) {
        return articleDao.listArticlesHomeBySearch(keyWord);
    }

    @Override
    public List<ArticleHomeRightDTO> recommendArticle()  {
        // 将redis中的数据转换为对象集合
        List<Object> homeRightList = redisUtil.lGet("img2d_article_recommend", 0, -1);
        if (homeRightList.size()<5){
            redisUtil.lRemove("img2d_article_recommend");
            List<ArticleHomeDTO> articleHomeDTOS=articleDao.randArticlesHome();
            List<ArticleHomeRightDTO> articleHomeRightDTOS=BeanCopyUtil.copyList(
                    articleHomeDTOS,ArticleHomeRightDTO.class);
            for (ArticleHomeRightDTO articleHomeRightDTO:articleHomeRightDTOS){
                articleHomeRightDTO.setTopId("1");
                redisUtil.lSet("img2d_article_recommend", JSON.toJSON(articleHomeRightDTO));
            }
            return articleHomeRightDTOS;
        }
        List<ArticleHomeRightDTO> newArticleHomeRightDTO= JSON.parseArray(String.valueOf(homeRightList),ArticleHomeRightDTO.class);
        return newArticleHomeRightDTO;
    }

    @Override
    public List<ArticleHomeRightDTO> randomRecommendArticle()  {
        // 将redis中的数据转换为对象集合
        List<Object> homeRightList = redisUtil.lGet("img2d_article_random_recommend", 0, -1);
        if (homeRightList.size()<5){
            redisUtil.lRemove("img2d_article_random_recommend");
            List<ArticleHomeDTO> articleHomeDTOS=articleDao.randArticlesHome();
            List<ArticleHomeRightDTO> articleHomeRightDTOS=BeanCopyUtil.copyList(
                    articleHomeDTOS,ArticleHomeRightDTO.class);
            for (ArticleHomeRightDTO articleHomeRightDTO:articleHomeRightDTOS){
                articleHomeRightDTO.setTopId("1");
                // 12小时有效期
                redisUtil.lSet("img2d_article_random_recommend", JSON.toJSON(articleHomeRightDTO),60*60*12);
            }
            return articleHomeRightDTOS;
        }
        List<ArticleHomeRightDTO> newArticleHomeRightDTO= JSON.parseArray(String.valueOf(homeRightList),ArticleHomeRightDTO.class);
        return newArticleHomeRightDTO;
    }

    @Override
    public void updateRecommendArticleList(List<ArticleHomeRightVO> articleHomeRightVOList) {
        redisUtil.lRemove("img2d_article_recommend");
        List<ArticleHomeRightDTO> articleHomeRightDTOS=BeanCopyUtil.copyList(
                articleHomeRightVOList,ArticleHomeRightDTO.class);
        for (ArticleHomeRightDTO articleHomeRightDTO:articleHomeRightDTOS){
            redisUtil.lSet("img2d_article_recommend", JSON.toJSON(articleHomeRightDTO));
        }
    }

    @Override
    public ArticleDTO getArticleById(String id) {
        return articleDao.getArticleById(id);
    }

    @Override
    public List<ArticleDTO> getPostInfoList() {
        return articleDao.getPostInfoList();
    }

    @Override
    public boolean AuditArticle(AuditBanEnum enumEnum, List<ArticleVO> articleVOS) {
        List<String> idList = articleVOS.stream().map(ArticleVO::getId).collect(Collectors.toList());
        articleDao.updateAudit(enumEnum.getAuditState(), idList);
        return true;
    }

    /**
     * 保存文章的标签
     * （Tags->文章和标签对）
     *
     * @param tagNameList
     * @param articleId
     */
    @Override
    public void saveArticleTags(String articleId,List<String> tagNameList) {
        // 编辑则删除文章所有标签
        if (Objects.nonNull(articleId)) {
            articleTagsDao.delete(new LambdaQueryWrapper<ArticleTags>()
                    .eq(ArticleTags::getArticleId, articleId));
        }
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已经存在的标签◡ ヽ(`Д´)ﾉ ┻━┻
            List<ArticleTag> existTagList = articleTagService.list(new LambdaQueryWrapper<ArticleTag>()
                    .in(ArticleTag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(ArticleTag::getTagName)
                    .collect(Collectors.toList());
            List<String> existTagIdList = existTagList.stream()
                    .map(ArticleTag::getId)
                    .collect(Collectors.toList());
            // 去除已经存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<ArticleTag> tagList = tagNameList.stream().map(item -> ArticleTag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 保存未存在的标签
                articleTagService.saveBatch(tagList);
                List<String> tagIdList = tagList.stream()
                        .map(ArticleTag::getId)
                        .collect(Collectors.toList());
                // 刚刚保存的标签合并到已经存在的标签数组内
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id和文章进行绑定
            List<ArticleTags> articleTagsList = existTagIdList.stream().map(item -> ArticleTags.builder()
                            .articleId(articleId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            articleTagsService.saveBatch(articleTagsList);
        }
    }
}
