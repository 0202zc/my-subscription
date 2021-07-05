package com.lzc.service;

import com.lzc.pojo.UserDO;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
public interface UserService {
    /**
     * 查询符合条件的用户列表
     *
     * @param user 属性内容为空，则为全搜索；否则为条件搜索
     * @return 符合条件的用户列表
     */
    List<UserDO> queryUsers(UserDO user);

    /**
     * 查询允许发送的邮件列表
     *
     * @return 返回允许发送的邮件列表
     */
    List<String> queryAllowEmails();

    /**
     * 利用包装类查询 User，可设置的为Unique键：id 和 email
     *
     * @param user 用户对象
     * @return User
     */
    UserDO queryUser(UserDO user);

    /**
     * 根据编号查询用户
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    UserDO queryUserById(Integer userId);

    /**
     * 根据邮件地址查询用户
     *
     * @param email 邮件地址
     * @return 用户信息
     */
    UserDO queryUserByEmail(String email);

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 0：添加失败，1：添加成功
     */
    UserDO addUser(UserDO user);

    /**
     * 删除用户
     *
     * @param user 用户信息
     * @return 0：删除失败，1：删除成功
     */
    Integer deleteUser(UserDO user);

    /**
     * 根据邮件地址删除用户
     *
     * @param email 邮件地址
     * @param id 用户编号
     * @return 0：删除失败，1：删除成功
     */
    Integer deleteUserByEmail(String email, Integer id);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 0：更新失败，1：更新成功
     */
    UserDO updateUser(UserDO user);

    /**
     * 更新用户邮件地址
     *
     * @param oldEmail 旧邮件地址
     * @param newEmail 新邮件地址
     * @return 0：更新失败，1：更新成功
     */
    UserDO updateUserEmail(String oldEmail, String newEmail);
}
