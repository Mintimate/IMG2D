package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.ArticleCategoryService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.entity.ArticleCategory;
import cn.mintimate.img2d.api.vo.ArticleCategoryVO;
import cn.mintimate.img2d.api.dto.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 文章分类模块控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/articleCategory")
public class ArticleCategoryController {
    @Resource
    private ArticleCategoryService articleCategoryService;

    /**
     * 普通用户获取分类数据
     * @return 分类基本数据
     */
    @GetMapping("/getCategoryList")
    public Result<List<ArticleCategory>> listCategory(){
        return Result.ok(articleCategoryService.list());
    }

    /**
     * 管理员用户添加分类数据
     * @param articleCategoryVO 分类视图对象
     * @return 是否成功
     */
    @PostMapping("/addCategory")
    @RequiredPermission(admin = true)
    public Result<?> addCategory(@Valid @RequestBody ArticleCategoryVO articleCategoryVO){
        articleCategoryService.saveOrUpdateCategory(articleCategoryVO);
        return Result.ok();
    }
}
