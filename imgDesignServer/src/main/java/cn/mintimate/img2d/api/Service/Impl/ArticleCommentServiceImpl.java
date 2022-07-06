package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ArticleCommentService;
import cn.mintimate.img2d.api.dao.ArticleCommentDao;
import cn.mintimate.img2d.api.dto.CommentDTO;
import cn.mintimate.img2d.api.dto.ReplyCountDTO;
import cn.mintimate.img2d.api.dto.ReplyDTO;
import cn.mintimate.img2d.api.entity.ArticleComment;
import cn.mintimate.img2d.api.until.PageUtil;
import cn.mintimate.img2d.api.until.RedisUtil;
import cn.mintimate.img2d.api.vo.CommentVO;
import cn.mintimate.img2d.api.until.PageResultUntil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_COMMENT_USER_LIKE;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentDao, ArticleComment> implements ArticleCommentService {
    @Resource
    private ArticleCommentDao commentDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private HttpServletRequest request;

    @Override
    public PageResultUntil<CommentDTO> listComments(String articleId) {
        // 根据文章ID查找评论数量
        long commentCount = commentDao.selectCount(new LambdaQueryWrapper<ArticleComment>()
                .eq(Objects.nonNull(articleId), ArticleComment::getArticleImgId, articleId)
                .isNull(Objects.isNull(articleId), ArticleComment::getArticleImgId)
                .isNull(ArticleComment::getParentId)
                .eq(ArticleComment::getIsReview, 1));
        if (commentCount == 0) {
            return new PageResultUntil<>();
        }
        // 分页查询评论集合
        List<CommentDTO> commentDTOList = commentDao.listComments(
                PageUtil.getLimitCurrent(),
                PageUtil.getSize(),
                articleId);
        if (CollectionUtils.isEmpty(commentDTOList)){
            return new PageResultUntil<>();
        }
        Map<Object, Object> likeCountMap=redisUtil.hmget(IMG2D_COMMENT_USER_LIKE);
        // 提取评论id集合
        List<String> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentDao.listReplies(commentIdList);
        // 封装回复点赞量
        replyDTOList.forEach(item -> item.setLikeCount((Integer) likeCountMap.get(item.getId().toString())));
        // 根据评论id分组回复数据
        Map<String, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<String, Integer> replyCountMap = commentDao.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentDTOList.forEach(item -> {
            item.setLikeCount((Integer) likeCountMap.get(item.getId()));
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResultUntil(commentCount,commentDTOList);
    }

    @Override
    public void saveComment(CommentVO commentVO) {
        ArticleComment articleComment = ArticleComment.builder()
                .userId(String.valueOf(request.getAttribute("userID")))
                .replyUserId(commentVO.getReplyUserId())
                .articleImgId(commentVO.getArticleImgId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .isReview(1)
                .build();
        commentDao.insert(articleComment);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(String commentId, Integer currentPage) {
        PageUtil.setCurrentPage(new Page<>(currentPage, 5));
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentDao.listRepliesByCommentId(PageUtil.getLimitCurrent(), PageUtil.getSize(), commentId);
        return replyDTOList;
    }

    @Override
    public List<CommentDTO> listCommentsForAdmin() {
        List<CommentDTO> list=commentDao.listCommentsForAdmin();
        // 提取评论id集合
        List<String> commentIdList = list.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentDao.listRepliesForAdmin(commentIdList);
        // 根据评论id分组回复数据
        Map<String, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 封装评论数据
        list.forEach(item -> {
            item.setReplyDTOList(replyMap.get(item.getId()));
        });
        return list;
    }
}
