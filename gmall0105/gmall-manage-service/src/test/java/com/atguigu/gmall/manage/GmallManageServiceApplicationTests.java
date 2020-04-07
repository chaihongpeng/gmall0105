package com.atguigu.gmall.manage;

import com.atguigu.gmall.bean.PmsProductSaleAttr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class GmallManageServiceApplicationTests {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setId("dsd");
        pmsProductSaleAttr.setProductId("dsd");
        redisTemplate.opsForValue().set("sort", pmsProductSaleAttr);
    }

}
