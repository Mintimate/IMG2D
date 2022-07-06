package cn.mintimate.img2d.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核类模版枚举
 *
 * @author Mintimate
 */
@Getter
@AllArgsConstructor
public enum AuditBanEnum {
    /**
     * 通过
     */
    PASS(1,0),
    /**
     * 不通过
     */
    NOT_PASS(0,1);
    private final long auditState;
    /**
     * 账号封禁
     */
    private final Integer bandState;
}
