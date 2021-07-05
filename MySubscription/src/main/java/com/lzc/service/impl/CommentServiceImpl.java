package com.lzc.service.impl;

import com.lzc.mapper.CommentMapper;
import com.lzc.mapper.UserMapper;
import com.lzc.pojo.CommentDO;
import com.lzc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
    public List<CommentDO> queryAllComments() {
        return commentMapper.queryAllComments();
    }

    @Override
    public List<CommentDO> queryCommentsByUserId(Integer userId) {
        return commentMapper.queryCommentsByUserId(userId);
    }

    @Override
    public List<CommentDO> queryCommentsByEmail(String email) {
        return commentMapper.queryCommentsByEmail(email);
    }

    @Override
    public CommentDO queryCommentById(Integer commentId) {
        return commentMapper.queryCommentById(commentId);
    }

    @Override
    public Integer addComment(CommentDO comment) {
        return commentMapper.addComment(comment);
    }

    @Override
    public CommentDO addComment(String email, String note) {
        if(email != null && email.trim().length() != 0 && note != null && note.trim().length() != 0) {
            Integer userId = userMapper.queryUserByEmail(email).getId();
            CommentDO comment = new CommentDO(userId, note);
            // 返回自动生成的id，存入comment.id中
            return commentMapper.addComment(comment) == 1 ? comment : null;
        } else {
            return null;
        }
    }

    @Override
    public Integer deleteComment(Integer commentId) {
        return commentMapper.deleteComment(commentId);
    }

    @Override
    public Integer updateComment(CommentDO comment) {
        return commentMapper.updateComment(comment);
    }

    @Override
    public Integer deleteCommentByUserId(Integer userId) {
        return commentMapper.deleteCommentByUserId(userId);
    }
}
