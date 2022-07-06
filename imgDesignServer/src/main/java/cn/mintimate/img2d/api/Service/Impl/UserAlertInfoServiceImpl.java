package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.UserAlertInfoService;
import cn.mintimate.img2d.api.dao.UserAlertInfoDao;
import cn.mintimate.img2d.api.dto.UserAlertInfoDTO;
import cn.mintimate.img2d.api.entity.UserAlertInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class UserAlertInfoServiceImpl extends ServiceImpl<UserAlertInfoDao, UserAlertInfo> implements UserAlertInfoService {
    @Resource
    private UserAlertInfoDao userAlertInfoDao;
    @Resource
    private HttpServletRequest request;

    @Override
    public Integer getHavenReadInfo() {
        Integer count=userAlertInfoDao.getHavenReadCount(String.valueOf(request.getAttribute("userID")));
        if (Objects.isNull(count)){
            count=0;
        }
        return count;
    }

    @Override
    public List<UserAlertInfoDTO> getUserMessage() {
        return userAlertInfoDao.getUserMessage(String.valueOf(request.getAttribute("userID")));
    }

    @Override
    public void saveMessageAlert(String userId, String type, String message) {
        UserAlertInfo userAlertInfo = UserAlertInfo.builder()
                .userId(userId)
                .message(message)
                .type(type).build();
        userAlertInfoDao.insert(userAlertInfo);
    }

    @Override
    public boolean updateHasRead(String id) {
        UserAlertInfo userAlertInfo=UserAlertInfo.builder()
                .id(id)
                .hasRead(1)
                .build();
        userAlertInfoDao.update(userAlertInfo,
                new LambdaQueryWrapper<UserAlertInfo>().eq(UserAlertInfo::getId,id));
        return true;
    }

    @Override
    public boolean updateDelete(String id) {
        UserAlertInfo userAlertInfo=UserAlertInfo.builder()
                .id(id)
                .hasRead(1)
                .isDelete(1)
                .build();
        userAlertInfoDao.update(userAlertInfo,
                new LambdaQueryWrapper<UserAlertInfo>().eq(UserAlertInfo::getId,id));
        return true;
    }
}
