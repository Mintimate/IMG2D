package cn.mintimate.img2d.api.constant;

/**
 * Desc 图片存储地址
 *
 * @author Mintimate
 */
public class ImageSavePathConst {
    /**
     * 所有图片保存的顶级目录
     */
    private static String VIRTUAL_TOP_FOLDER_DIR = System.getProperty("user.dir");
    /**
     * 在线处理图片：原图目录
     */
    public static String PROCESS_IMG_ONLINE_ORIGINAL=VIRTUAL_TOP_FOLDER_DIR+"/SystemData/ProcessOLD/Original/";
    /**
     * 在线处理图片：中间目录
     */
    public static String PROCESS_IMG_ONLINE_TEMP=VIRTUAL_TOP_FOLDER_DIR+"/SystemData/ProcessOLD/Temp/";
    /**
     * 在线处理图片：输出目录
     */
    public static String PROCESS_IMG_ONLINE_OUTPUT=VIRTUAL_TOP_FOLDER_DIR+"/SystemData/ProcessOLD/Output/";
    /**
     * 缩略图内数据库存放
     */
    public static String THUMBNAIL_SQL_PATH="/SystemData/IMGData/Thumbnail/";
    /**
     * 缩略图内数据库存放
     */
    public static String ORIGINAL_SQL_PATH="/SystemData/IMGData/Original/";
    /**
     * 缩略图内数据库存放
     */
    public static String HOST_SQL_PATH="/SystemData/IMGData/ImageHost/";
    /**
     * 缩略图保存目录
     */
    public static String THUMBNAIL_SAVE_PATH = VIRTUAL_TOP_FOLDER_DIR + THUMBNAIL_SQL_PATH;
    /**
     * 原图保存目录
     */
    public static String ORIGINAL_SAVE_PATH = VIRTUAL_TOP_FOLDER_DIR+ORIGINAL_SQL_PATH;
    /**
     * 图床保存目录
     */
    public static String HOST_SAVE_PATH = VIRTUAL_TOP_FOLDER_DIR+HOST_SQL_PATH;
}
