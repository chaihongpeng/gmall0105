package com.atguigu.gmall.config;

//import org.redisson.Redisson;
//import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host:localhost}")
    String host;
    @Value("${spring.redis.port:6379}")
    String port;
    @Value("${spring.redis.database:0}")
    int database;
//    @Bean
//    public Redisson redisson() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://" + host + ":" + port).setDatabase(database);
//        return (Redisson) Redisson.create(config);
//    }
}
