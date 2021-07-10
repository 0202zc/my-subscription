package com.lzc.controller.websocket;

import com.alibaba.fastjson.JSON;
import com.lzc.config.HttpSessionConfig;
import com.lzc.pojo.UserDO;
import com.lzc.service.impl.UserServiceImpl;
import com.lzc.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Liang Zhancheng
 * 由于是websocket 所以原本是@RestController的http形式
 * 直接替换成@ServerEndpoint即可，作用是一样的 就是指定一个地址
 * 表示定义一个websocket的Server端
 */
@ServerEndpoint(value = "/ws/asset", configurator = HttpSessionConfig.class)
@Component
public class WebSocketServer {

    public WebSocketServer() {
//        this.init();
    }

    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象
     */
    private static final CopyOnWriteArraySet<Session> SESSION_SET = new CopyOnWriteArraySet<Session>();
    private static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    public static CopyOnWriteArraySet<Session> getSessionSet() {
        return SESSION_SET;
    }

    public static Map<String, Session> getOnlineSession() {
        return ONLINE_SESSION;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        SESSION_SET.add(session);
        ONLINE_SESSION.put(String.valueOf(session.getId()), session);
        /**
         * 在线数加1
         */
        int cnt = ONLINE_COUNT.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
        sendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_SET.remove(session);
        ONLINE_SESSION.remove(String.valueOf(session.getId()));
        int cnt = ONLINE_COUNT.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自 {} 客户端的消息：{}", session.getId(), message);
        Object object = null;
        if ("queryUsers".equals(message)) {
            UserServiceImpl userService = SpringUtils.getBean(UserServiceImpl.class);
            object = userService.queryUsers(new UserDO());
            sendMessage(session, JSON.toJSONString(object));
        } else if ("updateUser".equals(message)) {
            try {
                UserServiceImpl userService = SpringUtils.getBean(UserServiceImpl.class);
                object = userService.queryUsers(new UserDO());
                broadCastInfo(JSON.toJSONString(object));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
//            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     * @throws IOException
     */
    public static void broadCastInfo(String message) throws IOException {
        for (Session session : SESSION_SET) {
            if (session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        for (Session s : SESSION_SET) {
            if (s.getId().equals(sessionId)) {
                session = s;
                break;
            }
        }
        if (session != null) {
            sendMessage(session, message);
        } else {
            log.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }
}