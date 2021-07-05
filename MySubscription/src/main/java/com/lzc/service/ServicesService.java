package com.lzc.service;

import com.lzc.pojo.ServiceDO;

import java.util.List;
import java.util.Map;


/**
 * @author Liang Zhancheng
 */
public interface ServicesService {
    /**
     * 查询所有服务
     *
     * @return 返回服务列表
     */
    List<ServiceDO> queryServices();

    /**
     * 查询启用的服务
     *
     * @return 启用服务列表
     */
    List<ServiceDO> queryEnabledServices();

    /**
     * 搜索爬虫——服务信息
     *
     * @param sSearch 搜索条件（serviceName 或 note）
     * @return 符合条件的爬虫——服务信息
     */
    List<Object> queryServicesWithCrawlers(String sSearch);

    /**
     * 查询每个服务的订阅数量
     *
     * @return key-val(serviceId-count)
     */
    List<Map<Integer, Integer>> countSubscription();

    /**
     * 查询该编号的服务
     *
     * @param id 服务编号
     * @return 服务信息
     */
    ServiceDO queryServicesById(Integer id);

    /**
     * 查询该名称的服务
     *
     * @param name 服务名称
     * @return 服务信息
     */
    ServiceDO queryServicesByName(String name);

    /**
     * 添加服务
     *
     * @param service 要添加的服务信息
     * @return 返回新增的服务
     */
    ServiceDO addService(ServiceDO service);

    /**
     * 删除服务
     *
     * @param service 要删除的服务信息
     * @return 0：删除失败；1：删除成功
     */
    Integer deleteService(ServiceDO service);

    /**
     * 更新服务
     *
     * @param service 更新的服务信息
     * @return 返回更新后的服务
     */
    ServiceDO updateService(ServiceDO service);

    /**
     * 切换服务状态：0-禁用，1-启用
     *
     * @param id 服务编号
     * @return 返回更新后的服务
     */
    ServiceDO updateServiceStatus(Integer id);
}
