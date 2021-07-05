package com.lzc.mysubscription;

import com.lzc.pojo.UserDO;
import com.lzc.util.EnumUtils;
import com.lzc.util.FileUtils;
import com.lzc.util.Md5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MySubscriptionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void fileTest() {
        FileUtils.scanFiles();
        FileUtils.scanDirectory(null);
    }

    @Test
    void md5Test() {
        String email = Md5Utils.encode("666");
        System.out.println(email);
    }

    @Test
    void guavaTest() {
        UserDO user = new UserDO();
        user.setId(1);
        System.out.println(user);
    }

    @Test
    void enumTest() {
        EnumUtils.SwitchRole switchRole = EnumUtils.SwitchRole.ADMIN;
        System.out.println(switchRole);
    }

}
