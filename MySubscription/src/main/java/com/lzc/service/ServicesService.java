package com.lzc.service;

import com.lzc.mapper.ServicesMapper;
import com.lzc.pojo.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author Liang Zhancheng
 */
public interface ServicesService {
    List<Services> queryServices();

    List<Services> queryEnabledServices();

    List<Object> queryServicesWithCrawlers(String sSearch);

    List<Map<Integer, Integer>> querySubsCount();

    Services queryServicesById(Integer id);

    Services queryServicesByName(String name);

    Services addService(Services service);

    Integer deleteService(Services service);

    Services updateService(Services service);

    Services updateServiceStatus(Integer id);
}
