package cn.mintimate.img2d.api.until;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class RedisUtilTest {
    @Resource
    private RedisUtil redisUtil;

    /**
     * 插入缓存数据
     */
    @Test
    public void set() {
        redisUtil.set("redis_key", "redis_vale!!!");
        redisUtil.set("Test","HelloWorld",60);
    }

    /**
     * 读取缓存数据
     */
    @Test
    public void get() {
        String value = (String) redisUtil.get("Test");
        log.debug("试试看");
        System.out.println(value);
    }

}