package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.vo.AlipayOrderVO;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface PayService {
    String saveOrderInfo(AlipayOrderVO alipayOrderVO);

    /**
     * 保存支付订单
     * @param out_trade_no
     * @param total_amount
     */
    void finishPayment(String out_trade_no,double total_amount);
}
