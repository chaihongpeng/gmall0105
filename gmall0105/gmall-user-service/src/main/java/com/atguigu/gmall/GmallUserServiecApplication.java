package com.atguigu.gmall;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.atguigu.gmall.user.mapper"})
@EnableDubbo
public class GmallUserServiecApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUserServiecApplication.class, args);
    }
    //中软集团

}
