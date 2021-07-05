package com.lzc.service;

import com.lzc.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> queryAllComments();

    List<Comment> queryCommentsByUserId(Integer userId);

    List<Comment> queryCommentsByEmail(String email);

    Comment queryCommentById(Integer commentId);

    Integer addComment(Comment comment);

    Comment addComment(String email, String note);

    Integer deleteComment(Integer commentId);

    Integer updateComment(Comment comment);

    Integer deleteCommentByUserId(Integer userId);
}
