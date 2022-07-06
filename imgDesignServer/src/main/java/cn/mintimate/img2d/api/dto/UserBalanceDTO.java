package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户积分DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String userId;
    /**
     * 用户累计拥有积分
     */
    private long historyBalance;
    /**
     * 用户当前有效积分
     */
    private long balance;
}
