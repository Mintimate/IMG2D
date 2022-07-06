package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户三连情况DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFavDTO {
    /**
     * 用户是否关注了作者
     */
    private boolean hasFollow;
    /**
     * 用户是否已经点赞图片
     */
    private boolean hasLikeImg;
    /**
     * 用户是否已经收藏图片
     */
    private boolean hasCollect;
}
