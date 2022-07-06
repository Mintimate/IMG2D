package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ProcessIMGService;
import cn.mintimate.img2d.api.dto.ProcessIMGDTO;
import cn.mintimate.img2d.api.vo.ProcessIMGVO;
import cn.mintimate.img2d.api.dto.Result;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;

/**
 * 在线处理图片控制器
 *
 * @author Mintimate
 */
@Controller
@RequestMapping("/imgOLP")
@ResponseBody
public class ImgOnlineToolController {
    @Resource
    private ProcessIMGService processIMGService;

    /**
     * 在线处理图片
     * @param processIMGVO 图片处理VO对象
     * @return 处理好的图片DTO对象
     */
    @PostMapping("/uploadImage")
    public Result<ProcessIMGDTO> uploadImage(ProcessIMGVO processIMGVO) {
        ProcessIMGDTO processIMGDTO = ProcessIMGDTO.builder()
                .imageName(processIMGVO.getFile().getOriginalFilename().substring(0, processIMGVO.getFile().getOriginalFilename().lastIndexOf(".")) + "."
                        + processIMGVO.getTargetFormat().toLowerCase())
                .originalPathName(IdWorker.getId()+processIMGVO.getFile().getOriginalFilename())
                .build();
        // 保存原图，返回原图地址；包装为原图File
        // (如果目标格式不是.jpg、.jpeg、.png，则保存为.jpg）
        File originalImage = new File(processIMGService.saveOriginalImage(processIMGVO.getFile(), processIMGDTO.getOriginalPathName()));

        // 判断模式,保存缩略图，返回缩略图名称
        if (processIMGVO.getTargetWorkMode().equals("ratio")) {
            // 等比例
            String setOutPutPathName= processIMGService.saveThumbnailImageByScale(originalImage, processIMGVO);
            if (setOutPutPathName!=null) {
                processIMGDTO.setOutPutPathName(processIMGService.saveThumbnailImageByScale(originalImage, processIMGVO));
            }
            else {
                return Result.fail("图片超清晰失败：原图已经够清晰了喵");
            }
        } else {
            // 固定尺寸
            processIMGDTO.setOutPutPathName(processIMGService.saveThumbnailImageBySize(originalImage, processIMGVO));
        }
        processIMGDTO.setImgType(processIMGService.judgeImageType(originalImage.getName()));

        return Result.ok(processIMGDTO);
    }

}
