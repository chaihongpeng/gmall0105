package com.atguigu.gmall.search;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GmallSearchWebApplicationTests {

    @Test
    void contextLoads() {
        List<String> list = new ArrayList<String>();
        list.add("11");
        list.add("22");
        list.removeIf(next -> next.equals("11"));
    }

}
