package com.lzc.pojo;

import com.google.common.base.Objects;
import lombok.*;

import java.io.Serializable;

/**
 * @author Liang Zhancheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Services implements Serializable {

    private static final long serialVersionUID = 5034306744011120277L;

    private Integer id;
    private String serviceName;
    private String note;
    /**
     * @description 这里email为自定义服务的用户联系方式
     */
    private String email;
    private Integer enabled;
    private Integer serviceType;
    private String gmtCreate;
    private String gmtModified;

    public Services(Integer id, String serviceName, String note) {
        this.id = id;
        this.serviceName = serviceName;
        this.note = note;
    }

    public Services(String serviceName, String note, String email, Integer enabled) {
        this.serviceName = serviceName;
        this.note = note;
        this.email = email;
        this.enabled = enabled;
    }

    public Services(String serviceName, String note) {
        this.serviceName = serviceName;
        this.note = note;
    }

    public Services(String serviceName, String note, Integer enabled) {
        this.serviceName = serviceName;
        this.note = note;
        this.enabled = enabled;
    }

    public Services(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Services services = (Services) o;
        return Objects.equal(id, services.id) && Objects.equal(serviceName, services.serviceName) && Objects.equal(note, services.note) && Objects.equal(email, services.email) && Objects.equal(enabled, services.enabled) && Objects.equal(serviceType, services.serviceType) && Objects.equal(gmtCreate, services.gmtCreate) && Objects.equal(gmtModified, services.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, serviceName, note, email, enabled, serviceType, gmtCreate, gmtModified);
    }
}
