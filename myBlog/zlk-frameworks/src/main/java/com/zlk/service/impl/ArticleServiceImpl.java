package com.zlk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.constants.SystemConstants;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.adminVo.*;
import com.zlk.domain.entity.Article;
import com.zlk.domain.entity.ArticleTag;
import com.zlk.domain.entity.Category;
import com.zlk.domain.entity.Tag;
import com.zlk.domain.vo.ArticleDetailVo;
import com.zlk.domain.vo.ArticleListVo;
import com.zlk.domain.vo.HotArticleVo;
import com.zlk.domain.vo.PageVo;
import com.zlk.mapper.ArticleMapper;
import com.zlk.mapper.ArticleTagMapper;
import com.zlk.mapper.TagMapper;
import com.zlk.service.ArticleService;
import com.zlk.service.ArticleTagService;
import com.zlk.service.CategoryService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    //解决循环依赖问题
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //必须是正式文档,这个查询条件是在查找 status 字段等于 0 的文章记录。
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page=new Page(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();
        //相当于用工具类替代了下面的代码
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
//        bean拷贝：将上面的字段的值赋值给下面这个对象相应的值
//        List<HotArticleVo> articleVos=new ArrayList<>();
//        for(Article article:articles){
//            HotArticleVo vo=new HotArticleVo();
//            一个源数据对象，一个目标数据对象
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }

        return ResponseResult.okResult(vs);
    }

    //分页查询
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //1.1如果有categoryId，要求查询时和传入的相同,第一个代表条件，只有条件成立才进行这个操作
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //1.2状态只能是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //1.3置顶的文章在最前面，对isTop进行降序排序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop).orderByDesc(Article::getUpdateTime);

        //分页查询
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);


        //由于前端返回的data里有两个属性，一个是rows,一个是total，即返回一个数组，和总记录数total
        //查询categoryName
        List<Article> articles = page.getRecords();

       /* //articleId去查询articleName进行设置
        //tips，数据库中会有一个categoryId,但是Vo中是以categoryName存在的，所以需要匹配
        for (Article article : articles) {
            //查询到对应的分类
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/

        //Stream流方式实现上面的代码
        articles = articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        //获取分类id，查询分类信息，获取分类名称
                        Category category = categoryService.getById(article.getCategoryId());
                        String name = category.getName();
                        //把分类名称设置给article
                        article.setCategoryName(name);
                        return article;
                    }
                }).collect(Collectors.toList());
        //TODO，这里特判一下ArticleListVo里五个值不能为空
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //根据id查询文章内容查询
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("Article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId=articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("Article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleMapper articleMapper;
    @Override
    @Transactional//开启事务
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    //获取文章列表
    @Override
    public ResponseResult adminGetArticleList(ArticleVo articleVo) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据id进行升序排序
        lambdaQueryWrapper.orderByAsc(Article::getId);
        // 根据标题模糊查询
        if (StringUtils.isNotBlank(articleVo.getTitle())) {
            lambdaQueryWrapper.like(Article::getTitle, "%" + articleVo.getTitle() + "%");
        }
        //分页查询
        Page<Article> page=new Page<>(articleVo.getPageNum(),articleVo.getPageSize());
        page(page, lambdaQueryWrapper);
        //拷贝
        List<AddArticleDto> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), AddArticleDto.class);
        //封装rows和total的对象
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    //更新文章
    @Override
    public ResponseResult adminUpdateArticleList(UpdateArticleVo updateArticleVo) {
        Long id=updateArticleVo.getId();
        Article article=articleMapper.selectById(updateArticleVo.getId());
        UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
        // 设置要更新的字段和值
        wrapper.eq("id",updateArticleVo.getId())
                .set("category_id", updateArticleVo.getCategoryId())
                .set("content", updateArticleVo.getContent())
                .set("is_comment", updateArticleVo.getIsComment())
                .set("is_top", updateArticleVo.getIsTop())
                .set("status", updateArticleVo.getStatus())
                .set("summary", updateArticleVo.getSummary())
                .set("thumbnail", updateArticleVo.getThumbnail())
                .set("title", updateArticleVo.getTitle())
                .set("create_by",updateArticleVo.getCreateBy())
                .set("create_time",updateArticleVo.getCreateTime());
        /*
在 MyBatis-Plus 的 update 方法中，第一个参数表示要更新的实体对象，而第二个参数是更新条件。
由于你在这个更新操作中不是更新一个具体的实体对象，而是根据条件更新多个记录，因此将第一个参数设置为 null 是合理的。*/
        articleMapper.update(article, wrapper);
        //更新tag
        List<ArticleTag> ll=new ArrayList<>();
        //接受tags集合
        List<String> list=updateArticleVo.getTags();
        for(String l:list){
            ArticleTag articleTag=new ArticleTag();
            articleTag.setTagId(updateArticleVo.getId());
            articleTag.setArticleId(Long.parseLong(l));
            ll.add(articleTag);
        }
        articleTagService.saveBatch(ll);
        return ResponseResult.okResult();
    }
    @Autowired
    private ArticleTagMapper articleTagMapper;
    //更加id查看文章详情
    @Override
    public ResponseResult selectArticleById(Long id) {

        Article article=articleMapper.selectById(id);
        //根据id查找tags
        // 假设你的 article_tag 表的实体类为 ArticleTag，且有一个 article_id 字段用于查询
        List<String> tagIds = new ArrayList<>();

        // 根据id用selectList 方法来查询 article_tag符合的数据
        List<ArticleTag> articleTags = articleTagMapper.selectList(new QueryWrapper<ArticleTag>().eq("article_id", id));
        // 遍历查询结果，将 tag_id 列的值添加到 List<String> 中
        for (ArticleTag articleTag : articleTags) {
            tagIds.add(articleTag.getTagId().toString());
        }
        ArticleTagVo articleTagVo=new ArticleTagVo();
        ArticleTagVo articleTagVo1 = BeanCopyUtils.copyBean(article, ArticleTagVo.class);
        articleTagVo1.setTags(tagIds);
        return ResponseResult.okResult(articleTagVo1);
    }

    @Override
    public ResponseResult adminDeleteArticleById(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id) // 设置条件，这里假设你要更新id为1的行
                .set(Article::getDelFlag, 1); // 设置要更新的字段及其值

        int updateResult = articleMapper.update(null, updateWrapper);
        return ResponseResult.okResult();
    }
}
