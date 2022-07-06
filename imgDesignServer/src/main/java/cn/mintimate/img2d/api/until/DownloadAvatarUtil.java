package cn.mintimate.img2d.api.until;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 根据邮箱下载头像
 *
 * @author Mintimate
 */
public class DownloadAvatarUtil {
    static final String VIRTUAL_FOLDER_DIR="/SystemData/UserData/Avatar/";
    static final String REAL_FOLDER_DIR=System.getProperty("user.dir")+VIRTUAL_FOLDER_DIR;
    public static String downloadUsingNIO(String urlStr, String fileName) throws IOException {
        fileName=fileName+".jpeg";
        File file=new File(REAL_FOLDER_DIR);
        if (!file.exists()){
            file.mkdirs();
        }
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(REAL_FOLDER_DIR+fileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        return VIRTUAL_FOLDER_DIR+fileName;
    }
}
