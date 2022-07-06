package cn.mintimate.img2d.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文章分类VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCategoryVO {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 分类名
     */
    @NotNull
    @NotBlank(message="分类名不能为空嗷")
    private String categoryName;

}
