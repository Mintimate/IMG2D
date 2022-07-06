package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.UserIdentService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.Result;
import cn.mintimate.img2d.api.vo.UserIdentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户身份认证控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/userIdent")
public class UserIdentController {
    @Resource
    private UserIdentService userIdentService;

    /**
     * 获取所有认证申请
     * @return
     */
    @RequiredPermission(admin = true)
    @GetMapping("/getIdents")
    public Result<List<UserIdentVO>> getIdents(){
        return Result.ok(userIdentService.getAllList());
    }

    /**
     * 用户添加申请
     * @param userIdentVO
     * @return
     */
    @RequiredPermission
    @PostMapping("/addIdent")
    public Result addIndent(@RequestBody UserIdentVO userIdentVO){
        userIdentService.addToList(userIdentVO);
        return  Result.ok();
    }

    /**
     * 审核认证
     * @param userIdList
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/judgeIdent")
    public Result judgeIdent(@RequestBody List<UserIdentVO> userIdList){
        return Result.ok(userIdentService.passList(userIdList));
    }

    /**
     * 拒绝审核
     * @param userIdList
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/refuseIdent")
    public Result refuseIdent(@RequestBody List<UserIdentVO> userIdList){
        return Result.ok(userIdentService.refuseList(userIdList));
    }

}
