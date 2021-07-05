package com.lzc.mapper;

import com.lzc.pojo.Subscription;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 0202zc
 * @date 2021-06-17
 */
@Mapper
@Repository
public interface SubscriptionMapper {
    /**
     * @return 返回所有订阅
     */
    List<Subscription> queryAllSubscriptions();

    Subscription querySubscription(Subscription subscription);

    List<Subscription> querySubscriptionsByEmail(String email);

    List<Subscription> querySubscriptionsByUserId(Integer userId);

    Subscription querySubscriptionById(Integer id);

    Integer insertSubscription(Subscription subscription);

    Integer deleteSubscription(String email, Integer serviceId, String sendTime);

    Integer updateSubscription(String oldEmail, Integer oldServiceId, String oldSendTime, Integer newServiceId, String newSendTime, Integer allowSend);

    Integer deleteSubscriptionByUserId(Integer userId);

    Integer updateSubsState(Integer id, Integer newState);
}
