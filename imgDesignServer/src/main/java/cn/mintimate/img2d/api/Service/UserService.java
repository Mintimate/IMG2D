package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.*;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.enums.SmsTemplateEnum;
import cn.mintimate.img2d.api.vo.UserLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 用户使用账户和密码登录
     * @param userLoginVO
     * @return 用户核心数据
     */
    UserCoreDTO login(UserLoginVO userLoginVO);

    /**
     * 用户使用短信登录
     * @param telephone 用户手机号
     * @return
     */
    UserCoreDTO loginByTelephone(String telephone);

    /**
     * 用户名和Email是否重复
     * @param Username
     * @param Email
     * @return
     */
    int checkRepeat(String Username,String Email);


    /**
     * 更新用户基本信息
     * @param Username
     * @return
     */
    UserCoreDTO updateBaseUser(String Username);

    /**
     * 获取用户基本信息
     * @return 用户基本信息
     */
    UserBaseInfoDTO getUserBaseInfo();

    /**
     * 用户高级信息获取
     * @return 用户高级信息
     */
    UserSeniorInfoDTO getUserSeniorInfo();

    /**
     * 用户基础信息更新
     * @param user
     * @return
     */
    String UpdateBaseInfo(User user);


    /**
     * 更新密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    boolean UpdatePassword(String oldPassword,String newPassword);

    /**
     * 查看QQ记录
     * @param openid
     * @return
     */
    boolean QQOpenIdSomeoneHave(String openid);

    /**
     * 用户使用QQ进行登录
     * @param openid QQ唯一识别ID
     * @return
     */
    List<String> loginByQQ(String openid);

    /**
     * 发送短信
     */
    boolean sendSMS(String teleNumber,SmsTemplateEnum smsTemplateEnum);

    /**
     * 关注或取消关注
     * @param userId 被关注这id
     */
    void saveFollow(String userId);

    List<UserSpaceDTO> getFollowerList(Integer currentPage);
    /**
     * 针对单张图片
     * 用户是否点赞、关注、收藏
     */
    UserFavDTO getUserFavByImgId(String imgId,String uploadUserId);

    /**
     * 管理员获取用户基本信息
     * @return 用户信息列表
     */
    List<AdminUserInfoDTO> getUserInfoForAdmin();
}
