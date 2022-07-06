package cn.mintimate.img2d.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章实体类
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("img2d_article")
public class Article {
    /**
     * id（雪花ID，MyBatisPlus实现）
     */
    @TableId
    private String id;
    /**
     * 作者ID
     */
    private String userId;
    /**
     * 分类ID
     */
    private String categoryId;
    /**
     * 文章首页图片地址
     */
    private String articleBanner;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章梗概
     */
    private String articleDesc;
    /**
     * 文章内容（Markdown）
     */
    private String articleContent;
    /**
     * 文章类型（原创、转载）
     */
    private String type;
    /**
     * 文章来源
     */
    private String originalUrl;
    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 是否逻辑删除
     */
    private Integer isDelete;
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
}
