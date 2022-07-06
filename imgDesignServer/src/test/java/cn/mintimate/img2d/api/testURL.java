package cn.mintimate.img2d.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class testURL {
    public static void main(String[] args) {
        try {
            System.out.println(URLEncoder.encode("https://api.img2d.flyinbug.cn","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
