package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户审核资料VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentVO {
    /**
     * 申请的用户ID
     */
    private String userId;
    /**
     * 认证的角色
     */
    private String identWhat;
    /**
     * 证明图片
     */
    private String identURL;
}
