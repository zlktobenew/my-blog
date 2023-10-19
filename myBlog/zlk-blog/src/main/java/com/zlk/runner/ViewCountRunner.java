package com.zlk.runner;

import com.zlk.domain.entity.Article;
import com.zlk.mapper.ArticleMapper;
import com.zlk.service.ArticleService;
import com.zlk.utlis.RedisCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {
                    @Override
                    //这里不使用Long类型的原因，因为把ViewCount存到redis当中
                    //如果是Long类型的话，它是以1L的形式存在的，而它无法递增，所以需要Integer
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));
        //存储到redis中
        redisCache.setCacheMap("Article:viewCount",viewCountMap);
    }
}