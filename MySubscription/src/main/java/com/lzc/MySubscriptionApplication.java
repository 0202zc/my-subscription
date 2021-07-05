package com.lzc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Liang Zhancheng
 */
@MapperScan(value = "com.lzc.mapper")
@SpringBootApplication
public class MySubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySubscriptionApplication.class, args);
    }

}
