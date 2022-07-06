package cn.mintimate.img2d.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户提醒表DTO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAlertInfoDTO {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    private String id;
    /**
     * 用户ID（雪花ID，MyBatisPlus实现）
     */
    private String userId;
    /**
     * Info的类别
     */
    private String type;
    /**
     * Info的信息
     */
    private String message;
    /**
     * 是否已读
     */
    private String hasRead;
    /**
     * 添加的日期
     */
    private Date createTime;
}
