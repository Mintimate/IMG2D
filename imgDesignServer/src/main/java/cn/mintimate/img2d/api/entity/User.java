package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_user")
public class User {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private String id;
    /**
     * 用户邮箱信息
     */
    private String userEmail;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户电话
     */
    private String userTele;
    /**
     * 用户电话确认
     */
    private Boolean userTeleCheck;
    /**
     * 用户个性签名
     */
    private String userSignature;
    /**
     * 用户自定义主页
     */
    private String userSpace;
    /**
     * 用户角色
     */
    private String userRole;
    /**
     * 用户的Token
     */
    private String userToken;
    /**
     * 用户头像地址
     */
    private String userAvatarPath;
    /**
     * 用户QQ唯一识别码
     */
    private String userQqOpenid;
    /**
     * 是否封禁
     */
    private int isBan;
}
