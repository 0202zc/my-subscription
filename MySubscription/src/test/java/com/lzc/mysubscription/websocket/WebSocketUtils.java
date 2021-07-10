package com.lzc.mysubscription.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author Liang Zhancheng
 * @date 2021/7/10 12:12
 * @description
 */
@Component
public class WebSocketUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUtils.class);

    /**
     * @Author: TheBigBlue
     * @Description: 使用map进行存储在线的session
     * @Date: 2019/7/16
     **/
    public static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * @Author: TheBigBlue
     * @Description: 添加Session
     * @Date: 2019/7/16
     * @Param userKey:
     * @Param session:
     * @Return:
     **/
    public static void addSession(String userKey, Session session) {
        ONLINE_SESSION.put(userKey, session);
    }

    public static void remoteSession(String userKey) {
        ONLINE_SESSION.remove(userKey);
    }

    /**
     * @Author: TheBigBlue
     * @Description: 向某个用户发送消息
     * @Date: 2019/7/16
     * @Param session:
     * @Param message:
     * @Return:
     **/
    public static Boolean sendMessage(Session session, String message) {
        if (session == null) {
            return false;
        }
        // getAsyncRemote()和getBasicRemote()异步与同步
        Async async = session.getAsyncRemote();
        //发送消息
        Future<Void> future = async.sendText(message);
        boolean done = future.isDone();
        LOGGER.info("服务器发送消息给客户端" + session.getId() + "的消息:" + message + "，状态为:" + done);
        return done;

    }
}
