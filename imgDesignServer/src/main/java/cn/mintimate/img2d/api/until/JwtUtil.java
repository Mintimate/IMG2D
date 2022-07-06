package cn.mintimate.img2d.api.until;

import cn.mintimate.img2d.api.Service.UserAlertInfoService;
import cn.mintimate.img2d.api.enums.UserAlertInfoEnum;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_USER_LAST_LOGIN_IP;

/**
 * JWT获取、解析工具
 *
 * @author Mintimate
 */
@Component
public class JwtUtil {
    @Resource
    private UserAlertInfoService userAlertInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private HttpServletRequest request;

    // 维护一个本类的静态变量(用于激活自动注入）
    public static JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.userAlertInfoService = this.userAlertInfoService;
        jwtUtil.redisUtil=this.redisUtil;
        jwtUtil.request=this.request;
    }
    // 三天
    private static long time=1000*60*60*24*3;
    //signature
    private static String signature="imgDesign2D";
    public static String creatToken(long id,String username){
        JwtBuilder jwtBuilder= Jwts.builder();
        String jwtToken=jwtBuilder
                //header
                .setHeaderParam("type","JWt")
                .setHeaderParam("alg","Hs256")
                //payload
                .claim("id",id)
                .claim("username",username)
                .setSubject("UserInfo")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        Object lastIP= jwtUtil.redisUtil.get(IMG2D_USER_LAST_LOGIN_IP+id);
        String nowIP= IPUtil.getIpAddr(jwtUtil.request);
        if (Objects.nonNull(lastIP)){
            String lastZone=IPUtil.getIpZone(String.valueOf(lastIP));
            if (!lastZone.equals(IPUtil.getIpZone(nowIP))){
                jwtUtil.userAlertInfoService.saveMessageAlert(
                        String.valueOf(id),
                        UserAlertInfoEnum.USER.getType(),
                        AlertInfoDemoUtil.User_Remote_login(String.valueOf(lastIP),nowIP));
            }
        }
        jwtUtil.redisUtil.set(IMG2D_USER_LAST_LOGIN_IP+id,nowIP);
        return jwtToken;
    }
    public static Object[] checkToken(String token){
        try {
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            if (claims.getExpiration().before(new Date(System.currentTimeMillis())))
                return null;
            Object[] objects=new Object[2];
            objects[0]=claims.get("id");
            objects[1]=claims.get("username");
            return objects;
        }
        catch (Exception e){
            return null;
        }
    }
}
