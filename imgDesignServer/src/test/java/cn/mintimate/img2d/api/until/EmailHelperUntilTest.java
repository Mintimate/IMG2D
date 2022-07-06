package cn.mintimate.img2d.api.until;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * Desc
 *
 * @author Mintimate
 */
@SpringBootTest
class EmailHelperUntilTest {
    @Resource
    private EmailHelperUntil emailHelper;

    @Test
    public void sendSuggestion() {
        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("to", new String[]{"1936573860@qq.com"});
        valueMap.put("title","IMG2D欢迎您的注册");
        valueMap.put("url","https://www.mintimate.cn");
        valueMap.put("userName","梅狗是真的狗");
    }
}