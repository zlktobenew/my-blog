package com.zlk.job;

import com.zlk.domain.entity.Article;
import com.zlk.service.ArticleService;
import com.zlk.utlis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;






    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0/55 * * * * ?")
    public  void updateViewCount(){
        UpdateViewCountFlag.setUpdateViewCountFlag(true);
        //获取redis的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("Article:viewCount");
        //但是由于updateBatchById只能接受单列集合，所以要对这个hash集合进行处理
        List<Article> articles = viewCountMap.entrySet()//转化成单列id,至于为什么用这个，自己可以搜索
                .stream()//把entry对象转化成article对象，直接新增article的构造方法使用即可
                //第一个参数由于是string类型，需要转化成long类型，而第二个类型是Integer类型，也需要转换成long类型，可以使用integer类型的方法
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                //收集，成为list对象
                .collect(Collectors.toList());

        UpdateViewCountFlag.clear(); // 重置标志
        //更新到数据库
        articleService.updateBatchById(articles);

    }
}
