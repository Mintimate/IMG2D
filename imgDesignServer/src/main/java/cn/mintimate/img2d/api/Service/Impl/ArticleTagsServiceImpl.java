package cn.mintimate.img2d.api.Service.Impl;

import cn.mintimate.img2d.api.Service.ArticleTagsService;
import cn.mintimate.img2d.api.dao.ArticleTagsDao;
import cn.mintimate.img2d.api.entity.ArticleTags;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Desc
 *
 * @author Mintimate
 */
@Service
public class ArticleTagsServiceImpl extends ServiceImpl<ArticleTagsDao, ArticleTags> implements ArticleTagsService {
}
