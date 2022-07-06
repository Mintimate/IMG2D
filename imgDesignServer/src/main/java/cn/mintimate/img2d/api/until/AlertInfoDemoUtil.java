package cn.mintimate.img2d.api.until;

/**
 * 提醒模版工具包
 *
 * @author Mintimate
 */
public class AlertInfoDemoUtil {
    /**
     * 用户远程登录消息提示模版
     * @param BeforeIP 上次登录IP
     * @param currentIP 本次登录IP
     * @return 模版加工后语句
     */
    public static String User_Remote_login(String BeforeIP, String currentIP) {
        return String.format("Hello～～你上次的登录IP为%s，这次的登录IP为%s，两次IP归属地不一样，请注意保护账号信息哦。", BeforeIP, currentIP);
    }
    /**
     * 用户修改密码消息提示模版
     * @param IpZone IP所在地域
     * @return 模版加工后语句
     */
    public static String User_Remote_Change_Password(String IpZone) {
        return String.format("注意哦，您在%s修改密码成功，请注意保护账号信息哦。", IpZone);
    }
}
