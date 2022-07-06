package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单信息VO
 *
 * @author Mintimate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordVO {
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
}
