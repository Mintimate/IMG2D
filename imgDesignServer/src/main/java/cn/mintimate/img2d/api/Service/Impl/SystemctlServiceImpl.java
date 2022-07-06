package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.SystemctlService;
import cn.mintimate.img2d.api.dto.ArticleHomeDTO;
import cn.mintimate.img2d.api.dto.ArticleHomeRightDTO;
import cn.mintimate.img2d.api.dto.SystemctlBannerIndexDTO;
import cn.mintimate.img2d.api.until.BeanCopyUtil;
import cn.mintimate.img2d.api.until.RedisUtil;
import cn.mintimate.img2d.api.vo.SystemctlBannerIndexVO;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.mintimate.img2d.api.constant.RedisPrefixConst.IMG2D_SYSTEM_BANNER_LIST;

/**
 * @author Mintimate
 */
@Service
public class SystemctlServiceImpl implements SystemctlService {
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<SystemctlBannerIndexDTO> getBannerIndex() {
        // 将redis中的数据转换为对象集合
        List<Object> bannerIndex= redisUtil.lGet(IMG2D_SYSTEM_BANNER_LIST, 0, -1);
        if (bannerIndex.size()==0){
            redisUtil.lRemove(IMG2D_SYSTEM_BANNER_LIST);
            SystemctlBannerIndexDTO systemctlBannerIndexDTO=SystemctlBannerIndexDTO.builder()
                    .topId("-1")
                    .build();
            redisUtil.lSet(IMG2D_SYSTEM_BANNER_LIST, JSON.toJSON(systemctlBannerIndexDTO));
            List<SystemctlBannerIndexDTO> systemctlBannerIndexDTOList=new ArrayList<>();
            systemctlBannerIndexDTOList.add(systemctlBannerIndexDTO);
            return systemctlBannerIndexDTOList;
        }
        List<SystemctlBannerIndexDTO> systemctlBannerIndexDTOS= JSON.parseArray(String.valueOf(bannerIndex),SystemctlBannerIndexDTO.class);
        return systemctlBannerIndexDTOS;
    }

    @Override
    public void saveBannerIndex(List<SystemctlBannerIndexVO> systemctlBannerIndexVOS) {
        redisUtil.lRemove(IMG2D_SYSTEM_BANNER_LIST);
        List<SystemctlBannerIndexDTO> systemctlBannerIndexDTOList=BeanCopyUtil.copyList(
                systemctlBannerIndexVOS,SystemctlBannerIndexDTO.class);
        for (SystemctlBannerIndexDTO systemctlBannerIndexDTO:systemctlBannerIndexDTOList){
            redisUtil.lSet(IMG2D_SYSTEM_BANNER_LIST, JSON.toJSON(systemctlBannerIndexDTO));
        }
    }
}
