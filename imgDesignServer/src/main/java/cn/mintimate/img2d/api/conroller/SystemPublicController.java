package cn.mintimate.img2d.api.conroller;

/**
 * @author Mintimate
 */

import cn.mintimate.img2d.api.Service.SystemctlService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.Result;
import cn.mintimate.img2d.api.vo.SystemctlBannerIndexVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统持久化控制器
 *
 * @author Mintimate
 */
@Controller
@RequestMapping("/systemctl")
@ResponseBody
public class SystemPublicController {
    @Resource
    private SystemctlService systemctlService;

    /**
     *  首页持久化头图
     *
     * @return 首页头图DTO
     */
    @GetMapping("/getBannerIndex")
    public Result getBannerIndex(){
        return Result.ok(systemctlService.getBannerIndex());
    }


    /**
     * 保存首页头图
     *
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/saveBannerIndex")
    public Result saveBannerIndex(@RequestBody List<SystemctlBannerIndexVO> systemctlBannerIndexVOList){
        systemctlService.saveBannerIndex(systemctlBannerIndexVOList);
        return Result.ok();
    }
}
