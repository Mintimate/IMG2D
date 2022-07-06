package cn.mintimate.img2d.api.until;

import cn.mintimate.img2d.api.constant.qqOpenIdConst;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * QQ工具类（主要用于解析QQ返回的信息）
 *
 * @author Mintimate
 */
public class QQ_OAuthUntil {

    private static JSONObject parseJSONP(String jsonp) {
        // 二次分装对象为JSON
        int startIndex = jsonp.indexOf("(");
        int endIndex = jsonp.lastIndexOf(")");
        String json = jsonp.substring(startIndex + 1, endIndex);
        return JSONObject.parseObject(json);
    }

    //QQ返回信息：access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
    public static String getAccessToken(String code) throws IOException {
        String parameter = "&client_id=" + qqOpenIdConst.QQ_APPID +
                "&client_secret=" + qqOpenIdConst.QQ_APPKEY +
                "&code=" + code +
                "&redirect_uri=" + qqOpenIdConst.QQ_REDIRECT_URI;
        CloseableHttpClient client = HttpClients.createDefault();
        String token = null;
        HttpGet httpGet = new HttpGet(qqOpenIdConst.QQ_GET_TOKEN + parameter);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            if (result.indexOf("access_token") >= 0) {
                String[] array = result.split("&");
                for (String str : array) {
                    if (str.indexOf("access_token") >= 0) {
                        token = str.substring(str.indexOf("=") + 1);
                        break;
                    }
                }
            }
        }
        httpGet.releaseConnection();
        return token;
    }

    // qq返回信息：callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
    // 需要用到上面自己定义的解析方法parseJSONP
    public static String getOpenID(String parameter) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(qqOpenIdConst.QQ_GET_OPENID_BY_TOKEN + parameter);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = parseJSONP(result);
        }
        httpGet.releaseConnection();
        if (jsonObject != null) {
            return jsonObject.getString("openid");
        } else {
            return null;
        }
    }

    public static String getOpenIDByCode(String code) throws IOException {
        return getOpenID(getAccessToken(code));
    }

    // qq返回信息：{ "ret":0, "msg":"", "nickname":"YOUR_NICK_NAME", ... }，为JSON格式
    // 直接使用JSONObject对象解析
    public static JSONObject getUserInfo(String access_token,String openid) throws IOException {
        String URL=qqOpenIdConst.QQ_GET_USERINFO_BY_TOKEN+
                access_token +
                "&oauth_consumer_key=" +
                qqOpenIdConst.QQ_APPID +
                "&openid=" +
                openid;
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            // JSON解析
            jsonObject = JSONObject.parseObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
}
