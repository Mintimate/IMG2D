package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ProcessIMGService;
import cn.mintimate.img2d.api.filter.ThumbnailsImgFilter;
import cn.mintimate.img2d.api.until.TencentImageUtil;
import cn.mintimate.img2d.api.vo.ProcessIMGVO;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.mintimate.img2d.api.constant.ImageSavePathConst.*;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class ProcessIMGServiceImpl implements ProcessIMGService {
    @Override
    public String saveOriginalImage(MultipartFile file, String OriginalImageName) {
        mkdirFolder();
        String originalType = OriginalImageName.substring(OriginalImageName.lastIndexOf("."));
        if (!Arrays.asList(".jpeg", ".jpg", ".png").contains(originalType)) {
            String ImageOutPut = PROCESS_IMG_ONLINE_ORIGINAL + OriginalImageName.substring(0, OriginalImageName.lastIndexOf(".")) + ".jpg";
            try {
                Thumbnails.of(file.getInputStream()).scale(1.0)
                        .addFilter(new ThumbnailsImgFilter())
                        .outputFormat("jpg")
                        .outputQuality(0.5)
                        .toFile(ImageOutPut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ImageOutPut;
        } else {
            String OriginalImagePath = PROCESS_IMG_ONLINE_ORIGINAL + OriginalImageName;
            File OriginalImage = new File(OriginalImagePath);
            // 判断路径是否存在，如果不存在则创建
            if (!OriginalImage.getParentFile().exists()) {
                OriginalImage.getParentFile().mkdirs();
            }
            try {
                // 保存到服务器中
                file.transferTo(OriginalImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return OriginalImagePath;
        }
    }

    @Override
    public String saveThumbnailImageByScale(File originalImage, ProcessIMGVO processIMGVO) {
        // 生成新图后缀
        String thumbnailImagePath = PROCESS_IMG_ONLINE_OUTPUT
                + originalImage.getName().substring(0, originalImage.getName().lastIndexOf(".")) + "."
                + processIMGVO.getTargetFormat().toLowerCase();
        // 如果目标图片质量大于1.0f，且图片大小大于4M=>拒绝图片增强
        if (processIMGVO.getQuality() > 1.0f) {
            if (originalImage.length() >= 1024 * 1024 * 4) {
                return null;
            }
            // 对原图进行增强并替换为原图
            File thumbnailImage = new File(TencentImageUtil.EnhanceImage(originalImage, new File(thumbnailImagePath)));
            return thumbnailImage.getName();
        }
        File thumbnailImage = new File(thumbnailImagePath);
        // 判断路径是否存在，如果不存在则创建
        if (!thumbnailImage.getParentFile().exists()) {
            thumbnailImage.getParentFile().mkdirs();
        }
        try {
            // 转换
            Thumbnails.of(originalImage).scale(processIMGVO.getProportion())
                    .imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
                    .addFilter(new ThumbnailsImgFilter())
                    .outputFormat(processIMGVO.getTargetFormat().toLowerCase())
                    .outputQuality(processIMGVO.getQuality())
                    .toFile(thumbnailImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PrintLog.Write("压缩后大小："+thumbnailImage.length()+"\n");
        return thumbnailImage.getName();
    }

    @Override
    public String saveThumbnailImageBySize(File originalImage, ProcessIMGVO processIMGVO) {
        int width = 0;
        int hight = 0;
        try {
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(originalImage));
            if (sourceImg.getWidth() >= sourceImg.getHeight()) {
                hight = width = (int) (processIMGVO.getProportion() * sourceImg.getHeight());
            } else {
                hight = width = (int) (processIMGVO.getProportion() * sourceImg.getWidth());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String thumbnailImageName = originalImage.getName();
        String thumbnailImagePath;
        // 生成新图后缀
        thumbnailImagePath = PROCESS_IMG_ONLINE_OUTPUT
                + thumbnailImageName.substring(0, thumbnailImageName.lastIndexOf(".")) + "." + processIMGVO.getTargetFormat().toLowerCase();
        File thumbnailImage = new File(thumbnailImagePath);
        // 判断路径是否存在，如果不存在则创建
        if (!thumbnailImage.getParentFile().exists()) {
            thumbnailImage.getParentFile().mkdirs();
        }
        if (processIMGVO.getQuality() > 1.0f) {
            if (originalImage.length() >= 1024 * 1024 * 4) {
                return null;
            }
            originalImage = new File(TencentImageUtil.EnhanceImage(originalImage, thumbnailImage));
            processIMGVO.setQuality(1.0f);
            if (originalImage == null) {
                return null;
            }
        }
        try {
            Thumbnails.of(originalImage)
                    .sourceRegion(Positions.CENTER, width, hight)
                    .scale(1.0)
                    .imageType(ThumbnailParameter.DEFAULT_IMAGE_TYPE)
                    .addFilter(new ThumbnailsImgFilter())
                    .outputFormat(processIMGVO.getTargetFormat().toLowerCase())
                    .outputQuality(processIMGVO.getQuality())
                    .toFile(thumbnailImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PrintLog.Write("压缩后大小："+thumbnailImage.length()+"\n");
        return thumbnailImage.getName();
    }

    @Override
    public List<String> judgeImageType(String imagePath) {
        List list = new ArrayList();
        File file = new File(PROCESS_IMG_ONLINE_ORIGINAL + imagePath);
        if (file.length() >= 1024 * 1024 * 4) {
            list.add("图片过于复杂");
            return list;
        }
        for (Object type : TencentImageUtil.TencentOCR(file)) {
            list.add(type);
        }
        if (list.size() == 0) {
            list.add("图太复杂，识别失败( ；´Д｀)");
        }
        return list;
    }

    private void mkdirFolder(){
        String[] WORK_PATH=new String[]{PROCESS_IMG_ONLINE_ORIGINAL,PROCESS_IMG_ONLINE_TEMP,PROCESS_IMG_ONLINE_OUTPUT};
        for (String item :WORK_PATH) {
            if (!new File(item).exists()) {
                new File(item).mkdirs();
            }
        }
    }
}
