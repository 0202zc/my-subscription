package com.lzc.service;


import com.lzc.pojo.Crawler;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
public interface CrawlerService {
    List<Crawler> queryAllCrawlers();

    Crawler queryCrawlerById(Integer id);

    /**
     * @description 使用精确查询，根据文件路径查询爬虫文件
     * @param filePath 文件路径：文件夹 + 文件名
     * @return 返回等于filePath的某个文件
     */
    Crawler queryCrawlerByPath(String filePath);

    /**
     * @description 使用模糊查询，根据文件夹查询该目录下的所有爬虫文件
     * @param filePath 文件夹路径
     * @return 返回含有filePath的所有文件
     */
    List<Crawler> queryCrawlersByPath(String filePath);

    List<Crawler> queryCrawlersByServiceId(Integer serviceId);

    Crawler addCrawler(Crawler crawler);

    Integer addCrawlerWithService(Crawler crawler, Integer serviceId);

    Integer addAssociation(Integer serviceId, Integer crawlerId);

    Integer deleteCrawlerById(Integer id);

    Crawler updateCrawler(Crawler crawler);

    Integer updateCrawlerWithService(Integer oldCrawlerId, Integer oldServiceId, Integer newCrawlerId, Integer newServiceId);
}
