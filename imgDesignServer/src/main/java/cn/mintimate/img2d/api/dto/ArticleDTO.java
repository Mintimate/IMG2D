package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 单个文章内容DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
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
     * 分类昵称
     */
    private String cateName;
    /**
     * 文章首页图片地址
     */
    private String articleBanner;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章梗概
     */
    private String articleDesc;
    /**
     * 文章内容（Markdown）
     */
    private String articleContent;
    /**
     * 文章类型（原创、转载）
     */
    private String type;
    /**
     * 文章来源
     */
    private String originalUrl;
    private Date createTime;
    private Date updateTime;

    /**
     * 是否删除&审核
     */
    private String isDelete;

    /**
     * 文章标签
     */
    private List<ArticleTagDTO> tagDTOList;
}
