package com.lzc.service.impl;

import com.lzc.mapper.SubscriptionMapper;
import com.lzc.mapper.UserMapper;
import com.lzc.pojo.Subscription;
import com.lzc.pojo.User;
import com.lzc.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Liang Zhancheng
 */
@Service
@CacheConfig(cacheNames = "subscribe")
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Subscription> queryAllSubscriptions() {
        return subscriptionMapper.queryAllSubscriptions();
    }

    @Override
//    @Cacheable(key = "#subscription.id", condition = "null ne #subscription and null ne #subscription.id and #subscription.id > 0", unless = "null eq #result")
    public Subscription querySubscription(Subscription subscription) {
        return subscriptionMapper.querySubscription(subscription);
    }

    @Override
//    @Cacheable(key = "#email", unless = "null eq #result")
    public List<Subscription> querySubscriptionsByEmail(String email) {
        return subscriptionMapper.querySubscriptionsByEmail(email);
    }

    @Override
//    @Cacheable(value = "user_subs", key = "#userId", unless = "null eq #result")
    public List<Subscription> querySubscriptionsByUserId(Integer userId) {
        return subscriptionMapper.querySubscriptionsByUserId(userId);
    }

    @Override
//    @CachePut(key = "#subscription.id", unless = "null eq #result and null eq subscription.id and 0 eq #result")
    public Integer insertSubscription(Subscription subscription, String userName, String email) {
        // 查询该邮箱是否已经注册
        User user = userMapper.queryUserByEmail(email);
        if (user == null) {
            // 没注册就注册
            user = new User();
            user.setUserName(userName);
            user.setEmail(email);
            user.setRole(1);
            user.setIsAllowed(subscription.getAllowSend());
            userMapper.addUser(user);
        } else {
            // 查询是否存在该订阅
            subscription.setUserId(user.getId());
            Subscription validSub = subscriptionMapper.querySubscription(subscription);
            if (validSub != null) {
                subscription.setId(validSub.getId());
                if (validSub.getAllowSend().equals(subscription.getAllowSend())) {
                    return 0;
                } else {
                    // 注册了就更新信息
                    user.setUserName(userName);
                    userMapper.updateUser(user);
                    System.out.println("用户 " + user.getUserName() + " 更新订阅成功");
                    return subscriptionMapper.updateSubscription(user.getEmail(), validSub.getServiceId(), validSub.getSendTime(), subscription.getServiceId(), subscription.getSendTime(), subscription.getAllowSend());
                }
            }
            // 注册了就更新信息
            user.setUserName(userName);
            userMapper.updateUser(user);
            System.out.println("用户 " + user.getUserName() + " 更新订阅成功");
        }
        subscription.setUserId(userMapper.queryUserByEmail(email).getId());
        return subscriptionMapper.insertSubscription(subscription);
    }

    @Override
//    @CacheEvict(key = "#result", condition = "0 ne #result")
    public Integer deleteSubscription(String email, Integer serviceId, String sendTime) {
        User user = userMapper.queryUserByEmail(email);
        if (user == null) {
            return 0;
        }
        Subscription subs = subscriptionMapper.querySubscription(new Subscription(user.getId(), serviceId, sendTime));
        if (subs == null) {
            return 0;
        } else {
            return subscriptionMapper.deleteSubscription(email, serviceId, sendTime) == 0 ? 0 : subs.getId();
        }
    }

    @Override
//    @CachePut(key = "#result", unless = "0 eq #result")
    public Integer updateSubscription(String oldEmail, Integer oldServiceId, String oldSendTime, Integer newServiceId, String newSendTime, Integer allowSend, String newEmail) {
        User user = userMapper.queryUserByEmail(oldEmail);
        if (user == null || oldEmail == null || oldEmail.trim().length() == 0) {
            return 0;
        }
        Subscription subs = subscriptionMapper.querySubscription(new Subscription(user.getId(), oldServiceId, oldSendTime));
        // 如果用户更改了邮箱
        if (!oldEmail.equals(newEmail)) {
            userMapper.updateUserEmail(oldEmail, newEmail);
        }

        return subscriptionMapper.updateSubscription(oldEmail, oldServiceId, oldSendTime, newServiceId, newSendTime, allowSend) == 0 ? 0 : subs.getId();
    }

    @Override
//    @CachePut(key = "#id", unless = "0 eq #result")
    public Integer updateSubsState(Integer id) {
        Integer newState = subscriptionMapper.querySubscriptionById(id).getAllowSend() == 0 ? 1 : 0;
        return subscriptionMapper.updateSubsState(id, newState);
    }

    @Override
//    @CacheEvict(value = "user_subs", key = "#userId")
    public Integer deleteSubscriptionByUserId(Integer userId) {
        return subscriptionMapper.deleteSubscriptionByUserId(userId);
    }

    /**
     * SimpleDateFormat 是线程不安全的类，一般不要定义为static变量
     */
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
}
