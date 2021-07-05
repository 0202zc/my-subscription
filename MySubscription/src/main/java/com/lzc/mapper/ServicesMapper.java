package com.lzc.mapper;

import com.lzc.pojo.Services;
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
     * @return 返回服务列表
     */
    List<Services> queryServices();

    List<Services> queryEnabledServices();

    List<Object> queryServicesWithCrawlers(String sSearch);

    /**
     * @return key-val(serviceId-count)
     * @description 查询每个服务的订阅数量
     */
    List<Map<Integer, Integer>> querySubsCount();

    Services queryServicesById(Integer id);

    Services queryServicesByName(String serviceName);

    Integer addService(Map<String, Object> map);

    Integer deleteService(Services service);

    Integer updateService(Services service);

    Integer updateServiceStatus(Integer id);
}
