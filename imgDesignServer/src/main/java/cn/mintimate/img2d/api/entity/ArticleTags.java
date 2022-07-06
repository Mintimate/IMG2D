package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章ID和文章标签ID对应关系实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_article_tags")
public class ArticleTags {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private String id;
    /**
     * 文章ID
     */
    private String articleId;
    /**
     * 文章标签ID
     */
    private String tagId;
}
