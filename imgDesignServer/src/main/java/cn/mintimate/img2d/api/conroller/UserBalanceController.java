package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.DailySignService;
import cn.mintimate.img2d.api.Service.UserBalanceService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.Result;
import cn.mintimate.img2d.api.vo.TradeRecordVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户代币控制器
 *
 * @author Mintimate
 */
@RestController
@RequiredPermission
@RequestMapping("/userBalance")
public class UserBalanceController {
    @Resource
    private UserBalanceService userBalanceService;
    @Resource
    private DailySignService dailySignService;
    @Resource
    private HttpServletRequest request;

    /**
     * 获取用户最新积分情况
     * @return
     */
    @GetMapping("/getLatest")
    public Result getUserBalance() {
        return Result.ok(userBalanceService.getUserBalanceInfo());
    }

    /**
     * 给其他用户充电
     * @param tradeRecordVO
     * @return
     */
    @PostMapping("/chargeAuthor")
    public Result chargeAuthor(@RequestBody TradeRecordVO tradeRecordVO) {
        return Result.ok(userBalanceService.chargeAuthor(tradeRecordVO));
    }

    /**
     * 获取当月用户签到情况
     *
     * @return
     */
    @GetMapping("/getDailySign")
    public Result getDailySign() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        return Result.ok(dailySignService.monthSigned(String.valueOf(request.getAttribute("userID")), today));
    }

    /**
     * 用户签到
     *
     * @return
     */
    @PostMapping("/makeDailySign")
    public Result makeDailySign() {
        return Result.ok(userBalanceService.makeDailySign());
    }
}
