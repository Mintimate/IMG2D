package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ArticleCommentService;
import cn.mintimate.img2d.api.Service.ImgCommentService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.CommentDTO;
import cn.mintimate.img2d.api.dto.ReplyDTO;
import cn.mintimate.img2d.api.entity.ArticleComment;
import cn.mintimate.img2d.api.entity.ImgComment;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.until.BeanCopyUtil;
import cn.mintimate.img2d.api.vo.AdminCommentVO;
import cn.mintimate.img2d.api.vo.CommentVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import cn.mintimate.img2d.api.dto.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Resource
    private ArticleCommentService articleCommentService;
    @Resource
    private ImgCommentService imgCommentService;

    /**
     * 查找文章评论列表
     *
     * @param articleId 文章ID
     * @return
     */
    @GetMapping("/get/byArticleId")
    public Result<?> listCommentsForArticle(String articleId) {
        return Result.ok(articleCommentService.listComments(articleId));
    }

    /**
     * 查找图片评论列表
     *
     * @param articleId 文章ID
     * @return
     */
    @GetMapping("/get/byIMG")
    public Result<PageResultUntil> listCommentsForIMG(String articleId) {
        return Result.ok(imgCommentService.listComments(articleId));
    }

    /**
     * 根据图片ID分页获取评论回复表
     *
     * @param commentId 评论ID
     * @param page 页面数
     * @return
     */
    @GetMapping("/get/byIMG/{commentId}/{page}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentIdForImg(@PathVariable("commentId") String commentId,
                                                               @PathVariable("page") Integer page) {
        return Result.ok(imgCommentService.listRepliesByCommentId(commentId, page));
    }


    /**
     * 根据文章ID分页获取评论回复表
     *
     * @param commentId 评论ID
     * @param page 页面数
     * @return
     */
    @GetMapping("/get/byArticleId/{commentId}/{page}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentIdForArticle(@PathVariable("commentId") String commentId,
                                                                   @PathVariable("page") Integer page) {
        return Result.ok(articleCommentService.listRepliesByCommentId(commentId, page));
    }


    /**
     * 给图片添加评论
     *
     * @param commentVO 评论视图对象
     * @return
     */
    @RequiredPermission
    @PostMapping("/add/toIMG")
    public Result<?> saveCommentToIMG(@RequestBody CommentVO commentVO) {
        imgCommentService.saveComment(commentVO);
        return Result.ok();
    }

    /**
     * 给文章添加评论
     *
     * @param commentVO 评论视图对象
     * @return
     */
    @RequiredPermission
    @PostMapping("/add/toArticle")
    public Result<?> saveCommentToArticle(@RequestBody CommentVO commentVO) {
        articleCommentService.saveComment(commentVO);
        return Result.ok();
    }

    /**
     * 查询所有图片所拥有的评论
     *
     * @return
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getAllForIMG")
    public Result<List<CommentDTO>> getAllForIMG() {
        return Result.ok(imgCommentService.listCommentsForAdmin());
    }

    /**
     * 查询所有文章所拥有的评论
     *
     * @return
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getAllForArticle")
    public Result<List<CommentDTO>> getAllForArticle() {
        return Result.ok(articleCommentService.listCommentsForAdmin());
    }


    /**
     * 管理员审核评论
     * @param type 审核评论类型?'文章':'图片'
     * @param commentType 评论类别
     * @param adminCommentVOList 管理员评论视图对象列表
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/auditComments/{commentType}/{type}")
    public Result auditComments(@PathVariable(value = "type") Integer type,
                                @PathVariable(value = "commentType") String commentType,
                                @RequestBody List<AdminCommentVO> adminCommentVOList){
        if (type== AuditBanEnum.NOT_PASS.getBandState()){
            adminCommentVOList.forEach(item->{
                item.setIsDelete(AuditBanEnum.NOT_PASS.getBandState());
            });
        }
        else {
            adminCommentVOList.forEach(item->{
                item.setIsDelete(AuditBanEnum.PASS.getBandState());
            });
        }
        if(commentType.equals("Article")){
            adminCommentVOList.forEach(item->{
                articleCommentService.update(BeanCopyUtil.copyObject(item, ArticleComment.class),
                        new LambdaQueryWrapper<ArticleComment>().eq(ArticleComment::getId,item.getId()));
            });
        }
        else{
            adminCommentVOList.forEach(item->{
                imgCommentService.update(BeanCopyUtil.copyObject(item,ImgComment.class),
                        new LambdaQueryWrapper<ImgComment>().eq(ImgComment::getId,item.getId()));
            });
        }
        return Result.ok();
    }
}
