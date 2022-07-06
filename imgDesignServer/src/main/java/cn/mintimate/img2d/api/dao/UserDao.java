package cn.mintimate.img2d.api.dao;

import cn.mintimate.img2d.api.dto.UserCoreDTO;
import cn.mintimate.img2d.api.dto.UserSpaceDTO;
import cn.mintimate.img2d.api.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserDao extends BaseMapper<User> {
    List<User> selectByUsername(String username);
    List<User> selectByEmail(String userEmail);
    User findUserByEmailAndPasswd(String userEmail,String Password);
    User findUserByTeleAndPasswd(String userTele,String Password);
    User getUserBaseInfoWithoutPasswordByUsername(String username);
    User getUserBaseInfoWithoutPasswordByID(Long id);
    User getUserSeniorInfoWithoutPasswordByID(Long id);
    User findUserByUserName(String UserName);

    /**
     * 获取关注人的用户信息
     * @param idList 用户ID列表
     * @param current 分页，当前选定的页面
     * @param size  分页，每页几条数据
     * @return
     */
    List<UserSpaceDTO> getFollowerListByUserId(@Param("idList") Set idList,
                                               @Param("current") long current,
                                               @Param("size") Long size);

}
