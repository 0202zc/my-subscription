package com.lzc.pojo;

import com.google.common.base.Objects;
import lombok.*;

import java.io.Serializable;

/**
 * @author Liang Zhancheng
 */
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Crawler implements Serializable {
    private static final long serialVersionUID = -3252639351361284652L;

    private Integer id;
    private String crawlerName;
    private String filePath;
    private String email;
    private Integer enabled;
    /**
     * @description 爬虫类型 0：管理员定义；1：用户自定义
     */
    private Integer fileType;
    private String gmtCreate;
    private String gmtModified;

    public Crawler(String crawlerName, String filePath, Integer enabled) {
        this.crawlerName = crawlerName;
        this.filePath = filePath;
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Crawler crawler = (Crawler) o;
        return Objects.equal(id, crawler.id) && Objects.equal(crawlerName, crawler.crawlerName) && Objects.equal(filePath, crawler.filePath) && Objects.equal(email, crawler.email) && Objects.equal(enabled, crawler.enabled) && Objects.equal(fileType, crawler.fileType) && Objects.equal(gmtCreate, crawler.gmtCreate) && Objects.equal(gmtModified, crawler.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, crawlerName, filePath, email, enabled, fileType, gmtCreate, gmtModified);
    }
}
