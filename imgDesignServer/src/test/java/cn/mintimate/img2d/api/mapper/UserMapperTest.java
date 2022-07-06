package cn.mintimate.img2d.api.mapper;

import cn.mintimate.img2d.api.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserMapperTest {
    @Resource
    private UserDao mapper;

    @Test
    void test(){
        mapper.selectList(null).forEach(System.out::println);
    }

}