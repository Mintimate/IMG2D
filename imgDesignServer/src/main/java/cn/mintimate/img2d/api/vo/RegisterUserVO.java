package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册信息VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserVO {
    /**
     * 用户邮箱
     */
    @NotBlank(message="用户邮箱不能为空嗷")
    private String userEmail;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
}
