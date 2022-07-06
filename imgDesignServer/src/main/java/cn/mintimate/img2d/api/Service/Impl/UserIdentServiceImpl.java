package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.UserIdentService;
import cn.mintimate.img2d.api.dao.UserDao;
import cn.mintimate.img2d.api.entity.User;
import cn.mintimate.img2d.api.until.RedisUtil;
import cn.mintimate.img2d.api.vo.UserIdentVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_USER_IDENT_LIST;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class UserIdentServiceImpl implements UserIdentService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserDao userDao;

    @Override
    public List<UserIdentVO> getAllList() {
        // 获取Redis里的信息
        List<Object> userIdentSource = redisUtil.lGet(IMG2D_USER_IDENT_LIST, 0, -1);
        List<UserIdentVO> userIdentList = JSON.parseArray(String.valueOf(userIdentSource), UserIdentVO.class);
        return userIdentList;
    }

    @Override
    public void addToList(UserIdentVO userIdentVO) {
        String userId = String.valueOf(request.getAttribute("userID"));
        userIdentVO.setUserId(userId);
        // 获取Redis里的信息
        List<Object> userIdentSource = redisUtil.lGet(IMG2D_USER_IDENT_LIST, 0, -1);
        List<UserIdentVO> userIdentList = JSON.parseArray(String.valueOf(userIdentSource), UserIdentVO.class);
        Iterator<UserIdentVO> item = userIdentList.iterator();
        while (item.hasNext()) {
            if (item.next().getUserId().equals(userId)) {
                item.remove();
            }
        }
        userIdentList.add(userIdentVO);
        // 清空列表
        redisUtil.lRemove(IMG2D_USER_IDENT_LIST);
        // 重新存储
        for (UserIdentVO userIdentVO1 : userIdentList) {
            redisUtil.lSet(IMG2D_USER_IDENT_LIST, JSON.toJSON(userIdentVO1));
        }
    }

    @Override
    public boolean passList(List<UserIdentVO> userIdentVOS) {
        // 获取Redis里的信息
        List<Object> userIdentSource = redisUtil.lGet(IMG2D_USER_IDENT_LIST, 0, -1);
        List<UserIdentVO> userIdentList = JSON.parseArray(String.valueOf(userIdentSource), UserIdentVO.class);
        for (UserIdentVO userIdentVO : userIdentVOS) {
            Iterator<UserIdentVO> item = userIdentList.iterator();
            while (item.hasNext()) {
                if (item.next().getUserId().equals(userIdentVO.getUserId())) {
                    item.remove();
                }
            }
            User user = User.builder()
                    .id(userIdentVO.getUserId())
                    .userRole(userIdentVO.getIdentWhat())
                    .build();
            userDao.update(user, new LambdaQueryWrapper<User>()
                    .eq(User::getId, user.getId()));
        }
        // 清空列表
        redisUtil.lRemove(IMG2D_USER_IDENT_LIST);
        // 重新存储
        for (UserIdentVO userIdentVO1 : userIdentList) {
            redisUtil.lSet(IMG2D_USER_IDENT_LIST, JSON.toJSON(userIdentVO1));

        }
        return true;
    }

    @Override
    public boolean refuseList(List<UserIdentVO> userIdentVOS) {
        // 获取Redis里的信息
        List<Object> userIdentSource = redisUtil.lGet(IMG2D_USER_IDENT_LIST, 0, -1);
        List<UserIdentVO> userIdentList = JSON.parseArray(String.valueOf(userIdentSource), UserIdentVO.class);
        for (UserIdentVO userIdentVO : userIdentVOS) {
            Iterator<UserIdentVO> item = userIdentList.iterator();
            while (item.hasNext()) {
                if (item.next().getUserId().equals(userIdentVO.getUserId())) {
                    item.remove();
                }
            }
        }
        // 清空列表
        redisUtil.lRemove(IMG2D_USER_IDENT_LIST);
        // 重新存储
        for (UserIdentVO userIdentVO1 : userIdentList) {
            redisUtil.lSet(IMG2D_USER_IDENT_LIST, JSON.toJSON(userIdentVO1));

        }
        return true;
    }


}
