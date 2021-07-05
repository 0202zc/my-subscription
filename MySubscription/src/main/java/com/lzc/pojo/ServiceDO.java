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
public class ServiceDO implements Serializable {

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

    public ServiceDO(Integer id, String serviceName, String note) {
        this.id = id;
        this.serviceName = serviceName;
        this.note = note;
    }

    public ServiceDO(String serviceName, String note, String email, Integer enabled) {
        this.serviceName = serviceName;
        this.note = note;
        this.email = email;
        this.enabled = enabled;
    }

    public ServiceDO(String serviceName, String note) {
        this.serviceName = serviceName;
        this.note = note;
    }

    public ServiceDO(String serviceName, String note, Integer enabled) {
        this.serviceName = serviceName;
        this.note = note;
        this.enabled = enabled;
    }

    public ServiceDO(Integer id) {
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
        ServiceDO service = (ServiceDO) o;
        return Objects.equal(id, service.id) && Objects.equal(serviceName, service.serviceName) && Objects.equal(note, service.note) && Objects.equal(email, service.email) && Objects.equal(enabled, service.enabled) && Objects.equal(serviceType, service.serviceType) && Objects.equal(gmtCreate, service.gmtCreate) && Objects.equal(gmtModified, service.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, serviceName, note, email, enabled, serviceType, gmtCreate, gmtModified);
    }
}
