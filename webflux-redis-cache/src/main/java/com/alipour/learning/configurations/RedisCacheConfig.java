package com.alipour.learning.configurations;

import com.alipour.learning.dtos.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SmileJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedissonClient redisClient(RedisProperties properties) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(properties.getUrl())
                /*.setUsername(properties.getUsername())
                .setPassword(properties.getPassword())*/;

        return Redisson.create(config);
    }

    @Bean
    public RedissonReactiveClient reactiveClient(RedissonClient redissonClient) {
        return redissonClient.reactive();
    }
}
