package cn.mintimate.img2d.api.until;

import java.io.*;
import java.util.Base64;

public class Base64ImgUtil {
    /**
     * 用户头像保存
     * @param imgStr 用户头像（Base64）
     * @param savePath 保存地址
     * @return 是否成功
     */
    public static boolean GenerateIMG(String imgStr, String savePath) {
        if (imgStr == null) // 图像数据为空
            return false;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // Base64解码
            byte[] b = decoder.decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = savePath;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 用户头像保存
     * @param imgStr 用户头像（Base64）
     * @param UserName 用户昵称
     * @return 是否成功
     */
    public static boolean GenerateUserAvatar(String imgStr, String UserName) {
        if (imgStr == null) // 图像数据为空
            return false;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // Base64解码
            byte[] b = decoder.decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = System.getProperty("user.dir") + "/SystemData/UserData/Avatar/"+UserName+".jpeg";
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将图片转为Base64并返回
     * @param imgPath 原图地址
     * @return Base64
     */
    public static String  GenerateImageToBase64(File imgPath){
        Base64.Encoder encoder=Base64.getEncoder();
        String base64 = null;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return encoder.encodeToString(data);
    }
}
