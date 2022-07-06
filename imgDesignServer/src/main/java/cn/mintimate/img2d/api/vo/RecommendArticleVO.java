package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员侧栏"推荐文章"
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendArticleVO {
    /**
     * 文章描述
     */
    private String articleDesc;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章ID
     */
    private String id;
    /**
     * 文章推荐级别
     */
    private String topId;
}
