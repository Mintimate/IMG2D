package cn.mintimate.img2d.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 腾讯云短信模版枚举
 * @author Mintimate
 */
@Getter
@AllArgsConstructor
public enum SmsTemplateEnum {
    /**
     * 短信登录
     */
    LOGIN("1320424","短信登录模版"),
    /**
     * 绑定手机号
     */
    BINDING("1264877","绑定手机号"),
    /**
     * 使用手机号注册
     */
    REGISTER("1198709","使用手机号注册");
    private final String TemplateID;
    private final String TemplateDesc;
}
