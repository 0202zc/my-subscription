package com.lzc.mysubscription.websocket;

import com.alibaba.fastjson.JSON;
import com.lzc.controller.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liang Zhancheng
 * @date 2021/7/10 12:24
 * @description
 */
@RestController
@RequestMapping("/socket")
public class WebSocketPushController {

    @Autowired
    private WebSocketController webSocketController;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * @Author: TheBigBlue
     * @Description:
     * @Date: 2019/7/16
     * @Param appNo: 发送的用户名
     * @Param relTyp: 发送的用户名
     * @Param message: 发送的信息
     * @Return:
     **/
    @RequestMapping("/push")
    public String pushToWeb(String appNo, String message) {
//        webSocketController.OnMessage(appNo, message);
//        webSocketServer.onMessage(message, session);
        return JSON.toJSONString("");
    }
}
