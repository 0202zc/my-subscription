package com.lzc.pojo;

import com.google.common.base.Objects;
import lombok.*;

import java.io.Serializable;

/**
 * @author Liang Zhancheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Subscription implements Serializable {

    private static final long serialVersionUID = 2543425082595826059L;

    private Integer id;
    private Integer userId;
    private Integer serviceId;
    private String sendTime;
    private Integer allowSend;
    private String subscribeTime;
    private String gmtModified;

    public Subscription(Integer serviceId, String sendTime, Integer allowSend) {
        this.serviceId = serviceId;
        this.sendTime = sendTime;
        this.allowSend = allowSend;
    }

    public Subscription(Integer userId, Integer serviceId, String sendTime) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscription that = (Subscription) o;
        return Objects.equal(id, that.id) && Objects.equal(userId, that.userId) && Objects.equal(serviceId, that.serviceId) && Objects.equal(sendTime, that.sendTime) && Objects.equal(allowSend, that.allowSend) && Objects.equal(subscribeTime, that.subscribeTime) && Objects.equal(gmtModified, that.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, serviceId, sendTime, allowSend, subscribeTime, gmtModified);
    }
}
