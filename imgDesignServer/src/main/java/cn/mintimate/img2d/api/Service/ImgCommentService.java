package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.CommentDTO;
import cn.mintimate.img2d.api.dto.ReplyDTO;
import cn.mintimate.img2d.api.entity.ImgComment;
import cn.mintimate.img2d.api.vo.CommentVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface ImgCommentService extends IService<ImgComment> {
    PageResultUntil<CommentDTO> listComments(String ImgId);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(String commentId,Integer currentPage);

    void saveComment(CommentVO commentVO);

    /**
     * 管理员获取全部评论
     * @return
     */
    List<CommentDTO> listCommentsForAdmin();

}
