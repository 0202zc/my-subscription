package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.config.LengthConfig;
import com.lzc.pojo.Services;
import com.lzc.service.ServicesService;
import com.lzc.util.FileUtil;
import com.lzc.util.JsonUtil;
import com.lzc.pojo.Crawler;
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
        return JsonUtil.toJsonString(response.getStatus(), "success", data);
    }

    @GetMapping("/queryCrawlerById/{id}")
    public String queryCrawlerById(HttpServletResponse response, @PathVariable("id") Integer id) {
        JSONObject data = new JSONObject();
        data.put("crawler", crawlerService.queryCrawlerById(id));
        return JsonUtil.toJsonString(response.getStatus(), "success", data);
    }

    @GetMapping("/queryCrawlerByServiceId/{serviceId}")
    public String queryCrawlerByServiceId(HttpServletResponse response, @PathVariable("serviceId") Integer serviceId) {
        JSONObject data = new JSONObject();
        data.put("crawlers", crawlerService.queryCrawlersByServiceId(serviceId));
        return JsonUtil.toJsonString(response.getStatus(), "success", data);
    }

    @PostMapping("/addCrawler")
    public String addCrawler(HttpServletResponse response, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled) {
        if (crawlerName == null || filePath == null || enabled == null) {
            JsonUtil.toJsonString(response.getStatus(), "有参数为空");
        }

        crawlerName = crawlerName.trim();
        filePath = filePath.trim();

        if (crawlerName.length() > FileUtil.FILE_NAME_LENGTH) {
            return JsonUtil.toJsonString(response.getStatus(), "文件名过长（大于45字）");
        } else if (filePath.length() > FileUtil.FILEPATH_LENGTH) {
            return JsonUtil.toJsonString(response.getStatus(), "路径过长（大于100字）");
        }
        Crawler crawler = new Crawler(crawlerName, filePath, enabled);
        String msg = crawlerService.addCrawler(crawler) != null ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/addCrawlerWithService")
    public String addCrawlerWithService(HttpServletResponse response, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled, @RequestParam("serviceId") Integer serviceId) {
        if (crawlerName == null || filePath == null || enabled == null || serviceId == null) {
            JsonUtil.toJsonString(response.getStatus(), "有参数为空");
        }
        crawlerName = crawlerName.trim();
        filePath = filePath.trim();
        if (crawlerName.length() > FileUtil.FILE_NAME_LENGTH) {
            return JsonUtil.toJsonString(response.getStatus(), "文件名过长（大于45字）");
        } else if (filePath.length() > FileUtil.FILEPATH_LENGTH) {
            return JsonUtil.toJsonString(response.getStatus(), "路径过长（大于100字）");
        }
        Integer res = crawlerService.addCrawlerWithService(new Crawler(crawlerName, filePath, enabled), serviceId);
        if (res == -1) {
            return JsonUtil.toJsonString(response.getStatus(), "fail");
        } else {
            log.info(crawlerName + " 爬虫添加已对应服务：" + res);
            JSONObject data = new JSONObject();
            data.put("crawlerId", res);

            return JsonUtil.toJsonString(response.getStatus(), "success", data);
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
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        }
        serviceName = serviceName.trim();
        note = note.trim();
        if (serviceName.length() > LengthConfig.SERVICE_NAME_MAX_LENGTH) {
            data.put("res", "服务名称过长（大于45字）");
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        } else if (crawlerId == null) {
            data.put("res", "爬虫id不合法");
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        } else if (note.length() > LengthConfig.SERVICE_NOTE_MAX_LENGTH) {
            data.put("res", "备注过长（大于100字）");
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        }
        if (servicesService.queryServicesByName(serviceName) != null) {
            data.put("res", "该服务名称已存在【" + serviceName + "】");
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        }
        // 先创建服务
        Services newService = servicesService.addService(new Services(serviceName, note, email, 1));
        Integer serviceId = newService == null ? -1 : newService.getId();
        if (serviceId == -1) {
            data.put("res", "服务创建失败");
            return JsonUtil.toJsonString(response.getStatus(), "fail", data);
        } else {
            log.info("服务已添加：" + serviceId + " - " + serviceName);
            data.put("serviceId", serviceId);
            data.put("crawlerId", crawlerId);
            String msg = crawlerService.addAssociation(serviceId, crawlerId) == 1 ? "success" : "fail";
            if ("fail".equals(msg)) {
                data.put("res", "服务创建失败");
            }
            return JsonUtil.toJsonString(response.getStatus(), msg, data);
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
        String msg = crawlerService.addAssociation(serviceId, crawlerId) == 1 ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteCrawlerById")
    public String deleteCrawlerById(HttpServletResponse response, @RequestParam("id") Integer id) {
        String msg = crawlerService.deleteCrawlerById(id) == 1 ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateCrawler")
    public String updateCrawler(HttpServletResponse response, @RequestParam("crawlerId") Integer crawlerId, @RequestParam("crawlerName") String crawlerName, @RequestParam("filePath") String filePath, @RequestParam("enabled") Integer enabled) {
        Crawler crawler = new Crawler(crawlerName, filePath, enabled);
        crawler.setId(crawlerId);

        String msg = crawlerService.updateCrawler(crawler) != null ? "success" : "fail";
        JSONObject data = new JSONObject();
        data.put("crawler", crawler);
        return JsonUtil.toJsonString(response.getStatus(), msg, data);
    }

    @PostMapping("/updateCrawlerWithService")
    public String updateCrawlerWithService(HttpServletResponse response, @RequestParam("oldCrawlerId") Integer oldCrawlerId, @RequestParam("oldServiceId") Integer oldServiceId, @RequestParam("newCrawlerId") Integer newCrawlerId, @RequestParam("newServiceId") Integer newServiceId) {
        String msg = crawlerService.updateCrawlerWithService(oldCrawlerId, oldServiceId, newCrawlerId, newServiceId) == 1 ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(HttpServletResponse response, @RequestParam("file") List<MultipartFile> files) {
        JSONObject jsonObject = new JSONObject();
        if (files.isEmpty()) {
            log.info("文件为空");
            return JsonUtil.toJsonString(response.getStatus(), "上传失败，请选择文件");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = FileUtil.FILEPATH;

            if (FileUtil.isExisted(fileName)) {
                log.info("该文件名已存在：" + fileName);
                try {
                    // 扫描并同步文件和数据库
                    synchronized (this) {
                        FileUtil.scanFiles();
                        FileUtil.scanDirectory(null);
                    }
                } catch (Exception e) {
                    log.info(e.toString(), e);
                }
                return JsonUtil.toJsonString(response.getStatus(), "该文件名 \"" + fileName + "\" 已存在，请修改文件名后上传");
            }

            File dest = new File(filePath + fileName);
            try {
                // 扫描并同步文件和数据库
                FileUtil.scanFiles();
                FileUtil.scanDirectory(null);

                file.transferTo(dest);

                Integer crawlerId = crawlerService.addCrawler(new Crawler(fileName, filePath + fileName, 1)).getId();
                log.info("上传成功，文件编号" + crawlerId);
                JSONObject data = new JSONObject();
                data.put("crawlerId", crawlerId);
                data.put("crawlerName", fileName);

                jsonObject = JsonUtil.toJson(response.getStatus(), "success", data);
            } catch (IOException e) {
                log.error(e.toString(), e);
                jsonObject = JsonUtil.toJson(response.getStatus(), "fail", null);
            }
        }
        return JSON.toJSONString(jsonObject);
    }
}
