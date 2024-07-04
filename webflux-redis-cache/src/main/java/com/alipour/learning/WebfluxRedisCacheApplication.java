package com.alipour.learning;

import com.alipour.learning.dtos.RedisProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
public class WebfluxRedisCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebfluxRedisCacheApplication.class, args);
    }
}
