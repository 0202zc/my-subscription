package com.lzc.mapper;

import com.lzc.pojo.CrawlerDO;
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
     * 根据文件路径查询爬虫
     *
     * @param filePath 文件路径
     * @return 该文件路径的爬虫信息
     */
    CrawlerDO queryCrawlerByPath(String filePath);

    /**
     * 查询该文件夹下的所有爬虫信息
     *
     * @param filePath 文件夹路径
     * @return 该文件夹下的爬虫信息列表
     */
    List<CrawlerDO> queryCrawlersByPath(String filePath);

    /**
     * 根据服务和爬虫文件编号查询关联表service_crawler中的编号
     *
     * @param serviceId 服务编号
     * @param crawlerId 爬虫编号
     * @return 关联的编号
     */
    Integer queryServiceCrawlerIdByServiceAndCrawlerId(Integer serviceId, Integer crawlerId);

    /**
     * 根据服务编号查询对应爬虫文件信息
     *
     * @param serviceId 服务编号
     * @return 对应该服务的爬虫信息列表
     */
    List<CrawlerDO> queryCrawlersByServiceId(Integer serviceId);

    /**
     * 新增爬虫信息
     *
     * @param map 爬虫信息映射表
     * @return 0：插入失败；1：插入成功
     */
    Integer addCrawler(Map<String, Object> map);

    /**
     * 新增带服务编号的爬虫信息
     *
     * @param map 带服务编号的爬虫信息映射表
     * @return 0：插入失败；1：插入成功
     */
    Integer addCrawlerWithService(Map<String, Object> map);

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
     * @return 0：更新失败；1：更新成功
     */
    Integer updateCrawler(CrawlerDO crawler);

    /**
     * 修改爬虫-服务关联信息
     *
     * @param crawlerId 爬虫编号
     * @param serviceId 服务编号
     * @param id        关联编号
     * @return 0：更新失败；1：更新成功
     */
    Integer updateCrawlerWithService(Integer crawlerId, Integer serviceId, Integer id);
}
