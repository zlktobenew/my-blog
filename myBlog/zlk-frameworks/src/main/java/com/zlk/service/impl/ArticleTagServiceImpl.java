package com.zlk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.domain.entity.ArticleTag;
import com.zlk.mapper.ArticleTagMapper;
import com.zlk.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ZlkArticleTag)表服务实现类
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-25 22:38:40
 */
@Service("ArticleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
