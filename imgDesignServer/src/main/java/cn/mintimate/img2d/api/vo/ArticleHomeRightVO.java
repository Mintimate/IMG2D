package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页右侧推荐文章DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHomeRightVO {
    /**
     * 推荐级别
     */
    private String topId;
    /**
     * 文章ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 作者昵称
     */
    private String username;
    /**
     * 标题
     */
    private String articleTitle;
    /**
     * 文章悬浮描述
     */
    private String articleDesc;
}
