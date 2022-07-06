package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录信息VO
 *
 * @author Mintimate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginVO {
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 账号方式（电话｜邮箱）
     */
    private String mode;
}
