package com.lzc.mapper;

import com.lzc.pojo.Crawler;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Liang Zhancheng
 */
@Mapper
@Repository
public interface CrawlerMapper {
    List<Crawler> queryAllCrawlers();

    Crawler queryCrawlerById(Integer id);

    Crawler queryCrawlerByPath(String filePath);

    List<Crawler> queryCrawlersByPath(String filePath);

    Integer queryServiceCrawlerIdByServiceAndCrawlerId(Integer serviceId, Integer crawlerId);

    List<Crawler> queryCrawlersByServiceId(Integer serviceId);

    Integer addCrawler(Map<String, Object> map);

    Integer addCrawlerWithService(Map<String, Object> map);

    Integer addAssociation(Integer serviceId, Integer crawlerId);

    Integer deleteCrawlerById(Integer id);

    Integer updateCrawler(Crawler crawler);

    Integer updateCrawlerWithService(Integer crawlerId, Integer serviceId, Integer id);
}
