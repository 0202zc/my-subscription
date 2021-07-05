package com.lzc.util;

import org.springframework.stereotype.Component;

/**
 * @author Liang Zhancheng
 * @date 2021/7/5 19:41
 * @description
 */
@Component
public class EnumUtils {

    public enum SwitchAuthority {
        /**
         * @description 权限枚举类，1：允许，0：禁止
         */
        ALLOW("1"), REFUSE("0");

        private final String key;

        SwitchAuthority(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }


    public enum SwitchRole {
        /**
         * @description 角色枚举类，1：用户，0：管理员
         */
        USER("1"), ADMIN("0");

        private final String key;

        SwitchRole(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum SwitchOperate {
        /**
         * @description 操作枚举类，add：添加，edit：编辑，del：删除
         */
        ADD("add"), EDIT("edit"), DELETE("del");
        private final String key;

        SwitchOperate(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * @description 判断 key 是否属于权限类的值
     * @param key String
     * @return boolean
     */
    public static boolean authorityInclude(String key) {
        for (SwitchAuthority e : SwitchAuthority.values()) {
            if (e.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @description 判断 key 是否属于角色类的值
     * @param key String
     * @return boolean
     */
    public static boolean roleInclude(String key) {
        for (SwitchRole e : SwitchRole.values()) {
            if (e.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @description 判断 key 是否属于操作类的值
     * @param key String
     * @return boolean
     */
    public static boolean operateInclude(String key) {
        for (SwitchOperate e : SwitchOperate.values()) {
            if (e.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
