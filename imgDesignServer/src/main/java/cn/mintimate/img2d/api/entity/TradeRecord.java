package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单交易记录实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_trade_record")
public class TradeRecord {
    /**
     * id（雪花ID，MyBatisPlus实现）
      */
    @TableId
    private String id;
    /**
     * 支付方
     */
    private String payerId;
    /**
     * 收款方
     */
    private String payeeId;
    /**
     * 交易金额
     */
    private long price;
    /**
     * 交易时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 订单状态
     */
    private String state = "Success";
}
