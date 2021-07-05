package com.lzc.pojo;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.*;

import java.io.Serializable;

/**
 * @author Liang Zhancheng
 * @description id（编号），registration（注册时间）和 gmtModified（修改时间）为数据库自动生成，无需手动注入
 */
@Data
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = -3650359202461551649L;

    private Integer id;
    private String userName;
    private String email;
    /**
     * @description 全局权限(isAllowed) 0: false; 1: true
     */
    private Integer isAllowed;
    /**
     * @description 角色(role) 0: admin; 1: user
     */
    private Integer role;
    private String registration;
    private String gmtModified;

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String userName, String email, Integer isAllowed, Integer role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAllowed = isAllowed;
        this.role = role;
    }

    public User(String userName, String email, Integer isAllowed, Integer role) {
        this.userName = userName;
        this.email = email;
        this.isAllowed = isAllowed;
        this.role = role;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userName", userName)
                .add("email", email)
                .add("isAllowed", isAllowed)
                .add("role", role)
                .add("registration", registration)
                .add("gmtModified", gmtModified)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equal(id, user.id) && Objects.equal(userName, user.userName) && Objects.equal(email, user.email) && Objects.equal(isAllowed, user.isAllowed) && Objects.equal(role, user.role) && Objects.equal(registration, user.registration) && Objects.equal(gmtModified, user.gmtModified);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userName, email, isAllowed, role, registration, gmtModified);
    }
}
