package cn.mintimate.img2d.api.until;

import cn.mintimate.img2d.api.filter.ThumbnailsImgFilter;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tiia.v20190529.TiiaClient;
import com.tencentcloudapi.tiia.v20190529.models.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.mintimate.img2d.api.constant.ImageSavePathConst.*;
import static cn.mintimate.img2d.api.constant.tencentAPIConst.smsSecretId;
import static cn.mintimate.img2d.api.constant.tencentAPIConst.smsSecretKey;

/**
 * 腾讯数据万象工具
 *
 * @author Mintimate
 */
public class TencentImageUtil {
    private static Credential cred = new Credential(smsSecretId, smsSecretKey);
    public static List TencentOCR(File file) {
        List<String> list =new ArrayList<>();
        try {
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tiia.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            TiiaClient client = new TiiaClient(cred, "ap-shanghai", clientProfile);

            DetectLabelRequest req = new DetectLabelRequest();

            req.setImageBase64(Base64ImgUtil.GenerateImageToBase64(file));
            DetectLabelResponse resp = client.DetectLabel(req);
            for (DetectLabelItem item : resp.getLabels()){
                list.add(item.getName());
            }
        } catch (TencentCloudSDKException e) {
            list.add("图片过于复杂");
        }
        return list;
    }

    public static String EnhanceImage(File originalImage,File outPutImage) {
        String ImageName = originalImage.getName();
        String outputType=outPutImage.getName().substring(outPutImage.getName().lastIndexOf("."));
        String ImagTemp = PROCESS_IMG_ONLINE_TEMP + ImageName;
        try {
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tiia.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            TiiaClient client = new TiiaClient(cred, "ap-shanghai", clientProfile);
            EnhanceImageRequest req = new EnhanceImageRequest();
            req.setImageBase64(Base64ImgUtil.GenerateImageToBase64(originalImage));
            EnhanceImageResponse resp = client.EnhanceImage(req);
            Base64ImgUtil.GenerateIMG(resp.getEnhancedImage(), ImagTemp);
            if(!Arrays.asList(".jpeg",".jpg",".png").contains(outputType)){
                try {
                    File thumbnailImage = new File(ImagTemp);
                    // 判断路径是否存在，如果不存在则创建
                    if (!thumbnailImage.getParentFile().exists()) {
                        thumbnailImage.getParentFile().mkdirs();
                    }
                    Thumbnails.of(ImagTemp).scale(1.0)
                            .addFilter(new ThumbnailsImgFilter())
                            .outputFormat(FilenameUtils.getExtension(outPutImage.getName()))
                            .outputQuality(1)
                            .toFile(outPutImage);
                    return outPutImage.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
        return ImagTemp;
    }
}
