package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片列表信息DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaterFallIMG_DTO {
    /**
     * 图片ID（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 图片昵称
     */
    private String imgName;
    /**
     * 图片缩略图地址
     */
    private String imgThumbnailPath;
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户头像地址
     */
    private String userAvatarPath;

}
