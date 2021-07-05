package com.lzc.service;

import com.lzc.pojo.CommentDO;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
public interface CommentService {
    /**
     * 查询所有的评论
     * @return Comment列表
     */
    List<CommentDO> queryAllComments();

    /**
     * 根据用户编号查询评论
     * @param userId 用户编号
     * @return Comment列表
     */
    List<CommentDO> queryCommentsByUserId(Integer userId);

    /**
     * 根据邮箱查询评论
     * @param email 邮箱
     * @return Comment列表
     */
    List<CommentDO> queryCommentsByEmail(String email);

    /**
     * 根据评论编号查询评论
     * @param commentId 评论编号
     * @return Comment
     */
    CommentDO queryCommentById(Integer commentId);

    /**
     * 新增评论
     * @param comment 新增的评论
     * @return 0：添加失败，1：添加成功
     */
    Integer addComment(CommentDO comment);

    /**
     * 仅使用 email 和 note 添加评论
     * @param email 邮箱
     * @param note 备注
     * @return 新增的评论
     */
    CommentDO addComment(String email, String note);

    /**
     * 根据评论编号删除评论
     * @param commentId 评论编号
     * @return 0：删除失败，1：删除成功
     */
    Integer deleteComment(Integer commentId);

    /**
     * 更新评论
     * @param comment 更改的comment信息
     * @return 0：更新失败，1：更新成功
     */
    Integer updateComment(CommentDO comment);

    /**
     * 删除该用户的所有评论
     * @param userId 用户编号
     * @return 0：删除失败，其他：删除成功
     */
    Integer deleteCommentByUserId(Integer userId);
}
