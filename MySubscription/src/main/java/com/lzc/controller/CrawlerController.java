package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.config.LengthConfig;
import com.lzc.pojo.ServiceDO;
import com.lzc.service.ServicesService;
import com.lzc.util.FileUtils;
import com.lzc.util.JsonUtils;
import com.lzc.pojo.CrawlerDO;
import com.lzc.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Liang Zhancheng
 */
@Slf4j
@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    CrawlerService crawlerService;

    @Autowired
    ServicesService servicesService;

    @GetMapping("/queryAllCrawlers")
    public String queryAllCrawlers(HttpServletResponse response) {
        JSONObject data = new JSONObject();
        data.put("crawlers", crawlerService.queryAllCrawlers());
        return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
    }

    @GetMapping("/queryCrawlerById/{id}")
    public String queryCrawlerById(HttpServletResponse response, @PathVariable("id") Integer id) {
        JSONObject data = new JSONObject();
        data.put("crawler", crawlerService.queryCrawlerById(id));
        return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
    }

    @GetMapping("/queryCrawlerByServiceId/{serviceId}")
    public String queryCrawlerByServiceId(HttpServletResponse response, @PathVariable("serviceId") Integer serviceId) {
        JSONObject data = new JSONObject();
        data.put("crawlers", crawlerService.queryCrawlersByServiceId(serviceId));
        return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
    }

    @PostMapping("/addCrawler")
    public String addCrawler(HttpServletResponse response, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled) {
        if (crawlerName == null || filePath == null || enabled == null) {
            JsonUtils.toJsonString(response.getStatus(), "有参数为空");
        }

        crawlerName = crawlerName.trim();
        filePath = filePath.trim();

        if (crawlerName.length() > FileUtils.FILE_NAME_LENGTH) {
            return JsonUtils.toJsonString(response.getStatus(), "文件名过长（大于45字）");
        } else if (filePath.length() > FileUtils.FILEPATH_LENGTH) {
            return JsonUtils.toJsonString(response.getStatus(), "路径过长（大于100字）");
        }
        CrawlerDO crawler = new CrawlerDO(crawlerName, filePath, enabled);
        String msg = crawlerService.addCrawler(crawler) != null ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/addCrawlerWithService")
    public String addCrawlerWithService(HttpServletResponse response, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled, @RequestParam("serviceId") Integer serviceId) {
        if (crawlerName == null || filePath == null || enabled == null || serviceId == null) {
            JsonUtils.toJsonString(response.getStatus(), "有参数为空");
        }
        crawlerName = crawlerName.trim();
        filePath = filePath.trim();
        if (crawlerName.length() > FileUtils.FILE_NAME_LENGTH) {
            return JsonUtils.toJsonString(response.getStatus(), "文件名过长（大于45字）");
        } else if (filePath.length() > FileUtils.FILEPATH_LENGTH) {
            return JsonUtils.toJsonString(response.getStatus(), "路径过长（大于100字）");
        }
        Integer res = crawlerService.addCrawlerWithService(new CrawlerDO(crawlerName, filePath, enabled), serviceId);
        if (res == -1) {
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING);
        } else {
            log.info(crawlerName + " 爬虫添加已对应服务：" + res);
            JSONObject data = new JSONObject();
            data.put("crawlerId", res);

            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
        }
    }

    /**
     * @param response    响应
     * @param serviceName 服务名称
     * @param note        备注
     * @param crawlerId   爬虫文件编号
     * @return json
     * @description 先创建服务，再关联爬虫
     */
    @PostMapping("/addServiceAfterCreateCrawler")
    public String addServiceAfterCreateCrawler(HttpServletResponse response, @RequestParam("serviceName") String serviceName, @RequestParam("note") String note, @RequestParam("crawlerId") Integer crawlerId, @RequestParam("email") String email) {
        JSONObject data = new JSONObject();
        if (serviceName == null || crawlerId == null || note == null || email == null) {
            data.put("res", "有参数为空");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        }
        serviceName = serviceName.trim();
        note = note.trim();
        if (serviceName.length() > LengthConfig.SERVICE_NAME_MAX_LENGTH) {
            data.put("res", "服务名称过长（大于45字）");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        } else if (crawlerId == null) {
            data.put("res", "爬虫id不合法");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        } else if (note.length() > LengthConfig.SERVICE_NOTE_MAX_LENGTH) {
            data.put("res", "备注过长（大于100字）");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        }
        if (servicesService.queryServicesByName(serviceName) != null) {
            data.put("res", "该服务名称已存在【" + serviceName + "】");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        }
        // 先创建服务
        ServiceDO newService = servicesService.addService(new ServiceDO(serviceName, note, email, 1));
        Integer serviceId = newService == null ? -1 : newService.getId();
        if (serviceId == -1) {
            data.put("res", "服务创建失败");
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING, data);
        } else {
            log.info("服务已添加：" + serviceId + " - " + serviceName);
            data.put("serviceId", serviceId);
            data.put("crawlerId", crawlerId);
            String msg = crawlerService.addAssociation(serviceId, crawlerId) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
            if (JsonUtils.FAIL_STRING.equals(msg)) {
                data.put("res", "服务创建失败");
            }
            return JsonUtils.toJsonString(response.getStatus(), msg, data);
        }
    }


    /**
     * @param response  响应
     * @param serviceId 服务编号
     * @param crawlerId 爬虫文件编号
     * @return
     * @description 根据已存在的服务和爬虫文件进行关联
     */
    @PostMapping("/addAssociation")
    public String addAssociation(HttpServletResponse response, @RequestParam("serviceId") Integer serviceId, @RequestParam("crawlerId") Integer crawlerId) {
        String msg = crawlerService.addAssociation(serviceId, crawlerId) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteCrawlerById")
    public String deleteCrawlerById(HttpServletResponse response, @RequestParam("id") Integer id) {
        String msg = crawlerService.deleteCrawlerById(id) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateCrawler")
    public String updateCrawler(HttpServletResponse response, @RequestParam("crawlerId") Integer crawlerId, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled) {
        CrawlerDO crawler = new CrawlerDO(crawlerName, filePath, enabled);
        crawler.setId(crawlerId);

        String msg = crawlerService.updateCrawler(crawler) != null ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        JSONObject data = new JSONObject();
        data.put("crawler", crawler);
        return JsonUtils.toJsonString(response.getStatus(), msg, data);
    }

    @PostMapping("/updateCrawlerWithService")
    public String updateCrawlerWithService(HttpServletResponse response, @RequestParam("oldCrawlerId") Integer oldCrawlerId, @RequestParam("oldServiceId") Integer oldServiceId, @RequestParam("newCrawlerId") Integer newCrawlerId, @RequestParam("newServiceId") Integer newServiceId) {
        String msg = crawlerService.updateCrawlerWithService(oldCrawlerId, oldServiceId, newCrawlerId, newServiceId) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(HttpServletResponse response, @RequestParam("file") List<MultipartFile> files) {
        JSONObject jsonObject = new JSONObject();
        if (files.isEmpty()) {
            log.info("文件为空");
            return JsonUtils.toJsonString(response.getStatus(), "上传失败，请选择文件");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = FileUtils.FILEPATH;

            if (FileUtils.isExisted(fileName)) {
                log.info("该文件名已存在：" + fileName);
                try {
                    // 扫描并同步文件和数据库
                    synchronized (this) {
                        FileUtils.scanFiles();
                        FileUtils.scanDirectory(null);
                    }
                } catch (Exception e) {
                    log.info(e.toString(), e);
                }
                return JsonUtils.toJsonString(response.getStatus(), "该文件名 \"" + fileName + "\" 已存在，请修改文件名后上传");
            }

            File dest = new File(filePath + fileName);
            try {
                // 扫描并同步文件和数据库
                FileUtils.scanFiles();
                FileUtils.scanDirectory(null);

                file.transferTo(dest);

                Integer crawlerId = crawlerService.addCrawler(new CrawlerDO(fileName, filePath + fileName, 1)).getId();
                log.info("上传成功，文件编号" + crawlerId);
                JSONObject data = new JSONObject();
                data.put("crawlerId", crawlerId);
                data.put("crawlerName", fileName);

                jsonObject = JsonUtils.toJson(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
            } catch (IOException e) {
                log.error(e.toString(), e);
                jsonObject = JsonUtils.toJson(response.getStatus(), JsonUtils.FAIL_STRING, null);
            }
        }
        return JSON.toJSONString(jsonObject);
    }
}
