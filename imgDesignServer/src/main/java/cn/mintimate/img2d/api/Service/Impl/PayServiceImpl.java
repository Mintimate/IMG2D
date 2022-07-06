package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.PayService;
import cn.mintimate.img2d.api.constant.AlipayConfig;
import cn.mintimate.img2d.api.dao.UserBalanceDao;
import cn.mintimate.img2d.api.dao.UserDao;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.entity.UserBalance;
import cn.mintimate.img2d.api.exception.dataException;
import cn.mintimate.img2d.api.until.RedisUtil;
import cn.mintimate.img2d.api.vo.AlipayOrderVO;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_ALIPAY_ORDER;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserDao userDao;
    @Resource
    private UserBalanceDao userBalanceDao;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public String saveOrderInfo(AlipayOrderVO alipayOrderVO) {
        if (Objects.isNull(alipayOrderVO)){
            throw new dataException("订单信息不可为空");
        }
        // 订单号存入Redis
        redisUtil.hset(IMG2D_ALIPAY_ORDER,alipayOrderVO.getOut_trade_no(),request.getAttribute("userID"));
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        try {
            alipayRequest.setBizContent(JSON.toJSONString(alipayOrderVO));
            //请求
            String result = alipayClient.pageExecute(alipayRequest, "GET").getBody();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void finishPayment(String out_trade_no, double total_amount) {
        Long balance= (long) total_amount*100;
        String userid= String.valueOf(redisUtil.hget(IMG2D_ALIPAY_ORDER,out_trade_no));
        User user=userDao.selectById(userid);
        if (Objects.isNull(user)){
            throw new dataException("订单获取用户失败！！");
        }
        UserBalance userBalance= userBalanceDao.selectOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId,userid));
        if (Objects.isNull(userBalance)){
            userBalance=UserBalance.builder()
                    .userId(userid)
                    .balance(balance)
                    .historyBalance(balance)
                    .build();
            userBalanceDao.insert(userBalance);
        }
        else{
            userBalance=UserBalance.builder()
                    .id(userBalance.getId())
                    .balance(userBalance.getBalance()+balance)
                    .historyBalance(userBalance.getHistoryBalance()+balance)
                    .build();
            userBalanceDao.updateById(userBalance);
        }
    }
}
