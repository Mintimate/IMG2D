package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.DailySignService;
import cn.mintimate.img2d.api.Service.UserBalanceService;
import cn.mintimate.img2d.api.dao.TradeRecordDao;
import cn.mintimate.img2d.api.dao.UserBalanceDao;
import cn.mintimate.img2d.api.dto.UserBalanceDTO;
import cn.mintimate.img2d.api.entity.TradeRecord;
import cn.mintimate.img2d.api.entity.UserBalance;
import cn.mintimate.img2d.api.until.BeanCopyUtil;
import cn.mintimate.img2d.api.vo.TradeRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceDao, UserBalance> implements UserBalanceService {
    @Resource
    private UserBalanceDao userBalanceDao;
    @Resource
    private TradeRecordDao tradeRecordDao;
    @Resource
    private HttpServletRequest request;
    @Resource
    private DailySignService dailySignService;

    @Override
    public UserBalanceDTO getUserBalanceInfo() {
        String userID = String.valueOf(request.getAttribute("userID"));
        if (Objects.isNull(userID)) {
            return null;
        }
        UserBalanceDTO userBalanceDTO = BeanCopyUtil.copyObject(userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, userID)), UserBalanceDTO.class);
        return userBalanceDTO;
    }

    @Override
    public UserBalanceDTO chargeAuthor(TradeRecordVO tradeRecordVO) {
        tradeRecordVO.setPayerId(String.valueOf(request.getAttribute("userID")));
        UserBalance payerBalance = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, tradeRecordVO.getPayerId()));
        UserBalance payeeBalance = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, tradeRecordVO.getPayeeId()));
        payerBalance.setBalance(payerBalance.getBalance() - tradeRecordVO.getPrice());
        payeeBalance.setBalance(payeeBalance.getBalance() + tradeRecordVO.getPrice());
        userBalanceDao.updateById(payeeBalance);
        userBalanceDao.updateById(payerBalance);
        TradeRecord tradeRecord = TradeRecord.builder()
                .payeeId(tradeRecordVO.getPayeeId())
                .payerId(tradeRecordVO.getPayerId())
                .price(tradeRecordVO.getPrice())
                .build();
        tradeRecordDao.insert(tradeRecord);
        UserBalanceDTO userBalanceDTO = BeanCopyUtil.copyObject(userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, tradeRecordVO.getPayerId())), UserBalanceDTO.class);
        return userBalanceDTO;
    }

    @Override
    public boolean enoughBalance(Integer balance) {
        String userid = String.valueOf(request.getAttribute("userID"));
        UserBalance userBalance = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, userid));
        if (userBalance.getBalance() < balance) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void deductBalance(Integer balance, String toWho) {
        String userid = String.valueOf(request.getAttribute("userID"));
        UserBalance userBalance = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, userid));
        UserBalance userBalanceReceiver = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, toWho));
        // 付款方
        userBalanceDao.update(null, new LambdaUpdateWrapper<UserBalance>()
                .set(UserBalance::getBalance, userBalance.getBalance() - balance)
                .eq(UserBalance::getUserId, userBalance.getUserId()));
        // 收款方
        userBalanceDao.update(null, new LambdaUpdateWrapper<UserBalance>()
                .set(UserBalance::getBalance, userBalanceReceiver.getBalance() + balance)
                .set(UserBalance::getHistoryBalance, userBalanceReceiver.getHistoryBalance() + balance)
                .eq(UserBalance::getUserId, userBalanceReceiver.getUserId()));
    }

    @Override
    public String makeDailySign() {
        String userid = String.valueOf(request.getAttribute("userID"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        Map<String, Object> signResult = dailySignService.doSign(userid, today);
        if (signResult.get("code").equals(200)) {
            int continuous = (Integer) signResult.get("continuous");
            int integral = 50;
            if (continuous < 5) {
                integral = continuous * 10;
            }
            UserBalance userBalance = userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                    .eq(UserBalance::getUserId, userid));
            if (Objects.isNull(userBalance)) {
                userBalance = UserBalance.builder()
                        .userId(userid)
                        .balance(integral)
                        .historyBalance(integral)
                        .build();
            } else {
                userBalance = UserBalance.builder()
                        .id(userBalance.getId())
                        .balance(userBalance.getBalance() + integral)
                        .historyBalance(userBalance.getHistoryBalance() + integral)
                        .build();
            }
            this.saveOrUpdate(userBalance);
            return signResult.get("message") + ",获得积分" + integral + "分";
        }
        return (String) signResult.get("message");
    }
}
