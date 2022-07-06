package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户空间内基本数据
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSpaceDTO {
    /**
     * 用户ID
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
     * 用户个性签名
     */
    private String userSignature;
    /**
     * 用户角色权限认证
     */
    private String userRole;
}
