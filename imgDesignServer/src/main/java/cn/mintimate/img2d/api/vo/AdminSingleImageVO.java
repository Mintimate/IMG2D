package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员单张图片VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSingleImageVO {
    /**
     * 图片ID
     */
    private String id;
    /**
     * 图片昵称
     */
    private String imgName;
    /**
     * 分类ID
     */
    private String cateId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 是否审核
     */
    private Integer isAudit;
    /**
     * 文章标签
     */
    private String[] tagNames;
}
