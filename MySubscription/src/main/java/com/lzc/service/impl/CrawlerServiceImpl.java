package com.lzc.service.impl;

import com.lzc.mapper.CrawlerMapper;
import com.lzc.pojo.CrawlerDO;
import com.lzc.service.CrawlerService;
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
@CacheConfig(cacheNames = "crawler")
public class CrawlerServiceImpl implements CrawlerService {
    private final String ID = "id";

    @Autowired
    private CrawlerMapper crawlerMapper;

    @Override
    public List<CrawlerDO> queryAllCrawlers() {
        return crawlerMapper.queryAllCrawlers();
    }

    @Override
    @Cacheable(key = "#id", condition = "null eq #id and #id > 0", unless = "null eq #result")
    public CrawlerDO queryCrawlerById(Integer id) {
        return crawlerMapper.queryCrawlerById(id);
    }

    @Override
    public CrawlerDO queryCrawlerByPath(String filePath) {
        return crawlerMapper.queryCrawlerByPath(filePath);
    }

    @Override
    public List<CrawlerDO> queryCrawlersByPath(String filePath) {
        return crawlerMapper.queryCrawlersByPath(filePath);
    }

    @Override
    public List<CrawlerDO> queryCrawlersByServiceId(Integer serviceId) {
        return crawlerMapper.queryCrawlersByServiceId(serviceId);
    }

    @Override
    @CachePut(key = "#result.id", unless = "null eq #result")
    public CrawlerDO addCrawler(CrawlerDO crawler) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("crawlerName", crawler.getCrawlerName());
        map.put("filePath", crawler.getFilePath());
        map.put("enabled", crawler.getEnabled());
        map.put("id", null);
        crawlerMapper.addCrawler(map);
        if (map.get(ID) == null) {
            return null;
        }
        crawler.setId(Integer.parseInt(map.get("id").toString()));
        return crawler;
    }

    @Override
    public Integer addCrawlerWithService(CrawlerDO crawler, Integer serviceId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("crawlerName", crawler.getCrawlerName());
        map.put("filePath", crawler.getFilePath());
        map.put("enabled", crawler.getEnabled());
        map.put("serviceId", serviceId);
        map.put("scId", null);
        crawlerMapper.addCrawlerWithService(map);
        return map.get("scId") == null ? -1 : Integer.parseInt(map.get("scId").toString());
    }

    @Override
    public Integer addAssociation(Integer serviceId, Integer crawlerId) {
        return crawlerMapper.addAssociation(serviceId, crawlerId);
    }

    @Override
    @CacheEvict(key = "#id")
    public Integer deleteCrawlerById(Integer id) {
        return crawlerMapper.deleteCrawlerById(id);
    }

    @Override
    @CachePut(key = "#crawler.id", unless = "0 eq #result")
    public CrawlerDO updateCrawler(CrawlerDO crawler) {
        return crawlerMapper.updateCrawler(crawler) == 1 ? crawler : null;
    }

    @Override
    public Integer updateCrawlerWithService(Integer oldCrawlerId, Integer oldServiceId, Integer newCrawlerId, Integer newServiceId) {
        Integer id = crawlerMapper.queryServiceCrawlerIdByServiceAndCrawlerId(oldServiceId, oldCrawlerId);
        return crawlerMapper.updateCrawlerWithService(newCrawlerId, newServiceId, id);
    }
}
