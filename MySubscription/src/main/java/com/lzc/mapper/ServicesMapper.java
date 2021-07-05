package com.lzc.mapper;

import com.lzc.pojo.ServiceDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Liang Zhancheng
 */
@Mapper
@Repository
public interface ServicesMapper {
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
     * @param serviceName 服务名称
     * @return 服务信息
     */
    ServiceDO queryServicesByName(String serviceName);

    /**
     * 添加服务
     *
     * @param map 服务映射表
     * @return 0：插入失败；1：插入成功
     */
    Integer addService(Map<String, Object> map);

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
     * @return 0：更新失败；1：更新成功
     */
    Integer updateService(ServiceDO service);

    /**
     * 切换服务状态：0-禁用，1-启用
     * @param id 服务编号
     * @return 0：更新失败；1：更新成功
     */
    Integer updateServiceStatus(Integer id);
}
