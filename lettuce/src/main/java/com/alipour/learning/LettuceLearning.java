package com.alipour.learning;

import com.alipour.learning.configs.RedisProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RedisProperties.class})
public class LettuceLearning {
    public static void main(String[] args) {
        SpringApplication.run(LettuceLearning.class, args);
    }
}
