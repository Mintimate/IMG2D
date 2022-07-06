package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分类对象DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CateDTO {
    /**
     * 分类ID （雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 分类名称
     */
    private String cateName;
    /**
     * 创建用户
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
}
