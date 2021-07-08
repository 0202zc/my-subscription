package com.lzc.service.impl;

import com.lzc.mapper.CommentMapper;
import com.lzc.mapper.SubscriptionMapper;
import com.lzc.mapper.UserMapper;
import com.lzc.pojo.UserDO;
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
    public List<UserDO> queryUsers(UserDO user) {
        return userMapper.queryUsers(user);
    }

    @Override
    public List<String> queryAllowEmails() {
        return userMapper.queryAllowEmails();
    }

    @Override
    @Cacheable(key = "#user.id", condition = "null ne #user.id", unless = "null eq #result")
    public UserDO queryUser(UserDO user) {
        if (user.getId() == null && user.getEmail() == null) {
            return null;
        }
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
    public UserDO queryUserById(Integer userId) {
        return userMapper.queryUserById(userId);
    }

    @Override
    public UserDO queryUserByEmail(String email) {
        return userMapper.queryUserByEmail(email);
    }

    @Override
    @CachePut(key = "#user.id")
    public UserDO addUser(UserDO user) {
        return userMapper.addUser(user) == 1 ? user : null;
    }

    @Override
    @CacheEvict(key = "#user.id")
    public Integer deleteUser(UserDO user) {
        commentMapper.deleteCommentByUserId(user.getId());
        subscriptionMapper.deleteSubscriptionByUserId(user.getId());
        return userMapper.deleteUser(user);
    }

    @Override
    @CacheEvict(key = "#id")
    public Integer deleteUserByEmail(String email, Integer id) {
        UserDO user = userMapper.queryUserByEmail(email);
        // 删除该用户的评论
        commentMapper.deleteCommentByUserId(user.getId());
        // 删除该用户的订阅
        subscriptionMapper.deleteSubscriptionByUserId(user.getId());
        // 删除该用户
        return userMapper.deleteUserByEmail(email);
    }

    @Override
    @CachePut(key = "#user.id", unless = "null eq #result")
    public UserDO updateUser(UserDO user) {
        return userMapper.updateUser(user) == 1 ? user : null;
    }

    @Override
    @CachePut(key = "#result.id", unless = "null eq #result")
    public UserDO updateUserEmail(String oldEmail, String newEmail) {
        userMapper.updateUserEmail(oldEmail, newEmail);
        return userMapper.queryUserByEmail(newEmail);
    }
}
