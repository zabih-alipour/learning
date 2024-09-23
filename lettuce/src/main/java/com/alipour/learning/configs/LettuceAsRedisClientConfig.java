package com.alipour.learning.configs;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LettuceAsRedisClientConfig {

    protected final RedisProperties redisProperties;

    public LettuceAsRedisClientConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }


    @Bean
    @ConditionalOnMissingBean
    public RedisReactiveCommands<String, Object> reactiveCommands() {

        String connectionString = "redis://" +
                redisProperties.getPassword() +
                "@" +
                redisProperties.getHost() +
                ":" +
                redisProperties.getPort() +
                "/";

        RedisClient redisClient = RedisClient.create(connectionString);
        final RedisURI redisURI = RedisURI.builder()
                .withPassword(redisProperties.getPassword().toCharArray())
                .withHost(redisProperties.getHost())
                .withPort(redisProperties.getPort())
                .build();
        StatefulRedisPubSubConnection<String, Object> connection = redisClient.connectPubSub(RedisCodec.of(new StringCodec(), new SepantaLettuceCustomStringCodec()), redisURI);
        return connection.reactive();
    }
}
