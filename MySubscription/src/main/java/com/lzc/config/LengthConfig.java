package com.lzc.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author Liang Zhancheng
 * @date 2021/6/28 20:36
 * @description 长度配置类
 */
@Configuration
public class LengthConfig {
    /**
     * serviceNameMaxLength 服务名称最大长度
     * serviceNoteMaxLength 服务备注最大长度
     */
    public static final Integer SERVICE_NAME_MAX_LENGTH = 45;
    public static final Integer SERVICE_NOTE_MAX_LENGTH = 100;
}
