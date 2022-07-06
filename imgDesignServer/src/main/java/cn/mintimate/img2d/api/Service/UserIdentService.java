package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.vo.UserIdentVO;

import java.util.List;

/**
 * Desc
 *
 * @author Mintimate
 */
public interface UserIdentService {
    /**
     * 获取所有认证请求
     * @return 认证请求列表
     */
    List<UserIdentVO> getAllList();

    /**
     * 添加用户认证请求
     * @param userIdentVO
     */
    void addToList(UserIdentVO userIdentVO);

    /**
     * 审核用户递交的认证请求
     * @param userIdentVOS 用户认证请求
     * @return
     */
    boolean passList(List<UserIdentVO> userIdentVOS);

    /**
     * 删除用户递交的认证请求
     * @param userIdentVOS
     * @return
     */
    boolean refuseList(List<UserIdentVO> userIdentVOS);
}
