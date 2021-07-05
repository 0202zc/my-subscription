package com.lzc.service;


import com.lzc.pojo.CrawlerDO;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
public interface CrawlerService {
    /**
     * 查询所有的爬虫信息
     *
     * @return 爬虫列表
     */
    List<CrawlerDO> queryAllCrawlers();

    /**
     * 根据爬虫编号查询单个爬虫信息
     *
     * @param id 爬虫编号
     * @return 该编号的爬虫信息
     */
    CrawlerDO queryCrawlerById(Integer id);

    /**
     * 使用精确查询，根据文件路径查询爬虫文件
     *
     * @param filePath 文件路径：文件夹 + 文件名
     * @return 返回等于filePath的某个文件
     */
    CrawlerDO queryCrawlerByPath(String filePath);

    /**
     * 使用模糊查询，根据文件夹查询该目录下的所有爬虫文件
     *
     * @param filePath 文件夹路径
     * @return 返回含有filePath的所有文件
     */
    List<CrawlerDO> queryCrawlersByPath(String filePath);

    /**
     * 查询该服务编号对应的爬虫
     *
     * @param serviceId 服务编号
     * @return 爬虫列表
     */
    List<CrawlerDO> queryCrawlersByServiceId(Integer serviceId);

    /**
     * 新增爬虫信息
     *
     * @param crawler 爬虫信息
     * @return 新增的爬虫
     */
    CrawlerDO addCrawler(CrawlerDO crawler);

    /**
     * 新增爬虫信息，同时关联服务
     *
     * @param crawler   爬虫信息
     * @param serviceId 服务编号
     * @return 返回新增的关联编号
     */
    Integer addCrawlerWithService(CrawlerDO crawler, Integer serviceId);

    /**
     * 关联服务和爬虫文件
     *
     * @param serviceId 服务编号
     * @param crawlerId 爬虫编号
     * @return 0：插入失败；1：插入成功
     */
    Integer addAssociation(Integer serviceId, Integer crawlerId);
    /**
     * 删除该编号的爬虫
     *
     * @param id 爬虫编号
     * @return 0：删除失败；1：删除成功
     */
    Integer deleteCrawlerById(Integer id);

    /**
     * 更新爬虫信息
     *
     * @param crawler 更新的爬虫
     * @return 返回更新后的爬虫
     */
    CrawlerDO updateCrawler(CrawlerDO crawler);

    /**
     * 更新爬虫-服务关联信息
     * @param oldCrawlerId 旧爬虫编号
     * @param oldServiceId 旧服务编号
     * @param newCrawlerId 新爬虫编号
     * @param newServiceId 新服务编号
     * @return 0：更新失败；1：更新成功
     */
    Integer updateCrawlerWithService(Integer oldCrawlerId, Integer oldServiceId, Integer newCrawlerId, Integer newServiceId);
}
