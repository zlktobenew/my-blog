package com.zlk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlk.constants.SystemConstants;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Comment;
import com.zlk.domain.vo.CommentVo;
import com.zlk.domain.vo.PageVo;
import com.zlk.mapper.CommentMapper;
import com.zlk.service.CommentService;
import com.zlk.service.UserService;
import com.zlk.utlis.BeanCopyUtils;
import com.zlk.utlis.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-23 13:39:23
 */

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    //这里注入的目的是通过createBy查询用户的信息
    @Autowired
    private UserService userService;

    //评论查询（包括父评论和子评论），commentType代表评论类型，友联或者文章评论
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //1查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断，根据这个查询对应文章所有的根评论，先不考虑子评论
        //小细节：当commenttype为友联评论时，articleId为null，那么这里会出现异常，所以需要判断，只有是文章评论才查询articleId,所以第一个为判断条件
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //筛选条件：根评论 rootId为-1，这个-1最好是常量
        queryWrapper.orderByDesc(Comment::getCreateTime);
        queryWrapper.eq(Comment::getRootId,-1);
        //评论类型，要求这个评论的类型要和穿进来的参数是一致的
        queryWrapper.eq(Comment::getType,commentType);

        //2分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);
        //3属性拷贝,返回需要的属性
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //4查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    //增加评论
    @Override
    public ResponseResult addComment(Comment comment) {
        //这里可以对新增评论内容的限制comment.getContent())

        /*1首先插入数据库的字段还需要增加创建人，创建时间，更新人，更新时间等字段
        1.1这里setCreateBy接受的参数需要是userId，但是前端一般不传这个参数，因为不安全，
        1.2后端一般解析前端传进来的token，解析获得userId,而token解析完获取了用户id存储在authenticationToken
        SecurityUtils是自定义的工具类*/
        comment.setCreateBy(SecurityUtils.getUserId());
        /* 2再根据userid填充创建人，创建时间，更新人，更新时间等字段
        2.1mybatisplus提供了一个方便的字段自动填充的功能
        注意这里调用了mybatisplus内置的一个类实现填充，具体可以看handler包下的mybatisplus里的一个类去看
        3save会执行insert语句操作数据库*/
        save(comment);
        return ResponseResult.okResult();
    }



    //工具类
    //但是拷贝过程中，CommentVo中有一些属性是Comment没有的，所以需要手动添加
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for(CommentVo commentVo:commentVos){
            //通过createBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            //TODO记录一下，这里的getToCommentUserId不小心打成了getToCommentId结果搞了半个小时的bug，最后debug才发现到了这里，我真是个小天才
            if(commentVo.getToCommentUserId()!=-1){
                //说明它不是根评论，是子评论，所以有父评论的id
                String nickName1 = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(nickName1);
            }
            //拿到头像的地址
            String avatar=userService.getById(commentVo.getCreateBy()).getAvatar();
            commentVo.setAvatar(avatar);
        }
        return commentVos;
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        //查询所有的子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        //使用这个方法拷贝可以完善查询toCommentUserName和username
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
}
