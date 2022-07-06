package cn.mintimate.img2d.api.until;

import java.util.Random;

/**
 * 验证码获取
 *
 * @author Mintimate
 */
public class VerifyCodeUntil {
    private final static String sources = "5678012349"; // 验证码可能出现的字符

    /**
     * 生成短信验证码
     * @param longSize
     * @return
     */
    public static String getVerifyCodes(int longSize){
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return String.valueOf(flag);
    }
}
