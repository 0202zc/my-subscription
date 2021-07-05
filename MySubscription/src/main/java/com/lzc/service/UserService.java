package com.lzc.service;

import com.lzc.pojo.User;

import java.util.List;

public interface UserService {
    List<User> queryUsers(User user);

    /**
     * @return 返回允许发送的邮件列表
     */
    List<String> queryAllowEmails();

    User queryUser(User user);

    User queryUserById(Integer userId);

    User queryUserByEmail(String email);

    User addUser(User user);

    Integer deleteUser(User user);

    Integer deleteUserByEmail(String email, Integer id);

    User updateUser(User user);

    User updateUserEmail(String oldEmail, String newEmail);
}
