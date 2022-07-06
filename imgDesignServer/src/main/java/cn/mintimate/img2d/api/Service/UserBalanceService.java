package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.UserBalanceDTO;
import cn.mintimate.img2d.api.entity.UserBalance;
import cn.mintimate.img2d.api.vo.TradeRecordVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface UserBalanceService extends IService<UserBalance> {
    /**
     * 获取用户当前所拥有的支付数据
     * @return
     */
    UserBalanceDTO getUserBalanceInfo();

    /**
     * 给用户发电
     * @param tradeRecordVO
     * @return
     */
    UserBalanceDTO chargeAuthor(TradeRecordVO tradeRecordVO);

    /**
     * 是否有足够的积分扣除
     * @param balance 需要扣除的积分
     * @return 是否拥有足够积分
     */
    boolean enoughBalance(Integer balance);

    /**
     * 扣除积分
     */
    void deductBalance(Integer balance,String toWho);

    /**
     * 用户签到
     */
    String makeDailySign();
}
