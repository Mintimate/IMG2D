package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 评论实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_article_comment")
public class ArticleComment {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private String id;

    /**
     * 评论用户Id
     */
    private String userId;

    /**
     * 回复用户id
     */
    private String replyUserId;

    /**
     * 评论文章id
     */
    private String articleImgId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论id
     */
    private String parentId;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否审核
     */
    private Integer isReview;

    /**
     * 创建时间
     */

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
