package com.lzc.controller;

import com.lzc.service.UserService;
import com.lzc.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liang Zhancheng
 */
@Slf4j
@RequestMapping("/redis")
@RestController
public class RedisController {

    /**
     * redis中存储的过期时间60s
     */
    private static final int EXPIRE_TIME = 60;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @RequestMapping("/set")
    public boolean redisSet(String key, String value) {
        return redisUtils.set(key, value);
    }

    @GetMapping("/queryUserByRedis/{id}")
    public String queryUserById(@PathVariable("id") String id) {
        if (id == null) {
            return "";
        }
        boolean hasKey = redisUtils.hasKey(id);
        String str;
        if (hasKey) {
            Object object = redisUtils.get(id);
            log.info("从缓存获取的数据" + object);
            str = object.toString();
        } else {
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            str = userService.queryUserById(Integer.parseInt(id)).toString();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id, str, 30);
            log.info("数据插入缓存" + str);
        }
        return str;
    }

    @RequestMapping("/get")
    public Object redisGet(String key) {
        return redisUtils.get(key);
    }

    @RequestMapping("/expire")
    public boolean expire(String key) {
        return redisUtils.expire(key, EXPIRE_TIME);
    }
}