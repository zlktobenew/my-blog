package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.AddArticleDto;
import com.zlk.domain.adminVo.ArticleVo;
import com.zlk.domain.adminVo.UpdateArticleVo;
import com.zlk.domain.entity.Article;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult adminGetArticleList(ArticleVo articleVo);

    ResponseResult adminUpdateArticleList(UpdateArticleVo updateArticleVo);

    ResponseResult selectArticleById(Long id);

    ResponseResult adminDeleteArticleById(Long id);
}
