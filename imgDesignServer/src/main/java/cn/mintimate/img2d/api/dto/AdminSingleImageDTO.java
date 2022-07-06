package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 管理员查找图片DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSingleImageDTO {
    /**
     * 图片ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 图片昵称
     */
    private String imgName;
    /**
     * 图床地址
     */
    private String imgHostPath;
    /**
     * 分类ID
     */
    private String cateId;
    /**
     * 分类昵称
     */
    private String cateName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 图片点赞数量
     */
    private Integer likeCount;
    /**
     * 图片收藏次数
     */
    private Integer collectCount;
    /**
     * 图片上传时间
     */
    private Date createTime;
    /**
     * 是否审核
     */
    private Integer isAudit;
    /**
     * 是否删除
     */
    private Integer delFlag;
    /**
     * 感知哈希
     */
    private String perceptualHash;
    /**
     * 文章标签
     */
    private List<ImgTagDTO> tagDTOList;

}
