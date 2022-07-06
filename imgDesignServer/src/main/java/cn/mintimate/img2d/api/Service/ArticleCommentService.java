package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.CommentDTO;
import cn.mintimate.img2d.api.dto.ReplyDTO;
import cn.mintimate.img2d.api.entity.ArticleComment;
import cn.mintimate.img2d.api.vo.CommentVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Desc 评论模块
 *
 * @author Mintimate
 */
public interface ArticleCommentService extends IService<ArticleComment> {
    /**
     * 查看评论
     *
     * @param articleId 文章id
     * @return 评论列表
     */
    PageResultUntil<CommentDTO> listComments(String articleId);

    /**
     * 保存评论
     * @param commentVO 评论信息
     */
    void saveComment(CommentVO commentVO);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(String commentId, Integer currentPage);

    /**
     * 管理员获取全部评论
     * @return
     */
    List<CommentDTO> listCommentsForAdmin();
}
