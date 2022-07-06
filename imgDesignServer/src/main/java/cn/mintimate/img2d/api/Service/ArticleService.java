package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.ArticleDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeRightDTO;
import cn.mintimate.img2d.api.entity.Article;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.vo.ArticleHomeRightVO;
import cn.mintimate.img2d.api.vo.ArticleVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Desc 文章服务
 *
 * @author Mintimate
 */
public interface ArticleService extends IService<Article> {
    /**
     * 添加或修改文章
     * @param articleVO
     */
    void saveOrUpdateArticle(ArticleVO articleVO);

    /**
     * 首页文章列表
     * @param page 页码
     * @return 分页后的文章列表
     */
    PageResultUntil<ArticleHomeDTO> listHomeArticle(Integer page);

    /**
     * 首页文章列表(分类获取）
     * @param type 文章分类
     * @param page 页码
     * @return 分页后的文章列表
     */
    PageResultUntil<ArticleHomeDTO> listHomeArticleByType(String type, Integer page);

    /**
     * 根据关键字查询文章
     * @param keyWord
     * @return
     */
    List<ArticleHomeDTO> getHomeArticleByKeyWord(String keyWord);

    /**
     * 获取首页推荐图片
     */
    List<ArticleHomeRightDTO> recommendArticle() ;

    /**
     * 获取"阅读发现"文章（随机文章）
     * @return
     */
    List<ArticleHomeRightDTO> randomRecommendArticle() ;

    /**
     * 更新"小编推荐"文章
     * @param articleHomeRightVOList
     */
    void updateRecommendArticleList(List<ArticleHomeRightVO> articleHomeRightVOList);

    /**
     * 根据文章ID查询文章内容
     * @param id：文章ID
     * @return 文章信息
     */
    ArticleDTO getArticleById(String id);

    /**
     * 管理员获取文章所有信息
     * @return 文章列表
     */
    List<ArticleDTO> getPostInfoList();


    /**
     * 审核或反审核文章（列表）
     * @param articleVOS 图片列表
     * @return 是否成功
     */
    boolean AuditArticle(AuditBanEnum enumEnum, List<ArticleVO> articleVOS);

    /**
     * 保存/更新标签
     * @param articleId
     * @param tagNameList
     */
    void saveArticleTags(String articleId,List<String> tagNameList);
}
