package com.atguigu.gmall.user;

import com.atguigu.gmall.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallUserServiecApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        System.out.println(userService.getAllUser());
    }

}
