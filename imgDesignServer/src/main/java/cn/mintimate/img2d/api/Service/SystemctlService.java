package cn.mintimate.img2d.api.Service;

import cn.mintimate.img2d.api.dto.SystemctlBannerIndexDTO;
import cn.mintimate.img2d.api.vo.SystemctlBannerIndexVO;

import java.util.List;

/**
 * @author Mintimate
 */
public interface SystemctlService {
    /**
     * 获取首页头图
     * @return 头图列表
     */
    List<SystemctlBannerIndexDTO> getBannerIndex();

    /**
     * 保持首页头图
     * @param systemctlBannerIndexVOS 首页头图列表
     */
    void saveBannerIndex(List<SystemctlBannerIndexVO> systemctlBannerIndexVOS);
}
