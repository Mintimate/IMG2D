package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基本信息DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfoDTO {
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String  id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 用户签名
     */
    private String userSignature;
    /**
     * 用户电话号码
     */
    private String userTele;
    /**
     * 用户个人主页
     */
    private String  userSpace;
    /**
     * 用户头像地址
     */
    private String userAvatarPath;
    /**
     * 用户权限
     */
    private String userRole;
}
