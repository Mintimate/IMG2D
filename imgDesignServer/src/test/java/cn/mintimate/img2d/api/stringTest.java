package cn.mintimate.img2d.api;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;

public class stringTest {
    public static void main(String args[]){
        boolean empty = StringUtils.isBlank(null);//注意这里是false
        System.out.println(empty);
    }
}
