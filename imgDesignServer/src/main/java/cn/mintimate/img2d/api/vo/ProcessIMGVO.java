package cn.mintimate.img2d.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 在线处理图片VO
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessIMGVO {
    /**
     * 原图
     */
    private MultipartFile file;
    /**
     * 选择输出格式
     */
    private String targetFormat;
    /**
     * 输出图片模式
     */
    private String targetWorkMode;
    /**
     * 选择输出图片压缩率
     */
    private float quality;
    /**
     * 输出图片的大小
     */
    private float proportion;
}
