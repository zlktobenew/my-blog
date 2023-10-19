package com.zlk.controller;

import com.zlk.constants.SystemConstants;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.dto.AddCommentDto;
import com.zlk.domain.entity.Comment;
import com.zlk.service.CommentService;
import com.zlk.utlis.BeanCopyUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//评论接口
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
//    @GetMapping("/commentList")
//    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
//        return commentService.commentList(articleId,pageNum,pageSize);
//    }
    //改进版区别友联评论查询和文章评论查询
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }


    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto commentDto){
        Comment comment = BeanCopyUtils.copyBean(commentDto, Comment.class);
        return commentService.addComment(comment);
    }


    //SystemConstants.LINK_COMMENT根据表中的type来区别友联和文章评论查询
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}