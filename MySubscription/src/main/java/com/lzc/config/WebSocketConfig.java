package com.lzc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Liang Zhancheng
 * @date 2021/7/10 10:24
 * @description WebSocket 配置类
 */
@Configuration
public class WebSocketConfig {

    public static final String GET_USERS = "queryUsers";
    public static final String UPDATE_USER = "updateUser";

    /**
     * 给spring容器注入这个ServerEndpointExporter对象
     * 相当于xml：
     * <beans>
     *  <bean id="serverEndpointExporter" class="org.springframework.web.socket.server.standard.ServerEndpointExporter"/>
     * </beans>
     *
     * 检测所有带有@serverEndpoint注解的bean并注册它们。
     *
     * @return ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
