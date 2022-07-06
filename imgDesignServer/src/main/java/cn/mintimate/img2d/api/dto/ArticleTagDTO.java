package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章标签DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagDTO {
    /**
     * id （雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 标签名
     */
    private String tagName;
}
