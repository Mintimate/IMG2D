package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.PayService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.vo.AlipayOrderVO;
import cn.mintimate.img2d.api.dto.Result;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;


/**
 * 外接支付控制器
 *
 * @author Mintimate
 */
@RestController
public class PayController {
    @Resource
    private PayService payService;

    /**
     * 接收订单信息
     *
     * @param alipayOrderVO：订单信息
     * @return
     */
    @RequestMapping("/alipay/pay")
    @RequiredPermission
    public Result payByAlipay(@RequestBody AlipayOrderVO alipayOrderVO) {
        // 商户网站唯一订单号
        String out_trade_no = UuidUtil.getTimeBasedUuid().toString();
        alipayOrderVO.setOut_trade_no(out_trade_no);
        // 返回支付宝交易链接，前端进行重定向
        String redirect = payService.saveOrderInfo(alipayOrderVO);
        if (!Objects.isNull(redirect)) {
            return Result.ok(redirect);
        }
        return Result.fail("支付参数错误");
    }

    /**
     * 用户支付成功后，支付宝调用回调信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/alipay/notifyUrl")
    public void notifyUrl(HttpServletRequest request) {
        String out_trade_no = null;
        double price = 0;
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            String[] strings = parameterMap.get(s);
            for (int i = 0; i < strings.length; i++) {
                if (s.equals("out_trade_no")) {
                    out_trade_no = strings[i];
                }
                if (s.equals("total_amount")) {
                    price = Double.parseDouble(strings[i]);
                }
            }
        }
        payService.finishPayment(out_trade_no, price);
    }

    /**
     * 支付成功后重定向页面：关闭当前页面
     *
     * @return
     */
    @RequestMapping("/alipay/returnUrl")
    public String returnUrl() {
        return "<script>" +
                "window.opener = null;" +
                "window.open(\"\", \"_self\");" +
                "window.close();" +
                "</script>";
    }
}
