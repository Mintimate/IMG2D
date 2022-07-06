package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 回复列表DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    /**
     * 评论id（雪花ID，MyBatisPlus实现）
     */
    private String id;

    /**
     * 父评论id（雪花ID，MyBatisPlus实现）
     */
    private String parentId;

    /**
     * 用户id（雪花ID，MyBatisPlus实现）
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatarPath;

    /**
     * 个人网站
     */
    private String userSpace;

    /**
     * 被回复用户id（雪花ID，MyBatisPlus实现）
     */
    private String replyUserId;

    /**
     * 被回复用户昵称
     */
    private String replyUsername;

    /**
     * 被回复个人网站
     */
    private String replyUserSpace;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
}
