package com.lzc.mysubscription.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Liang Zhancheng
 * @date 2021/7/10 12:17
 * @description 向app端实时推送业务状态信息
 * 由于是websocket 所以原本是@RestController的http形式
 * 直接替换成@ServerEndpoint即可，作用是一样的 就是指定一个地址
 * 表示定义一个websocket的Server端
 */
@Component
@ServerEndpoint(value = "/websocket/{appNo}")
public class WebSocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    /**
     * @Author: TheBigBlue
     * @Description: 加入连接
     * @Date: 2019/7/16
     * @param appNo: 申请单号
     * @param session:
     * @return:
     **/
    @OnOpen
    public void onOpen(@PathParam("appNo") String appNo, Session session) {
        LOGGER.info("[" + appNo + "]加入连接!");
        WebSocketUtils.addSession(appNo, session);
    }

    /**
     * @Author: TheBigBlue
     * @Description: 断开连接
     * @Date: 2019/7/16
     * @param appNo:
     * @param session:
     * @return:
     **/
    @OnClose
    public void onClose(@PathParam("appNo") String appNo, Session session) {
        LOGGER.info("[" + appNo + "]断开连接!");
        WebSocketUtils.remoteSession(appNo);
    }

    /**
     * @Author: TheBigBlue
     * @Description: 发送消息
     * @Date: 2019/7/16
     * @param appNo: 申请单号
     * @param message: 消息
     * @return:
     **/
    @OnMessage
    public void OnMessage(@PathParam("appNo") String appNo, String message) {
        String messageInfo = "服务器对[" + appNo + "]发送消息：" + message;
        LOGGER.info(messageInfo);
        Session session = WebSocketUtils.ONLINE_SESSION.get(appNo);
        if ("heart".equalsIgnoreCase(message)) {
            LOGGER.info("客户端向服务端发送心跳");
            //向客户端发送心跳连接成功
            message = "success";
        }
        //发送普通信息
        WebSocketUtils.sendMessage(session, message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.error(session.getId() + "异常:", throwable);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }
}