package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc 图片ID和图片标签ID对应关系
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_img_tags")
public class ImgTags {
    /**
     * id（雪花ID，MyBatisPlus实现）
      */
    @TableId
    private String id;
    /**
     * 文章ID
     */
    private String imgId;
    /**
     * 文章标签ID
     */
    private String tagId;
}
