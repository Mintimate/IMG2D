package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页头图
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemctlBannerIndexDTO {
    /**
     * 优先级别
     */
    private String topId;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 点击跳转地址
     */
    private String url;
    /**
     * 主标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subtitle;
}
