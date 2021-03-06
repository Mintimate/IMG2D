package cn.mintimate.img2d.api.until;

import com.github.jarod.qqwry.IPZone;
import com.github.jarod.qqwry.QQWry;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 根据Request请求获取IP
 *
 * @author Mintimate
 */
public class IPUtil {

    /**
     * 根据Request请求获取IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        return ipAddress;
    }

    /**
     * 根据Request请求获取IP所在地区
     * @param request
     * @return
     */
    public static String getIpZone(HttpServletRequest request){
        try {
            QQWry qqwry = new QQWry();
            IPZone ipzone = qqwry.findIP(getIpAddr(request));
            return ipzone.getMainInfo();
        } catch (IOException e) {
            e.printStackTrace();
            return "地球";
        }
    }

    public static String getIpZone(String ip){
        try {
            QQWry qqwry = new QQWry();
            IPZone ipzone = qqwry.findIP(ip);
            return ipzone.getMainInfo();
        } catch (IOException e) {
            e.printStackTrace();
            return "地球";
        }
    }
}