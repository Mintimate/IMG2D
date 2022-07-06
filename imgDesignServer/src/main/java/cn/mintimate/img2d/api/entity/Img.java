package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 图片实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("img2d_img")
public class Img {
    /**
     * ID（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private Long id;
    /**
     * 分类ID
     */
    private String cateId;
    /**
     * 上传用户ID
     */
    private String userId;
    /**
     * 图片名称
     */
    private String imgName;
    /**
     * 原图大小
     */
    private double imgSize;
    /**
     * 缩略图地址
     */
    private String imgThumbnailPath;
    /**
     * 图床地址
     */
    private String imgHostPath;
    /**
     * 原图地址
     */
    private String imgPath;
    /**
     * 感知Hash
     */
    private String perceptualHash;
    /**
     * 图片积分价格
     */
    private double imgPrice;
    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标识
     */
    private int delFlag;
    /**
     * 是否审核
     */
    private int isAudit;
}
