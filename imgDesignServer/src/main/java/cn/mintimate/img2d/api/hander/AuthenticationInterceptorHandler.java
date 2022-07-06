package cn.mintimate.img2d.api.hander;

import cn.mintimate.img2d.api.Service.UserService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.until.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 用户验证拦截器
 *
 * @author Mintimate
 */
@Component
@Slf4j
public class AuthenticationInterceptorHandler implements HandlerInterceptor {
    @Resource
    private UserService userService;

    /**
     * 登录拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // "不是HandlerMethod类型，则无需检查"
            return true;
        }
        // 判断权限
        return hasPermission(request, response, handler);
    }

    /**
     * 判断是否有权限
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        // 类和方法注解检测
        boolean hasLoginAnnotationOnMethod = method.getMethod().isAnnotationPresent(RequiredPermission.class);
        boolean hasLoginAnnotationOnClass=method.getBeanType().isAnnotationPresent(RequiredPermission.class);
        // 是否有注解
        RequiredPermission loginRequired;
        if (!hasLoginAnnotationOnClass){
            //类不存在LoginRequired注解，则检查方法是否有注解
            if (!hasLoginAnnotationOnMethod){
                return true;
            }
            else{
                loginRequired = method.getMethod().getAnnotation(RequiredPermission.class);
            }
        }
        else {
            loginRequired = method.getBeanType().getAnnotation(RequiredPermission.class);
        }
        // 1 存在RequiredPermission注解，判断用户是否登录
        // 1.1 http 请求头中取出 token
        String token = request.getHeader("Authorization");
        if (Objects.isNull(token)) {
            Refuse(response);
            return false;
        }
        // 1.2 取出token内的信息并封装到request内
        Object[] objects = JwtUtil.checkToken(token);
        if (objects == null) {
            Refuse(response);
            return false;
        }
        request.setAttribute("userID", objects[0]);
        request.setAttribute("userName", objects[1]);
        log.info("地址为：" + request.getServletPath() + "这次的值为：" + token);
        // 2 admin=true,则代表为普通用户登录
        if (loginRequired.admin()) {
            return checkAdmin(String.valueOf(objects[0]));
        }
        //3.登录状态检查,使用response返回指定信息
        log.info("required=true,需检查是否登录");
        return true;
    }

    /**
     * 判断是否为管理员
     * @param userId
     * @return
     */
    private boolean checkAdmin(String userId) {
        User adminUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId));
        if (Objects.isNull(adminUser)) {
            return false;
        } else if (adminUser.getUserRole().equals("SuperAdmin")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否登录了（检查token有效性）
     * @param response
     * @throws Exception
     */
    private void Refuse(HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setStatus(403);
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.getWriter().write("访问拒绝!!!◡ ヽ(`Д´)ﾉ ┻━┻，请先登录嗷！");
    }
}