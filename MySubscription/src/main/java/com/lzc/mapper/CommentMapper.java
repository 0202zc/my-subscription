package com.lzc.mapper;

import com.lzc.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
@Mapper
@Repository
public interface CommentMapper {
    List<Comment> queryAllComments();

    List<Comment> queryCommentsByUserId(Integer userId);

    List<Comment> queryCommentsByEmail(String email);

    Comment queryCommentById(Integer commentId);

    Integer addComment(Comment comment);

    Integer deleteComment(Integer commentId);

    Integer updateComment(Comment comment);

    Integer deleteCommentByUserId(Integer userId);
}
