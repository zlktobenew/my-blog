package com.zlk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.domain.ResponseResult;
import com.zlk.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author zlk a person who is mysterious
 * @since 2023-09-23 13:39:23
 */
public interface CommentService extends IService<Comment> {
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
