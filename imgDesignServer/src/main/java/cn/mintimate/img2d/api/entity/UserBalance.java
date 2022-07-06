package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户账单实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_user_balance")
public class UserBalance {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private String id;

    /**
     * 用户昵称
     */
    private String userId;

    /**
     * 用户历史总计拥有积分
     */
    private long historyBalance;

    /**
     * 积分金额
     */
    private long balance;
}
