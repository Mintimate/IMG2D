package cn.mintimate.img2d.api.conroller;

import cn.mintimate.img2d.api.Service.UserService;
import cn.mintimate.img2d.api.annotation.RequiredPermission;
import cn.mintimate.img2d.api.dto.*;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.enums.AuditBanEnum;
import cn.mintimate.img2d.api.enums.SmsTemplateEnum;
import cn.mintimate.img2d.api.until.*;
import cn.mintimate.img2d.api.vo.AdminUserInfoVO;
import cn.mintimate.img2d.api.vo.RegisterUserVO;
import cn.mintimate.img2d.api.dto.Result;
import cn.mintimate.img2d.api.vo.UserLoginVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_USER_REGISTER_EXP;
import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_USER_REGISTER_TEMP;
import static cn.mintimate.img2d.api.constant.SystemConfigConst.SALT;
import static cn.mintimate.img2d.api.enums.StatusCodeEnum.USERNAME_NOT_EXIST;

/**
 * 用户基本信息控制器
 *
 * @author Mintimate
 */
@RestController
@Slf4j
@RequestMapping("/userCenter")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private EmailHelperUntil emailHelper;

    /**
     * 用户普通登录方法
     *
     * @param userLoginVO 用户登录基本信息
     * @return 用户基本登录信息
     */
    @PostMapping("/loginNormal")
    public Result<UserCoreDTO> login(@RequestBody UserLoginVO userLoginVO) {
        userLoginVO.setPassword(DigestUtils.md5DigestAsHex((SALT+userLoginVO.getPassword()).getBytes(StandardCharsets.UTF_8)));
        if (Objects.isNull(userLoginVO.getMode())) {
            userLoginVO.setMode("Email");
        }
        UserCoreDTO userCoreDTO = userService.login(userLoginVO);
        if (userCoreDTO.getId() == null) {
            return Result.fail("登录失败，对象不存在");
        }
        userCoreDTO.setUserToken(JwtUtil.creatToken(Long.parseLong(userCoreDTO.getId()), userCoreDTO.getUsername()));
        return Result.ok(userCoreDTO);
    }

    /**
     * 根据手机号，发送短信
     *
     * @param telephone 需要发送的手机号
     * @return
     */
    @GetMapping("/getVerifyCodesByTelephone")
    public Result getVerifyCodesByTelephone(
            @RequestParam(value = "telephone") String telephone) {
        User user = userService.getOne(new LambdaQueryWrapper<>(User.class)
                .eq(User::getUserTele, telephone));
        if (Objects.isNull(user)) {
            return Result.fail(USERNAME_NOT_EXIST);
        }
        if (userService.sendSMS(telephone, SmsTemplateEnum.LOGIN)) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 使用短信登录
     *
     * @param telephone   用户电话
     * @param verifyCodes 验证码
     * @return UserDTO
     */
    @PostMapping("/VerifyCodesByTelephone")
    public Result VerifyCodesByTelephone(
            @RequestParam(value = "telephone") String telephone,
            @RequestParam(value = "verifyCodes") String verifyCodes) {
        Object redisCodes = redisUtil.get(telephone + "_verifyCodes");
        if (redisCodes == null) {
            return Result.fail();
        }
        if (redisCodes.equals(verifyCodes)) {
            return Result.ok(userService.loginByTelephone(telephone));
        } else {
            return Result.fail();
        }
    }

    /**
     * 用户注册
     *
     * @param registerUserVO:用户注册信息
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserVO registerUserVO) {
        String newPasswd=DigestUtils.md5DigestAsHex((SALT+registerUserVO.getPassword()).getBytes(StandardCharsets.UTF_8));
        registerUserVO.setPassword(newPasswd);
        String uuid = UUID.randomUUID().toString();
        redisUtil.set(
                IMG2D_USER_REGISTER_TEMP + uuid,
                JSON.toJSON(registerUserVO),
                IMG2D_USER_REGISTER_EXP);
        emailHelper.sendSimpleMail(
                registerUserVO,
                Base64.getEncoder().encodeToString(uuid.getBytes(StandardCharsets.UTF_8)));
        return Result.ok();
    }

    /**
     * 用户邮件注册确认
     *
     * @param Base64Key
     * @return 是否成功
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/registerCheck/{Base64Key}")
    public Result registerCheck(@PathVariable(value = "Base64Key") String Base64Key) throws UnsupportedEncodingException {
        byte[] base64decodedBytes = Base64.getDecoder().decode(Base64Key);
        Object jsonStr = redisUtil.get(IMG2D_USER_REGISTER_TEMP + new String(base64decodedBytes, "utf-8"));
        if (jsonStr != null) {
            RegisterUserVO registerUserVO = JSON.parseObject(String.valueOf(jsonStr), RegisterUserVO.class);
            User user = User.builder()
                    .userEmail(registerUserVO.getUserEmail())
                    .username(registerUserVO.getUsername())
                    .password(registerUserVO.getPassword())
                    .userRole("RegisteredUser")
                    .build();
            String AvatarPath = "/SystemData/UserData/Avatar/Demo.png";
            try {
                String username = IdWorker.getId(user) + "-" + user.getUsername();
                AvatarPath = DownloadAvatarUtil.downloadUsingNIO("https://v1.alapi.cn/api/avatar?email=" + user.getUserEmail(), username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            redisUtil.del(IMG2D_USER_REGISTER_TEMP + new String(base64decodedBytes, "utf-8"));
            user.setUserAvatarPath(AvatarPath);
            userService.save(user);
            return Result.ok(RegisterUserVO.builder().username(user.getUsername()).userEmail(user.getUserEmail()).build());
        }
        return Result.fail("参数错误");
    }

    /**
     * 校验用户名、邮箱是否存在
     *
     * @param Username
     * @param Email
     * @return
     */
    @GetMapping("/CheckUser/{Username}/{Email}")
    public int checkUser(@PathVariable(value = "Username") String Username,
                         @PathVariable(value = "Email") String Email) {
        // Username已经存在：返回1；
        // Email已经存在：返回2；
        // 都存在：返回3；
        return userService.checkRepeat(Username, Email);
    }

    /**
     * 用户空间获取基本信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/getSpaceInfo/{userId}")
    public Result<UserSpaceDTO> getUserSpaceBase(@PathVariable(value = "userId") String userId) {
        return Result.ok(BeanCopyUtil.copyObject(userService.getById(userId), UserSpaceDTO.class));
    }

    /**
     * 获取用户基本信息
     *
     * @return
     */
    @RequiredPermission
    @RequestMapping("/loggedUser/baseInfo")
    public Result<UserBaseInfoDTO> getBaseInfo() {
        return Result.ok(userService.getUserBaseInfo());
    }

    /**
     * 获取用户高级信息
     *
     * @return
     */
    @RequiredPermission
    @RequestMapping("/loggedUser/seniorInfo")
    public Result<UserSeniorInfoDTO> getSeniorInfo() {
        UserSeniorInfoDTO userSeniorInfoDTO = userService.getUserSeniorInfo();
        if (Objects.isNull(userSeniorInfoDTO)) {
            return Result.fail("Can't Find The User");
        } else {
            return Result.ok(userSeniorInfoDTO);

        }
    }


    /**
     * 更新用户基本信息
     *
     * @param user
     * @return
     */
    @RequiredPermission
    @PostMapping("/loggedUser/updateBaseInfo")
    public Result<UserCoreDTO> updateBaseInfo(@RequestBody User user) {
        String Token = userService.UpdateBaseInfo(user);
        if (Token != null) {
            UserCoreDTO userCoreDTO = userService.updateBaseUser(user.getUsername());
            userCoreDTO.setUserToken(JwtUtil.creatToken(Long.parseLong(userCoreDTO.getId()), userCoreDTO.getUsername()));
            return Result.ok(userCoreDTO);
        } else
            return Result.fail("更新失败：参数错误");
    }

    /**
     * 更新用户密码
     */
    @RequiredPermission
    @PostMapping("/loggedUser/updateUserPassword")
    public Result updateUserPassword(@RequestBody Map<String, String> Password) {
        String oldPassword = Password.get("oldPassword");
        String newPassword = Password.get("newPassword");
        if (Objects.isNull(oldPassword) || Objects.isNull(newPassword)) {
            return Result.fail("关键错误为空");
        }
        if (userService.UpdatePassword(oldPassword, newPassword)) {
            return Result.ok();
        } else {
            return Result.fail("密码错误");
        }
    }

    /**
     * 获取验证码（以备修改电话号）
     *
     * @param telephone
     * @param request
     * @return
     */
    @RequiredPermission
    @GetMapping("/loggedUser/getVerifyCodes")
    public Result getVerifyCodes(
            @RequestParam(value = "telephone") String telephone,
            HttpServletRequest request) {
        String flag = VerifyCodeUntil.getVerifyCodes(6);
        if (redisUtil.set((request.getAttribute("userName") + "_" + telephone + "_verifyCodes"), flag, 300)) {
            tencentSmsUtil.sendMessage(telephone, SmsTemplateEnum.REGISTER.getTemplateID(), flag);
            return Result.ok();
        }
        return Result.fail("Redis缓存创建失败");
    }

    /**
     * 更新电话号码
     *
     * @param telephone 新的电话号码
     * @param verifyCodes 短信验证码
     * @param request
     * @return
     */
    @RequiredPermission
    @PostMapping("/loggedUser/updateTelePhone")
    public Result updateTelePhone(
            @RequestParam(value = "telephone") String telephone,
            @RequestParam(value = "verifyCodes") String verifyCodes,
            HttpServletRequest request) {
        Object redisCodes = redisUtil.get((request.getAttribute("userName") + "_" + telephone + "_verifyCodes"));
        if (redisCodes == null) {
            return Result.fail("无效验证码");
        }
        if (redisCodes.equals(verifyCodes)) {
            Long UserID = (Long) request.getAttribute("userID");
            User user = new User();
            user.setId(String.valueOf(UserID));
            user.setUserTele(telephone);
            user.setUserTeleCheck(true);
            userService.updateById(user);
            return Result.ok("验证成功～～～");
        } else {
            return Result.fail("验证码错误");
        }
    }


    /**
     * 更新QQ号绑定
     *
     * @param openid
     * @param request
     * @return
     */
    @RequiredPermission
    @PostMapping("/loggedUser/updateQQ/{openid}")
    public Result updateQQ(
            @PathVariable(value = "openid") String openid,
            HttpServletRequest request) {
        Long UserID = (Long) request.getAttribute("userID");
        User user = new User();
        user.setId(String.valueOf(UserID));
        user.setUserQqOpenid(openid);
        userService.updateById(user);
        return Result.ok("验证成功～～～");
    }


    /**
     * 关注/取关用户
     *
     * @param userId
     * @return
     */
    @RequiredPermission
    @PostMapping("/loggedUser/follow/{userId}")
    public Result<?> followUser(@PathVariable(value = "userId") String userId) {
        userService.saveFollow(userId);
        return Result.ok();
    }

    /**
     * 获取用户关注列表
     *
     * @param page
     * @return
     */
    @RequiredPermission
    @GetMapping({"/loggedUser/getFollowList/{page}", "/loggedUser/getFollowList"})
    public Result<List<UserSpaceDTO>> getFollowList(
            @PathVariable(value = "page", required = false) Integer page) {
        if (Objects.isNull(page)) {
            page = 1;
        }
        return Result.ok(userService.getFollowerList(page));
    }

    /**
     * 根据图片ID获取用户数据（关注、点赞）
     *
     * @param imgId
     * @param userId
     * @return
     */
    @RequiredPermission
    @GetMapping("/loggedUser/getImgDetail/{imgId}/{userId}")
    public Result<?> getImgDetail(@PathVariable(value = "imgId") String imgId,
                                  @PathVariable(value = "userId") String userId) {
        return Result.ok(userService.getUserFavByImgId(imgId, userId));
    }


    /**
     * 管理员获取用户信息
     *
     * @return
     */
    @RequiredPermission(admin = true)
    @GetMapping("/admin/getUserInfo")
    public Result<List<AdminUserInfoDTO>> getUserInfo() {
        return Result.ok(userService.getUserInfoForAdmin());
    }


    /**
     * 管理员审核用户
     *
     * @param auditType
     * @param adminUserInfoVOS
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/auditUser/{auditType}")
    public Result auditUser(@PathVariable(value = "auditType") Integer auditType,
                            @RequestBody List<AdminUserInfoVO> adminUserInfoVOS) {
        if (Objects.isNull(adminUserInfoVOS)) {
            return Result.fail("参数错误");
        }
        if (auditType == AuditBanEnum.NOT_PASS.getBandState()) {
            adminUserInfoVOS.forEach(item -> {
                item.setIsBan(AuditBanEnum.NOT_PASS.getBandState());
            });
        } else {
            adminUserInfoVOS.forEach(item -> {
                item.setIsBan(AuditBanEnum.PASS.getBandState());
            });
        }
        List<User> userList = BeanCopyUtil.copyList(adminUserInfoVOS, User.class);
        userList.forEach(user -> {
            userService.update(user, new LambdaQueryWrapper<User>()
                    .eq(User::getId, user.getId()));
        });
        return Result.ok();
    }

    /**
     * 管理员更新用户高级信息
     * @param identWhat
     * @param adminUserInfoVO
     * @return
     */
    @RequiredPermission(admin = true)
    @PostMapping("/admin/updateSeniorInfo/{identWhat}")
    public Result updateSeniorInfo(@PathVariable(value = "identWhat") String identWhat,
                                   @RequestBody AdminUserInfoVO adminUserInfoVO) {
        User user = BeanCopyUtil.copyObject(adminUserInfoVO, User.class);
        user.setUserRole(identWhat);
        userService.update(user,new LambdaQueryWrapper<User>()
                .eq(User::getId,user.getId()));
        return Result.ok();
    }


}
