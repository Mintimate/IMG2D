package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 单张图片组合信息DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleIMG_DTO {
    /**
     * 图片ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 图片昵称
     */
    private String imgName;
    /**
     * 原图地址
     */
    private String imgHostPath;
    /**
     * 分类ID（雪花ID，MyBatisPlus实现）
     */
    private String cateId;
    /**
     * 分类昵称
     */
    private String cateName;
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户角色
     */
    private String userRole;
    /**
     * 用户头像地址
     */
    private String userAvatarPath;
    /**
     * 用户空间
     */
    private String userSpace;
    /**
     * 图片点赞数量
     */
    private long likeCount;
    /**
     * 图片收藏次数
     */
    private long collectCount;
    /**
     * 图片上传时间
     */
    private Date createTime;
    /**
     * 文章标签
     */
    private List<ImgTagDTO> tagDTOList;
}
