package com.zlk.controller;

import com.zlk.domain.ResponseResult;
import com.zlk.job.UpdateViewCountJob;
import com.zlk.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//文章接口
@RestController
@RequestMapping("/article")
public class ArticleController {
    //这里注入由于是多模块的，所以它依赖的是老版本的frameworks的jar包里的service
    //所以需要重新install父模块的maven项目
    //修改其他模块的值，需要重新install
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UpdateViewCountJob updateViewCountJob;
//    @GetMapping("/list")
//    public List<Article> test(){
//        return articleService.list();
//    }
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //查询热门文章，封装成ResponseResult返回
        ResponseResult result=articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    //更新viewCount
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        updateViewCountJob.updateViewCount();
        return articleService.updateViewCount(id);
    }

    @GetMapping("/{id}")//@PathVariable获取路径参数
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


}
