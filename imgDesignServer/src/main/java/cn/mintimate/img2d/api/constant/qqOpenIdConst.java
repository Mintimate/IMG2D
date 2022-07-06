package cn.mintimate.img2d.api.constant;

/**
 * @author Mintimate
 */
public class qqOpenIdConst {
    public static final String QQ_REDIRECT_PATH="https://img2d.flyinbug.cn/login";
    //QQ互联中提供的 appid 和 appkey
    public static final String QQ_APPID = "";
    public static final String QQ_APPKEY = "";
    public static final String QQ_REDIRECT_URI = "/OAuth/qq";
    //QQ互联接口URL
    public static final String QQ_GET_TOKEN = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code";
    public static final String QQ_GET_OPENID_BY_TOKEN = "https://graph.qq.com/oauth2.0/me?access_token=";
    public static final String QQ_GET_USERINFO_BY_TOKEN = "https://graph.qq.com/user/get_user_info?access_token=";

}
