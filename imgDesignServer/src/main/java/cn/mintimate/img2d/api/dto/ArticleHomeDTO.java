package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 首页文章DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHomeDTO {
    /**
     * 文章ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 作者ID
     */
    private String userId;
    /**
     * 作者昵称
     */
    private String username;
    /**
     * 分类ID
     */
    private String categoryId;
    /**
     * 分类名称
     */
    private String cateName;
    /**
     * 文章头图链接
     */
    private String articleBanner;
    /**
     * 标题
     */
    private String articleTitle;
    /**
     * 文章简介描述
     */
    private String articleDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 文章类型（原创？转载）
     */
    private String type;
    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 文章标签
     */
    private List<ArticleTagDTO> tagDTOList;

}
