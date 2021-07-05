package com.lzc.service;

import com.lzc.pojo.Subscription;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> queryAllSubscriptions();

    Subscription querySubscription(Subscription subscription);

    List<Subscription> querySubscriptionsByEmail(String email);

    List<Subscription> querySubscriptionsByUserId(Integer userId);

    Integer insertSubscription(Subscription subscription, String userName, String email);

    Integer deleteSubscription(String email, Integer serviceId, String sendTime);

    Integer updateSubscription(String oldEmail, Integer oldServiceId, String oldSendTime, Integer newServiceId, String newSendTime, Integer allowSend, String newEmail);

    Integer updateSubsState(Integer id);

    Integer deleteSubscriptionByUserId(Integer userId);
}
