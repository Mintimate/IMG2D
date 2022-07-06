package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.UserService;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.model.LocalStorage;
import cn.mintimate.img2d.api.until.JwtUtil;
import cn.mintimate.img2d.api.until.QQ_OAuthUntil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 外置登录控制器
 *
 * @author Mintimate
 */
@RestController
@Log4j2
@RequestMapping("/OAuth")
public class OAuthController {
    @Resource
    private UserService userService;
    @Resource
    private HttpServletRequest request;

    /**
     * 判断state是否合法
     * @param state
     * @return
     */
    private boolean judgeSate(String state){
        long now_state = System.currentTimeMillis();
        if (now_state > Long.parseLong(state) + 1000 * 60 || now_state < Long.parseLong(state)) {
            log.error("QQ State错误");
            return false;
        }
        return true;
    }

    /**
     * QQ登录回调
     * 是要与QQ互联填写的回调路径一致(我这里因为前端请求愿意不用写成  api/qq/callback)
     *
     * @return 登录视图对象重定向
     */
    @GetMapping("/qq")
    public RedirectView Login_By_QQ_Callback() throws Exception {
        if (!judgeSate(request.getParameter("state"))){
            return new RedirectView(QQ_OAuthUntil.REDIRECT_PATH + "?state=fail");
        }
        //qq返回的信息：http://graph.qq.com/demo/index.jsp?code=9A5F************************06AF&state=test
        String openid = QQ_OAuthUntil.getOpenIDByCode(request.getParameter("code"));
        log.info(openid);
        String fullURL = QQ_OAuthUntil.REDIRECT_PATH + "?state=fail";
        if (userService.QQOpenIdSomeoneHave(openid)) {
            User user = userService.getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUserQqOpenid, openid));
            fullURL = QQ_OAuthUntil.REDIRECT_PATH + "?state=success&user=" + user.getUsername() +
                    "&role=" + user.getUserRole() +
                    "&userId="+user.getId()+
                    "&avatarPath=" + user.getUserAvatarPath() +
                    "&token=" + JwtUtil.creatToken(Long.parseLong(user.getId()), user.getUsername());
        }
        RedirectView redirectView = new RedirectView(fullURL);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;
    }

    /**
     * 用户绑定QQ
     * @param response
     * @return 存储localStorage
     * @throws Exception
     */
    @GetMapping("/bandingQQ")
    public String bandingQQ_Callback(HttpServletResponse response) throws Exception {
        //qq返回的信息：http://graph.qq.com/demo/index.jsp?code=9A5F************************06AF&state=test
        if (!judgeSate(request.getParameter("state"))){
            return  "<script>" +
                    "window.opener.localStorage.setItem('QQ_ID', '" +
                    JSON.toJSONString(LocalStorage.builder()
                            .time(System.currentTimeMillis())
                            .expired(1000 * 60 * 60 * 24 * 2)
                            .data("A113")
                            .build()) + "');" +
                    "window.opener = null;" +
                    "window.open(\"\", \"_self\");" +
                    "window.close();" +
                    "</script>";
        }
        //获取回调后的 openid 值
        String openid = QQ_OAuthUntil.getOpenIDByCode(request.getParameter("code"));
        LocalStorage localStorage = LocalStorage.builder()
                .time(System.currentTimeMillis())
                .expired(1000 * 60 * 60 * 24 * 2)
                .build();
        log.info(openid);
        if (!userService.QQOpenIdSomeoneHave(openid)) {
            // 没有用户绑定该OpenId
            localStorage.setData(openid);
        }
        else {
            localStorage.setData("A113");
        }
        response.setContentType("text/html; charset=utf-8");  // 响应编码
        return "<script>" +
                "window.opener.localStorage.setItem('QQ_ID', '" + JSON.toJSONString(localStorage) + "');" +
                "window.opener = null;" +
                "window.open(\"\", \"_self\");" +
                "window.close();" +
                "</script>";
    }
}