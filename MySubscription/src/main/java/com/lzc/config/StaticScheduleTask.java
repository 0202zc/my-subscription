package com.lzc.config;

import com.lzc.controller.websocket.WebSocketServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author lanhaifeng
 * @date 2019/5/16 0016 14:29
 * @description webSocket定时发送消息类
 * 以 < 60 s的频率发送给websocket连接的对象 ， 以防止反向代理的60s超时限制
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {
    /**
     * @note @Scheduled(cron = "0/5 * * * * ?")
     * @throws Exception
     */
    @Scheduled(fixedRate = 5000)
    private void configureTasks() throws Exception {
        WebSocketServer.broadCastInfo("connected");
    }
}
