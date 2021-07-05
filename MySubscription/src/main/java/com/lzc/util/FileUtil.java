package com.lzc.util;

import com.lzc.pojo.Crawler;
import com.lzc.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liang Zhancheng
 * @date 2021/6/27 21:18:40
 * @description 文件工具类
 */
@Component
public final class FileUtil {

    @Autowired
    private CrawlerService crawlerService;
    private static FileUtil fileUtil;

    public static final Integer FILE_NAME_LENGTH = 45;
    public static final Integer FILEPATH_LENGTH = 100;

    @PostConstruct
    public void init() {
        fileUtil = this;
        fileUtil.crawlerService = this.crawlerService;
    }

    /**
     * @description 上传文件的存储路径
     * 服务器路径 /usr/local/Python/customize/
     * 测试路径 C:/Users/Liang Zhancheng/Desktop/files/
     */
    public static final String FILEPATH = "C:/Users/Liang Zhancheng/Desktop/files/";

    /**
     * @param fileName 文件名
     * @return Boolean
     * @description 仅根据文件名判断文件是否存在，路径使用默认 FILEPATH
     */
    public static Boolean isExisted(String fileName) {
        File file = new File(FILEPATH + fileName);
        return file.exists();
    }

    /**
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return Boolean
     * @description 自定文件路径，判断文件是否存在
     */
    public static Boolean isExisted(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        return file.exists();
    }

    /**
     * @author Liang Zhancheng
     * @date 2021/6/27 21:47
     * @description 扫描文件并同步数据库删除失效的文件，只扫描用户自定义文件夹
     */
    public static void scanFiles() {
        List<Crawler> crawlers = fileUtil.crawlerService.queryAllCrawlers();
        for (Crawler crawler : crawlers) {
            if (crawler.getFileType() == 0) {
                continue;
            }
            String crawlerName = crawler.getCrawlerName();
            File file = new File(FILEPATH + crawlerName);
//            File file = new File(crawler.getFilePath());
            if (!file.exists()) {
                fileUtil.crawlerService.deleteCrawlerById(crawler.getId());
            }
        }
    }

    /**
     * @param path 若不指定路径，则使用默认路径
     * @description 扫描文件夹并添加文件到数据库中
     */
    public static void scanDirectory(String path) {
        if (path == null) {
            path = FILEPATH;
        }
        File file = new File(path);
//        if (!file.exists()) {
//            throw new FileNotFoundException("文件路径不存在:" + path);
//        } else if (!file.isDirectory()) {
//            throw new FileNotFoundException("该路径不是文件夹:" + path);
//        }
        // 若不指定路径或路径无效，则使用默认路径
        if (path.trim().length() == 0 || !file.exists() || !file.isDirectory()) {
            path = FILEPATH;
            file = new File(path);
        }

        // 获取系统文件夹中的文件
        File[] files = file.listFiles();
        // 获取数据库中的文件
        List<Crawler> dbFiles = fileUtil.crawlerService.queryCrawlersByPath(path);

        // 文件路径映射
        Map<String, String> map = new HashMap<>();
        for (Crawler crawler : dbFiles) {
            map.put(crawler.getFilePath(), crawler.getCrawlerName());
        }
        for (File fl : files) {
            // 反斜杠变为正斜杠
            String filePath = fl.getAbsoluteFile().toString().replaceAll("\\\\", "/");
            // 如果数据库中没有该文件，则添加到数据库中
            if (map.get(filePath) == null) {
                fileUtil.crawlerService.addCrawler(new Crawler(fl.getName(), filePath, 1));
            }
        }
    }
}
