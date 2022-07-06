package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.AdminSingleImageDTO;
import cn.mintimate.img2d.api.dto.ArticleDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeDTO;
import cn.mintimate.img2d.api.entity.Article;
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
public interface ArticleDao extends BaseMapper<Article> {
    /**
     * 查询首页文章
     * @param current 页面
     * @param size 大小
     * @return 文章列表
     */
    List<ArticleHomeDTO> listArticlesHome(@Param("current") Long current, @Param("size") Long size);

    List<ArticleHomeDTO> listArticlesHomeByType(@Param("categoryId") String categoryId,
                                                @Param("current") Long current,
                                                @Param("size") Long size);

    List<ArticleHomeDTO> listArticlesHomeBySearch(@Param("keyWord") String keyWord);



    /**
     * 随机获取5张首页文章
     */
    List<ArticleHomeDTO> randArticlesHome();

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleDTO getArticleById(@Param("articleId") String articleId);

    /**
     * 管理员获取文章信息列表
     * @return 管理员图片信息
     */
    List<ArticleDTO> getPostInfoList();

    /**
     * 管理员审核图片
     * @param isAudit 模式（审核/反审核）
     * @param idList 图片ID List
     */
    void updateAudit(@Param("isAudit") long isAudit,@Param("idList") List idList);
}
