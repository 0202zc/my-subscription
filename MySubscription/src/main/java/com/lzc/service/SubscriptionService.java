package com.lzc.service;

import com.lzc.pojo.SubscriptionDO;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
public interface SubscriptionService {
    /**
     * 查询所有订阅信息
     *
     * @return 返回所有订阅
     */
    List<SubscriptionDO> queryAllSubscriptions();

    /**
     * 根据信息动态查询订阅
     *
     * @param subscription 待查询的订阅
     * @return 返回符合特定信息的某个订阅
     */
    SubscriptionDO querySubscription(SubscriptionDO subscription);

    /**
     * 查询该邮件地址下的所有订阅
     *
     * @param email 订阅的邮件地址
     * @return 返回符合信息的订阅列表
     */
    List<SubscriptionDO> querySubscriptionsByEmail(String email);

    /**
     * 查询属于该用户编号的所有订阅
     *
     * @param userId 用户编号
     * @return 符合信息的订阅列表
     */
    List<SubscriptionDO> querySubscriptionsByUserId(Integer userId);

    /**
     * 新增订阅信息
     *
     * @param subscription 订阅信息
     * @param userName     用户名
     * @param email        邮件地址
     * @return 0：插入失败；1：插入成功
     */
    Integer insertSubscription(SubscriptionDO subscription, String userName, String email);

    /**
     * 删除符合条件的订阅
     *
     * @param email     邮件地址
     * @param serviceId 服务编号
     * @param sendTime  发送时间
     * @return 0：删除失败；1：删除成功
     */
    Integer deleteSubscription(String email, Integer serviceId, String sendTime);

    /**
     * 更新订阅信息
     *
     * @param oldEmail     旧邮件地址
     * @param oldServiceId 旧服务编号
     * @param oldSendTime  旧发送时间
     * @param newServiceId 新服务编号
     * @param newSendTime  新发送时间
     * @param allowSend    发送权限 0：禁止，1：允许
     * @param newEmail     新邮件地址
     * @return 0：更新失败；1：更新成功
     */
    Integer updateSubscription(String oldEmail, Integer oldServiceId, String oldSendTime, Integer newServiceId, String newSendTime, Integer allowSend, String newEmail);

    /**
     * 切换订阅状态
     *
     * @param id 订阅编号
     * @return 0：更新失败；1：更新成功
     */
    Integer updateSubsState(Integer id);

    /**
     * 删除该用户编号下的所有订阅
     *
     * @param userId 用户编号
     * @return 0：删除失败；其他：删除成功
     */
    Integer deleteSubscriptionByUserId(Integer userId);
}
