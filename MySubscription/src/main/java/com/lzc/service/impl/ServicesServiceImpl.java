package com.lzc.service.impl;

import com.lzc.mapper.ServicesMapper;
import com.lzc.pojo.ServiceDO;
import com.lzc.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liang Zhancheng
 */
@Service
@CacheConfig(cacheNames = "service")
public class ServicesServiceImpl implements ServicesService {
    private final String ID = "id";

    @Autowired
    private ServicesMapper servicesMapper;

    @Override
    public List<ServiceDO> queryServices() {
        return servicesMapper.queryServices();
    }

    @Override
    public List<ServiceDO> queryEnabledServices() {
        return servicesMapper.queryEnabledServices();
    }

    @Override
    public List<Object> queryServicesWithCrawlers(String sSearch) {
        return servicesMapper.queryServicesWithCrawlers(sSearch);
    }

    @Override
    public List<Map<Integer, Integer>> countSubscription() {
        return servicesMapper.countSubscription();
    }

    @Override
    @Cacheable(key = "#id", condition = "null ne #id and #id > 0", unless = "null eq #result")
    public ServiceDO queryServicesById(Integer id) {
        return servicesMapper.queryServicesById(id);
    }

    @Override
    public ServiceDO queryServicesByName(String name) {
        return servicesMapper.queryServicesByName(name);
    }

    @Override
    @CachePut(key = "#result.id", unless = "null eq #result")
    public ServiceDO addService(ServiceDO service) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("serviceName", service.getServiceName());
        map.put("note", service.getNote());
        map.put("enabled", service.getEnabled());
        map.put("email", service.getEmail());
        map.put("id", null);
        servicesMapper.addService(map);
        if (map.get(ID) == null) {
            return null;
        }
        return servicesMapper.queryServicesById(Integer.parseInt(map.get("id").toString()));
    }

    @Override
    @CacheEvict(key = "#service.id")
    public Integer deleteService(ServiceDO service) {
        return servicesMapper.deleteService(service);
    }

    @Override
    @CachePut(key = "#service.id", condition = "null ne #service", unless = "null eq #result")
    public ServiceDO updateService(ServiceDO service) {
        return servicesMapper.updateService(service) == 1 ? service : null;
    }

    @Override
    @CachePut(key = "#id", condition = "null ne #id and #id > 0", unless = "null eq #result")
    public ServiceDO updateServiceStatus(Integer id) {
        return servicesMapper.updateServiceStatus(id) == 1 ? servicesMapper.queryServicesById(id) : null;
    }
}
