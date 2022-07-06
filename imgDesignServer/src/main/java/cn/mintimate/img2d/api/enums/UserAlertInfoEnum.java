package cn.mintimate.img2d.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户消息提醒枚举
 *
 * @author Mintimate
 */
@Getter
@AllArgsConstructor
public enum UserAlertInfoEnum {
    /**
     * 用户
     */
    USER("UserAccount", "用户提醒"),
    /**
     * 图片
     */
    IMG("IMGInFo", "图片提醒"),
    /**
     * 文章
     */
    ARTICLE("ArticleInFo", "文章提醒");
    /**
     * 类别
     */
    private final String type;

    /**
     * 中文描述
     */
    private final String chineseType;
}
