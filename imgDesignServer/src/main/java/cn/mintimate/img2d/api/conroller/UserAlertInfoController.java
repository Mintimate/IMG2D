package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.UserAlertInfoService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.UserAlertInfoDTO;
import cn.mintimate.img2d.api.dto.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户消息提醒控制器
 *
 * @author Mintimate
 */
@RestController
@RequestMapping("/userAlertInfo")
@RequiredPermission
public class UserAlertInfoController {
    @Resource
    private UserAlertInfoService userAlertInfoService;


    /**
     * 获取用户未读消息统计
     * @return
     */
    @GetMapping("/getHavenReadInfo")
    public Result<Integer> getHavenReadInfo(){
        return Result.ok(userAlertInfoService.getHavenReadInfo());
    }

    /**
     * 获取用户消息列表
     *
     * @return 提醒消息列表
     */
    @GetMapping("/getPersonAlertInfo")
    public Result<List<UserAlertInfoDTO>> getPersonAlertInfo() {
        return Result.ok(userAlertInfoService.getUserMessage());
    }

    /**
     * 用户已读AlertInfo
     *
     * @param userAlertInfoId
     * @return
     */
    @PostMapping("/haveRead")
    public Result checkRead(@RequestBody String userAlertInfoId) {
        return Result.ok(userAlertInfoService.updateHasRead(userAlertInfoId));
    }

    /**
     * 用户删除AlertInfo
     *
     * @param userAlertInfoId
     * @return
     */
    @PostMapping("/deleteInfo")
    public Result deleteInfo(@RequestBody String userAlertInfoId) {
        return Result.ok(userAlertInfoService.updateDelete(userAlertInfoId));
    }

}
