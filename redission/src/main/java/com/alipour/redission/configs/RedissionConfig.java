package com.alipour.redission.configs;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.io.IOException;

public class RedissionConfig {

    public RedissonClient redissonClient() {
        Config config = null;
        try {
            config = Config.fromYAML(getClass().getResourceAsStream("/config-file.yaml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Redisson.create(config);
    }

    public RedissonReactiveClient reactiveClient() {
        return redissonClient().reactive();
    }
}
