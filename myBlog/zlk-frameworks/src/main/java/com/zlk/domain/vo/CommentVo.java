package com.zlk.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    /*
   这个属性通常用于表示评论与目标评论的关联。它存储了目标评论的唯一标识，通常是目标评论的评论ID或主键。
toCommentId指向了目标评论的具体记录，使您可以轻松地查找和定位到与之关联的评论。
这个属性通常用于构建评论的父子关系，以便在评论系统中形成评论线程或嵌套评论的结构。*/
    //所回复的目标评论的userid
    private Long toCommentUserId;
    private String toCommentUserName;
    /*
    * 这个属性通常用于表示评论所回复的目标用户的唯一标识。它存储了目标用户的用户ID或主键。
toCommentUserId指向了目标用户的记录，表示这个评论是针对目标用户发布的，通常用于通知或提醒目标用户有人对其评论。
这个属性通常用于在评论系统中实现用户间的互动和通知功能，例如，当有人回复了您的评论时，系统可以使用toCommentUserId来通知您。*/
    //回复目标评论id
    private Long toCommentId;

    private Long createBy;

    private Date createTime;

    private String username;

    //头像
    private String avatar;

    //根评论下的所有子评论
    private List<CommentVo> children;
}
