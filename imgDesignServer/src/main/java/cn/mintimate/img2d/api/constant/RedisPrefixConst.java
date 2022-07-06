package cn.mintimate.img2d.api.constant;

/**
 * Desc redis常量
 *
 * @author Mintimate
 */
public class RedisPrefixConst {
    /**
     * 用户注册时，临时数据存储
     */
    public static final String IMG2D_USER_REGISTER_TEMP = "img2d_user_register_temp:";
    /**
     * 用户注册时，临时数据有效期：1800S
     */
    public static final long IMG2D_USER_REGISTER_EXP = 1800;
    /**
     * 用户上次登录时候的IP
     */
    public static final String IMG2D_USER_LAST_LOGIN_IP="img2d_user_last_login_ip:";
    /**
     * 用户点赞评论
     */
    public static final String IMG2D_COMMENT_USER_LIKE = "img2d_comment_user_like:";
    /**
     * 用户点赞图片数据表
     */
    public static final String IMG2D_IMG_USER_LIKE="img2d_img_user_like:";
    /**
     * 图片点赞计数
     */
    public static final String IMG2D_IMG_LIKE_COUNT="img2d_img_like_count";
    /**
     * 用户收藏图片数据表
     */
    public static final String IMG2D_IMG_USER_COLLECT="img2d_img_user_collect:";
    /**
     * 图片点赞计数
     */
    public static final String IMG2D_IMG_COLLECT_COUNT="img2d_img_collect_count";
    /**
     * 用户被关注统计
     */
    public static final String IMG2D_USER_BE_FOLLOWED_COUNT="img2d_user_be_followed_count";
    /**
     * 用户被关注列表
     */
    public static final String IMG2D_USER_BE_FOLLOWED_LIST="img2d_user_follow_list:be_follow:";
    /**
     * 用户关注列表
     */
    public static final String IMG2D_USER_FOLLOW_LIST="img2d_user_follow_list:be_followed:";
    /**
     *
     * 用户订单号状态
     */
    public static final String IMG2D_ALIPAY_ORDER="img2d_trade_no";
    /**
     * 用户认证
     */
    public static final String IMG2D_USER_IDENT_LIST="img2d_user_ident_list";
    /**
     * 系统首页头图标题
     */
    public static final String IMG2D_SYSTEM_BANNER_LIST="img2d_system_banner_list";
}
