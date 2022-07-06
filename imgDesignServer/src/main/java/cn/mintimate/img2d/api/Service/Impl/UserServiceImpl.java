package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.UserAlertInfoService;
import cn.mintimate.img2d.api.Service.UserService;
import cn.mintimate.img2d.api.dto.*;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.dao.UserDao;
import cn.mintimate.img2d.api.enums.SmsTemplateEnum;
import cn.mintimate.img2d.api.enums.UserAlertInfoEnum;
import cn.mintimate.img2d.api.until.*;
import cn.mintimate.img2d.api.vo.UserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.*;
import static cn.mintimate.img2d.api.constant.SystemConfigConst.SALT;


@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAlertInfoService userAlertInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private HttpServletRequest servletRequest;


    @Override
    public UserCoreDTO login(UserLoginVO userLoginVO) {
        User user;
        if (userLoginVO.getMode().equals("Email")) {
            user = userDao.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUserEmail, userLoginVO.getUserAccount())
                    .eq(User::getPassword, userLoginVO.getPassword()));
        } else {
            user = userDao.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUserTele, userLoginVO.getUserAccount())
                    .eq(User::getPassword, userLoginVO.getPassword()));
        }
        UserCoreDTO userCoreDTO = BeanCopyUtil.copyObject(user, UserCoreDTO.class);
        return userCoreDTO;
    }

    @Override
    public UserCoreDTO loginByTelephone(String telephone) {
        User user = userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserTele, telephone));
        UserCoreDTO userCoreDTO = BeanCopyUtil.copyObject(user, UserCoreDTO.class);
        userCoreDTO.setUserToken(JwtUtil.creatToken(Long.parseLong(userCoreDTO.getId()), userCoreDTO.getUsername()));
        return userCoreDTO;
    }

    @Override
    public int checkRepeat(String Username, String Email) {
        int stateCode = 0;
        if (Username == null || Email == null)
            return -1;
        if (!userDao.selectByUsername(Username).isEmpty()) {
            stateCode += 1;
        }
        if (!userDao.selectByEmail(Email).isEmpty()) {
            stateCode += 2;
        }
        return stateCode;
    }

    @Override
    public UserCoreDTO updateBaseUser(String Username) {
        User user = userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, Username));
        UserCoreDTO userCoreDTO = BeanCopyUtil.copyObject(user, UserCoreDTO.class);
        return userCoreDTO;
    }

    @Override
    public UserBaseInfoDTO getUserBaseInfo() {
        User user = userDao.selectOne(new LambdaQueryWrapper<>(User.class)
                .eq(User::getId, servletRequest.getAttribute("userID")));
        UserBaseInfoDTO userBaseInfoDTO = BeanCopyUtil.copyObject(user, UserBaseInfoDTO.class);
        return userBaseInfoDTO;
    }

    @Override
    public UserSeniorInfoDTO getUserSeniorInfo() {
        User user = userDao.selectOne(new LambdaQueryWrapper<>(User.class)
                .eq(User::getId, servletRequest.getAttribute("userID")));
        UserSeniorInfoDTO userSeniorInfoDTO = BeanCopyUtil.copyObject(user, UserSeniorInfoDTO.class);
        return userSeniorInfoDTO;
    }

    @Override
    public String UpdateBaseInfo(User user) {
        user.setId(userDao.findUserByUserName(user.getUsername()).getId());
        if (user.getUserAvatarPath() != null) {
            // 如果用户并没有修改头像，则用户头像不更新
            String imageBase64Str = user.getUserAvatarPath().replace("data:image/png;base64,", "");
            String AvatarPathName = IdWorker.getId() + user.getUsername();
            Base64ImgUtil.GenerateUserAvatar(imageBase64Str, AvatarPathName);
            user.setUserAvatarPath("/SystemData/UserData/Avatar/" + AvatarPathName + ".jpeg");
        } else {
            user.setUserAvatarPath(user.getUserAvatarPath());
        }
        userDao.updateById(user);
        return JwtUtil.creatToken(Long.parseLong(user.getId()), user.getUsername());
    }

    @Override
    public boolean UpdatePassword(String oldPassword, String newPassword) {
        newPassword= DigestUtils.md5DigestAsHex((SALT+newPassword).getBytes(StandardCharsets.UTF_8));
        User user = userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, servletRequest.getAttribute("userID")));
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        } else {
            user.setPassword(newPassword);
            userDao.updateById(user);
            userAlertInfoService.saveMessageAlert(
                    String.valueOf(servletRequest.getAttribute("userID")),
                    UserAlertInfoEnum.USER.getType(),
                    AlertInfoDemoUtil.User_Remote_Change_Password(IPUtil.getIpZone(servletRequest)));
        }
        return true;
    }

    @Override
    public boolean QQOpenIdSomeoneHave(String openid) {
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserQqOpenid,openid));
        if (Objects.isNull(user)) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public List<String> loginByQQ(String openid) {
        List<String> list = new ArrayList<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_qq_openid", openid);
        try {
            User user = userDao.selectOne(wrapper);
            if (user != null) {
                list.add(JwtUtil.creatToken(Long.parseLong(user.getId()), user.getUsername()));
                list.add(user.getUserRole());
                list.add(user.getUsername());
                return list;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean sendSMS(String teleNumber, SmsTemplateEnum smsTemplateEnum) {
        // 随机六个数字
        String flag = VerifyCodeUntil.getVerifyCodes(6);
        // 存储到Redis内，并设置有效期5分钟
        if (redisUtil.set(teleNumber + "_verifyCodes", flag, 300)) {
            return tencentSmsUtil.sendMessage(teleNumber, smsTemplateEnum.getTemplateID(), flag);
        }
        return false;
    }

    @Override
    public void saveFollow(String userId) {
        String followUserId = String.valueOf(servletRequest.getAttribute("userID"));
        String followedKey = IMG2D_USER_BE_FOLLOWED_LIST + userId;
        // 判断是否已经关注
        String followKey = IMG2D_USER_FOLLOW_LIST + followUserId;
        if (redisUtil.sHasKey(followKey, userId)) {
            // 关注过则删除用户id
            redisUtil.setRemove(followKey, userId);
            redisUtil.setRemove(followedKey, followUserId);
            // 评论点赞量-1
            redisUtil.hdecr(IMG2D_USER_BE_FOLLOWED_COUNT, userId, 1L);
        } else {
            // 未点赞则增加评论id
            redisUtil.sSet(followKey, userId);
            redisUtil.sSet(followedKey, followUserId);
            // 评论点赞量+1
            redisUtil.hincr(IMG2D_USER_BE_FOLLOWED_COUNT, userId, 1L);
        }
    }

    @Override
    public List<UserSpaceDTO> getFollowerList(Integer currentPage) {
        Set<Object> idList = redisUtil.sGet(IMG2D_USER_FOLLOW_LIST + servletRequest.getAttribute("userID"));
        if (idList.size()==0){
            return null;
        }
        PageUtil.setCurrentPage(new Page<>(currentPage, 8));
        return userDao.getFollowerListByUserId(idList, PageUtil.getLimitCurrent(), PageUtil.getSize());
    }

    @Override
    public UserFavDTO getUserFavByImgId(String imgId, String uploadUserId) {
        UserFavDTO userFavDTO = UserFavDTO.builder()
                .hasCollect(false)
                .hasFollow(false)
                .hasLikeImg(false)
                .build();
        // 是否已经点赞了
        if (redisUtil.sHasKey(IMG2D_IMG_USER_LIKE + servletRequest.getAttribute("userID"), imgId)) {
            userFavDTO.setHasLikeImg(true);
        }
        // 是否已经收藏了图片
        if (redisUtil.sHasKey(IMG2D_IMG_USER_COLLECT + servletRequest.getAttribute("userID"), imgId)) {
            userFavDTO.setHasCollect(true);
        }
        if (redisUtil.sHasKey(IMG2D_USER_FOLLOW_LIST + servletRequest.getAttribute("userID"), uploadUserId)) {
            userFavDTO.setHasFollow(true);
        }
        return userFavDTO;
    }

    @Override
    public List<AdminUserInfoDTO> getUserInfoForAdmin() {
        List<AdminUserInfoDTO> adminUserInfoDTOList= BeanCopyUtil.copyList(this.list(),AdminUserInfoDTO.class);
        return adminUserInfoDTOList;
    }
}
