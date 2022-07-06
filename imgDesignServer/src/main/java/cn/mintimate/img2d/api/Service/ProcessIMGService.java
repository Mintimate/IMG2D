package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.vo.ProcessIMGVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface ProcessIMGService {
    // 保存原图
    String saveOriginalImage(MultipartFile file, String OriginalImageName);
    // 保存缩略图（等比例）
    String saveThumbnailImageByScale(File originalImage, ProcessIMGVO processIMGVO);
    // 保存缩略图（固定大小）
    String saveThumbnailImageBySize(File originalImage, ProcessIMGVO processIMGVO);
    // 使用API接口，判断图片分类
    List<String> judgeImageType(String imagePath);
}
