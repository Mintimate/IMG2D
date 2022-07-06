package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户审核评论VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCommentVO {
    /**
     * id
     */
    private String  id;
    /**
     * 评论内具体内容
     */
    private String commentContent;
    /**
     * 是否删除
     */
    private Integer isDelete;
}
