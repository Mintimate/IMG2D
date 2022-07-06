package cn.mintimate.img2d.api.until;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingResponse;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static cn.mintimate.img2d.api.constant.tencentAPIConst.*;
import static com.qcloud.cos.demo.BucketRefererDemo.cosClient;

/**
 * 腾讯对象存储工具
 *
 * @author Mintimate
 */
public class TencentCOSUtil {

    /**
     * 创建COS客户端
     * @return
     */
    private static COSClient getCosClient(){
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(cosSecretId, cosSecretKey);
        // 2.1 设置 bucket 的地域
        Region region = new Region("ap-nanjing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 2.2 使用https协议传输
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

//    public static String judgeIMG() {
//        //1.创建任务请求对象
//        ImageAuditingRequest request = new ImageAuditingRequest();
//        //2.添加请求参数 参数详情请见api接口文档
//        //2.1设置请求bucket
//        request.setBucketName("image-test-1302972711");
//        //2.2设置审核类型
//        request.setDetectType("porn");
//        //2.3设置bucket中的图片位置
//        request.setObjectKey("imageHost/动物-鸟.jpg");
//        //3.调用接口,获取任务响应对象
//        ImageAuditingResponse response = getCosClient().imageAuditing(request);
//        return response.getPornInfo().getHitFlag();
//    }

    /**
     * 上传文件到COS内
     * @param imgFile
     * @param imgName
     * @return
     */
    public static String UploadIMG(MultipartFile imgFile, String imgName) {
        try {
            InputStream inputStream = imgFile.getInputStream();
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // - 使用输入流存储，需要设置请求长度
            objectMetadata.setContentLength(inputStream.available());
            // - 设置缓存
            objectMetadata.setCacheControl("no-cache");
            // - 设置Content-Type
            objectMetadata.setContentType(getcontentType(imgName.substring(imgName.lastIndexOf("."))));
            // 指定文件将要存放的存储桶
            // 指定文件上传到 COS 上的路径，即对象键。
            String key = "imageHost/" + imgName;
            PutObjectResult putResult = getCosClient().putObject(bucketName, key, inputStream, objectMetadata);
            // 成功存储后，返回图片MD5
            return putResult.getContentMd5();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 扩展名枚举JS类型
     *
     * @param FilenameExtension
     * @return
     */
    private static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        //PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }
}
