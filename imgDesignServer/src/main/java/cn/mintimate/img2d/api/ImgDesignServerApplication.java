package cn.mintimate.img2d.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.mintimate.img2d.api.dao")
public class ImgDesignServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImgDesignServerApplication.class, args);
    }

}
