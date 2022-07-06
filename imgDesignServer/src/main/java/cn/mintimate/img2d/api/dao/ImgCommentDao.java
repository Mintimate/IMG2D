package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.CommentDTO;
import cn.mintimate.img2d.api.dto.ReplyCountDTO;
import cn.mintimate.img2d.api.dto.ReplyDTO;
import cn.mintimate.img2d.api.entity.ImgComment;
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
public interface ImgCommentDao extends BaseMapper<ImgComment> {
    /**
     * 评论列表 查看评论
     *
     * @param articleId 文章id
     * @param current 当前页码
     * @param size 大小
     * @return 评论集合
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size,
                                  @Param("articleImgId") String articleId);

    /**
     * 查看评论id集合下的回复（第一页）
     *
     * @param commentIdList 评论id集合
     * @return 回复集合
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<String> commentIdList);

    /**
     * 查看当条评论下的回复
     *
     * @param commentId 评论id
     * @param current 当前页码
     * @param size 大小
     * @return 回复集合
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("current") Long current, @Param("size") Long size,
                                          @Param("commentId") String commentId);

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id集合
     * @return 回复数量
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<String> commentIdList);

    /**
     * 管理员获取全部评论
     * @return
     */
    List<CommentDTO> listCommentsForAdmin();


    /**
     * 管理员根据评论ID查询评论的回复列表
     * @param commentIdList
     * @return
     */
    List<ReplyDTO> listRepliesForAdmin(@Param("commentIdList") List<String> commentIdList);
}
