package com.lzc.service.impl;

import com.lzc.mapper.CommentMapper;
import com.lzc.mapper.UserMapper;
import com.lzc.pojo.Comment;
import com.lzc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
@Service
@CacheConfig(cacheNames = "comment")
public class CommentServiceImpl implements CommentService  {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Comment> queryAllComments() {
        return commentMapper.queryAllComments();
    }

    @Override
    public List<Comment> queryCommentsByUserId(Integer userId) {
        return commentMapper.queryCommentsByUserId(userId);
    }

    @Override
//    @Cacheable(key = "#email", condition = "null ne #email", unless = "null ne #result")
    public List<Comment> queryCommentsByEmail(String email) {
//        commentMapper.queryCommentById(userMapper.queryUserByEmail(email).getId());
        return commentMapper.queryCommentsByEmail(email);
    }

    @Override
//    @Cacheable(key = "#commentId", unless = "null eq #result")
    public Comment queryCommentById(Integer commentId) {
        return commentMapper.queryCommentById(commentId);
    }

    @Override
//    @CachePut(key = "#comment.id")
    public Integer addComment(Comment comment) {
        return commentMapper.addComment(comment);
    }

    @Override
//    @CachePut(key = "#result.id", condition = "null ne #email and null ne #note", unless = "null eq #result")
    public Comment addComment(String email, String note) {
        if(email != null && email.trim().length() != 0 && note != null && note.trim().length() != 0) {
            Integer userId = userMapper.queryUserByEmail(email).getId();
            Comment comment = new Comment(userId, note);
            // 返回自动生成的id，存入comment.id中
            return commentMapper.addComment(comment) == 1 ? comment : null;
        } else {
            return null;
        }
    }

    @Override
//    @CacheEvict(key = "#commentId")
    public Integer deleteComment(Integer commentId) {
        return commentMapper.deleteComment(commentId);
    }

    @Override
//    @CachePut(key = "#comment.id", unless = "0 ne #result")
    public Integer updateComment(Comment comment) {
        return commentMapper.updateComment(comment);
    }

    @Override
    /**
     * 存疑：用户的评论是一个List，需要根据comment.id挨个删除comment
     */
//    @CacheEvict(key = "#userId")
    public Integer deleteCommentByUserId(Integer userId) {
        return commentMapper.deleteCommentByUserId(userId);
    }
}
