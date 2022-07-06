package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.UserAlertInfoDTO;

import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface UserAlertInfoService {

    /**
     * 根据用户ID获取用户的未读信息数量
     * @return
     */
    Integer getHavenReadInfo();

    /**
     * 根据用户ID获取用户的提示信息
     * @return 用户提示信息DTO
     */
    List<UserAlertInfoDTO> getUserMessage();

    /**
     * 添加用户信息
     */
    void saveMessageAlert(String userId,String type,String message);

    /**
     * 更新为已读
     * @param id
     * @return
     */
    boolean updateHasRead(String id);

    /**
     * 更新删除
     * @param id
     * @return
     */
    boolean updateDelete(String id);
}
