package cn.mintimate.img2d.api.config;


import cn.mintimate.img2d.api.hander.AuthenticationInterceptorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    // 定位到项目文件夹下的SystemData文件夹，作为个人静态资源目录
    static final  String IMG_PATH=System.getProperty("user.dir")+"/SystemData/";
    static final  String IMG_PATH_TWO=System.getProperty("user.dir")+"/Test/";

    @Autowired
    AuthenticationInterceptorHandler authenticationInterceptorHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        registry.addInterceptor(authenticationInterceptorHandler)
                .addPathPatterns("/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射
        registry.addResourceHandler("/SystemData/**")
                .addResourceLocations("file:"+IMG_PATH);
        registry.addResourceHandler("/SystemDataTest/**")
                .addResourceLocations("file:"+IMG_PATH_TWO);
        super.addResourceHandlers(registry);
    }


}
