package com.zlk.controller;

import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.AddArticleDto;
import com.zlk.domain.adminVo.ArticleVo;
import com.zlk.domain.adminVo.UpdateArticleVo;
import com.zlk.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博文接口
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult getArticleList(ArticleVo articleVo){
        return articleService.adminGetArticleList(articleVo);
    }

    //更新文章
    @PutMapping
    public ResponseResult updateArticleList(@RequestBody  UpdateArticleVo updateArticleVo){
        return articleService.adminUpdateArticleList(updateArticleVo);
    }
    //根据id查询文章详情
    @GetMapping("/{id}")
    public ResponseResult selectArticleById(@PathVariable Long id){
        return articleService.selectArticleById(id);
    }

    //根据id删除文章
    @DeleteMapping("/{id}")
    public ResponseResult deteleArticleById(@PathVariable Long id){
        return articleService.adminDeleteArticleById(id);
    }
}