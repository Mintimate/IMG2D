package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ArticleService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.ArticleDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeRightDTO;
import cn.mintimate.img2d.api.entity.Article;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.vo.ArticleHomeRightVO;
import cn.mintimate.img2d.api.vo.ArticleVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import cn.mintimate.img2d.api.dto.Result;
import cn.mintimate.img2d.api.vo.RecommendArticleVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 文章模块控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Resource
    private HttpServletRequest request;

    /**
     * 获取首页文章（分页）
     *
     * @return 分页获取
     */
    @GetMapping("/getHomeArticle/{page}")
    public Result<PageResultUntil<ArticleHomeDTO>> listHomeArticle(
            @PathVariable(value = "page") Integer page
    ) {
        return Result.ok(articleService.listHomeArticle(page));
    }


    /**
     * 获取首页文章（分页、分类）
     *
     * @param page 页码
     * @param type 分类
     * @return 文章传输对象
     */
    @GetMapping("/getHomeArticleByType/{type}/{page}")
    public Result<PageResultUntil<ArticleHomeDTO>> getHomeArticleByType(
            @PathVariable(value = "page") Integer page,
            @PathVariable(value = "type") String type) {

        return Result.ok(articleService.listHomeArticleByType(type, page));
    }

    /**
     * 根据关键字获取文章
     *
     * @param keyWords 关键字
     * @return 文章传输对象
     */
    @GetMapping("/getHomeArticleBySearch/{keyWords}")
    public Result<List<ArticleHomeDTO>> getHomeArticleBySearch(
            @PathVariable(value = "keyWords") String keyWords) {
        return Result.ok(articleService.getHomeArticleByKeyWord(keyWords));
    }


    /**
     * 保存文章（文章作者保存或修改文章）
     *
     * @param articleVO 文章视图对象
     * @return 是否成功
     */
    @RequiredPermission
    @PostMapping("/saveArticle")
    public Result<?> saveArticle(@RequestBody ArticleVO articleVO) {
        // session获取Userid
        articleVO.setUserId(String.valueOf(request.getAttribute("userID")));
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    /**
     * 根据文章ID获取文章数据
     *
     * @param articleId
     * @return
     */
    @GetMapping("/getArticleDetail/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") String articleId) {
        return Result.ok(articleService.getArticleById(articleId));
    }

    /**
     * 获取推荐文章
     *
     * @return
     */
    @GetMapping("/getRecommendArticle")
    public Result<List<ArticleHomeRightDTO>> recommendArticle() {
        return Result.ok(articleService.recommendArticle());
    }

    /**
     * 获取随机推荐文章
     * @return
     */
    @GetMapping("/getRandomRecommendArticle")
    public Result<List<ArticleHomeRightDTO>> randomRecommendArticle(){
        return Result.ok(articleService.randomRecommendArticle());
    }

    /**
     * 管理员获取文章基本信息
     *
     * @return 文章信息
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getAllInfo")
    public Result getAllInfoForAdmin() {
        return Result.ok(articleService.getPostInfoList());
    }


    /**
     * 根据文章标题，查找文章
     * @param recommendArticleVO
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/getArticleBaseByTitle")
    public Result getArticleBaseByTitle(@RequestBody RecommendArticleVO recommendArticleVO){
        System.out.println(recommendArticleVO.getArticleTitle());
        Article article=articleService.getOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getArticleTitle,recommendArticleVO.getArticleTitle()));
        if (Objects.nonNull(article)){
            return Result.ok(article);
        }
        else {
            return Result.fail();
        }
    }

    @RequiredPermission(admin = true)
    @PostMapping("/admin/updateRecommendArticle")
    public Result updateRecommendArticle(@RequestBody List<ArticleHomeRightVO> articleHomeRightVOList){
        articleService.updateRecommendArticleList(articleHomeRightVOList);
        return Result.ok();
    }


    /**
     * 管理员审核/反审核文章
     *
     * @param articleVOs 需要更改审核状态的文章
     * @param isAudit    审核/反审核类别
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/auditPost/{auditType}")
    public Result auditPost(@RequestBody List<ArticleVO> articleVOs,
                            @PathVariable(value = "auditType") long isAudit) {
        if (isAudit == AuditBanEnum.PASS.getAuditState()) {
            articleService.AuditArticle(AuditBanEnum.PASS, articleVOs);
        } else {
            articleService.AuditArticle(AuditBanEnum.NOT_PASS, articleVOs);
        }
        return Result.ok();
    }

    /**
     * 更新文章信息
     *
     * @param articleVO  文章视图对象
     * @param updateWhat 更新属性名
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/updatePost/{updateWhat}")
    public Result updatePost(@RequestBody ArticleVO articleVO,
                             @PathVariable(value = "updateWhat") String updateWhat) {
        if(Objects.isNull(updateWhat)){
            updateWhat="tagNames";
        }
        Article article=articleService.getById(articleVO.getId());
        if (Objects.isNull(article)){
            return Result.fail("对象错误");
        }
        if (updateWhat.equals("cate")&&Objects.nonNull(articleVO.getCategoryId())){
            article.setCategoryId(articleVO.getCategoryId());
            return Result.ok(articleService.updateById(article));
        }
        else if(updateWhat.equals("articleTitle")&&Objects.nonNull(articleVO.getArticleTitle())){
            article.setArticleTitle(articleVO.getArticleTitle());
            return Result.ok(articleService.updateById(article));
        }
        List<String> tagNameList = new ArrayList<>();
        Collections.addAll(tagNameList, articleVO.getTagNameList().toArray(new String[tagNameList.size()]));
        articleService.saveArticleTags(articleVO.getId(), tagNameList);
        return Result.ok();
    }

}
