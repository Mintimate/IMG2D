package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.UserAlertInfoDTO;
import cn.mintimate.img2d.api.enums.UserAlertInfoEnum;
import cn.mintimate.img2d.api.until.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Desc
 *
 * @author Mintimate
 */
@SpringBootTest
class dailySignServiceTest {
    @Resource
    private DailySignService dailySignService;
    @Resource
    private UserAlertInfoService userAlertInfoService;
    @Resource
    private RedisUtil redisUtil;

    @Test
    public  void testSign(){
//        userAlertInfoService.saveMessageAlert("1452998090465296386","UserAccount","Hello，你上次的登录IP为192.168.3.1，这次的登录IP为172.168.2.1，两次IP归属地不一样，请注意保护账号信息哦。");
    }

}