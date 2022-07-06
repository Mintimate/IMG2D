package cn.mintimate.img2d.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
    /**
     * 文章ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 作者ID
     */
    private String userId;
    /**
     * 分类昵称
     */
    private String cateName;
    /**
     * 分类ID
     */
    private String categoryId;
    /**
     * 文章标签
     */
    private List<String> tagNameList;

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
    /**
     * 是否置顶
     */
    private Integer isTop;
}
