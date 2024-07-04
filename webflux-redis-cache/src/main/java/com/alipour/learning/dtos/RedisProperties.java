package com.alipour.learning.dtos;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String url;
    private String username;
    private String password;
}
