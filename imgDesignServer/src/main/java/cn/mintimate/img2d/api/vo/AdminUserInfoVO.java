package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员发送新用户信息VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVO {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户电子邮箱
     */
    private String userEmail;
    /**
     * 用户角色
     */
    private String userRole;
    /**
     * 用户头像地址
     */
    private String userAvatarPath;
    /**
     * 用户电话号码
     */
    private String userTele;
    /**
     * 用户个性签名
     */
    private String userSignature;
    /**
     * 用户空间
     */
    private String userSpace;
    /**
     * 是否封禁
     */
    private Integer isBan;
}
