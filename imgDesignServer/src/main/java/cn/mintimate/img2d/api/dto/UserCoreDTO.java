package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息核心信息（用于登录）DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCoreDTO {
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String  id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户头像
     */
    private String userAvatarPath;
    /**
     * 用户Token
     */
    private String UserToken;
    /**
     * 用户权限
     */
    private String userRole;
}
