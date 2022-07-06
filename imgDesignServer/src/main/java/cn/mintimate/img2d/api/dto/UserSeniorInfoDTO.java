package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户高级信息DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSeniorInfoDTO {
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String  id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户电话号码
     */
    private String userTele;
    /**
     * 用户QQ_ID（QQ互联提供）
     */
    private String userQqOpenid;
    /**
     * 用户权限
     */
    private String userRole;
}
