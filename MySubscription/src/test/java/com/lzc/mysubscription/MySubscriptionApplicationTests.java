package com.lzc.mysubscription;

import com.lzc.pojo.User;
import com.lzc.util.FileUtil;
import com.lzc.util.Md5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

@SpringBootTest
class MySubscriptionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void fileTest() {
        FileUtil.scanFiles();
        FileUtil.scanDirectory(null);
    }

    @Test
    void md5Test() {
        String email = Md5Utils.encode("666");
        System.out.println(email);
    }

    @Test
    void guavaTest() {
        User user = new User();
        user.setId(1);
        System.out.println(user);
    }

}
