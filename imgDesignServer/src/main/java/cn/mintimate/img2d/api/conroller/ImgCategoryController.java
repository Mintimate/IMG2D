package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ImgCategoryService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.entity.ImgCategory;
import cn.mintimate.img2d.api.dto.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片分类控制器
 *
 * @author Mintimate
 */
@Controller
@ResponseBody
@RequestMapping("/imgCate")
public class ImgCategoryController {
    @Resource
    ImgCategoryService imgCategoryService;

    @RequiredPermission
    @GetMapping("/getAllCategory")
    public Result getAllCategory() {
        return Result.ok(imgCategoryService.getAllCateNameAndID());
    }

    @RequiredPermission(admin = true)
    @GetMapping("/getCateDetail")
    public Result getCateDetail(){
        return Result.ok(imgCategoryService.getAllCateForAdmin());
    }

    @RequiredPermission(admin = true)
    @PostMapping(
            value = "/addImgCate",
            produces = {"application/json;charset=UTF-8"}
    )
    public Result addImgCate(@RequestBody ImgCategory IMG_Cate, HttpServletRequest request) {
        if (IMG_Cate.getCateName() == null || IMG_Cate.getCateName().equals("")) {
            return Result.fail("非法请求");
        }
        IMG_Cate.setCreateBy((String) request.getAttribute("userName"));
        if (imgCategoryService.save(IMG_Cate)) {
            return Result.ok();
        } else {
            return Result.fail("非法请求");
        }
    }
}
