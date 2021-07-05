package com.lzc.service.impl;

import com.lzc.mapper.CommentMapper;
import com.lzc.mapper.SubscriptionMapper;
import com.lzc.mapper.UserMapper;
import com.lzc.pojo.User;
import com.lzc.service.UserService;
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
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<User> queryUsers(User user) {
        return userMapper.queryUsers(user);
    }

    @Override
    public List<String> queryAllowEmails() {
        return userMapper.queryAllowEmails();
    }

    @Override
    @Cacheable(key = "#user.id", condition = "null ne #user.id", unless = "null eq #result")
    public User queryUser(User user) {
        if (user.getId() == null && user.getEmail() == null) {
            return null;
        }
        System.out.println("database");
        return userMapper.queryUser(user);
    }

    /**
     * Cacheable在redis下有问题：java.lang.Integer cannot cast to com.pojo.User
     *
     * @param userId 用户编号
     * @return User
     */
    @Override
    @Cacheable(key = "#userId", unless = "null eq #result")
    public User queryUserById(Integer userId) {
        System.out.println("database");
        return userMapper.queryUserById(userId);
    }

    @Override
//    @Cacheable(key = "#email", unless = "null eq #result")
    public User queryUserByEmail(String email) {
        return userMapper.queryUserByEmail(email);
    }

    @Override
    @CachePut(key = "#user.id")
    public User addUser(User user) {
        return userMapper.addUser(user) == 1 ? user : null;
    }

    @Override
    @CacheEvict(key = "#user.id")
    public Integer deleteUser(User user) {
        commentMapper.deleteCommentByUserId(user.getId());
        subscriptionMapper.deleteSubscriptionByUserId(user.getId());
        return userMapper.deleteUser(user);
    }

    @Override
    @CacheEvict(key = "#id")
    public Integer deleteUserByEmail(String email, Integer id) {
        User user = userMapper.queryUserByEmail(email);
        commentMapper.deleteCommentByUserId(user.getId());  // 删除该用户的评论
        subscriptionMapper.deleteSubscriptionByUserId(user.getId());    // 删除该用户的订阅
        return userMapper.deleteUserByEmail(email); // 删除该用户
    }

    @Override
    @CachePut(key = "#user.id", unless = "null eq #result")
    public User updateUser(User user) {
        return userMapper.updateUser(user) == 1 ? user : null;
    }

    @Override
    @CachePut(key = "#result.id", unless = "null eq #result")
    public User updateUserEmail(String oldEmail, String newEmail) {
        userMapper.updateUserEmail(oldEmail, newEmail);
        return userMapper.queryUserByEmail(newEmail);
    }
}
