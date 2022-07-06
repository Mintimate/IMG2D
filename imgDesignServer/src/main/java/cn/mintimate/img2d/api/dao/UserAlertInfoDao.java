package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.UserAlertInfoDTO;
import cn.mintimate.img2d.api.entity.UserAlertInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
@Mapper
public interface UserAlertInfoDao extends BaseMapper<UserAlertInfo> {

    /**
     * 根据用户ID获取用户提醒
     * @param userId 用户ID
     * @return 提醒列表DTO
     */
    List<UserAlertInfoDTO> getUserMessage(String userId);

    /**
     * 根据用户ID获取用户未读提醒数量
     * @param userId
     * @return
     */
    Integer getHavenReadCount(@Param("userId") String userId);
}
