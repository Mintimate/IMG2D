package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ImgTagService;
import cn.mintimate.img2d.api.dto.WaterFallIMG_DTO;
import cn.mintimate.img2d.api.entity.ImgTag;
import cn.mintimate.img2d.api.until.PageResultUntil;
import cn.mintimate.img2d.api.dto.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 图片标签控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/imgTag")
public class ImgTagController {
    @Resource
    private ImgTagService imgTagService;

    /**
     * 获取全部标签
     * @return
     */
    @GetMapping("/getAllTagNames")
    public Result<List<String>> getAllTagNames() {
        return Result.ok(imgTagService.list().stream().map(ImgTag::getTagName).collect(Collectors.toList()));
    }

    /**
     * 通过标签获取图片
     * @param tagNames 标签名列表
     * @param currentPage 第几页
     * @return 瀑布流图片DTO对象
     */
    @PostMapping({"/getImagesByTagNames/{page}", "/getImagesByTagNames"})
    public Result<PageResultUntil<WaterFallIMG_DTO>> getImagesByTagNames(
            @RequestBody String[] tagNames,
            @PathVariable(value = "page", required = false) Integer currentPage) {
        if (Objects.isNull(currentPage)){
            currentPage=1;
        }
        return Result.ok(imgTagService.getImgByPage(currentPage, Arrays.asList(tagNames)));
    }
}
