package com.alipour.learning;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class ExpireVsExpireAt implements CommandLineRunner {
    @Autowired
    RedisReactiveCommands<String, Object> lettuceReactiveClient;

    @Override
    public void run(String... args) throws Exception {
        final LocalDate date = LocalDate.now().plusDays(1);
        final LocalDateTime expireAt = date.atTime(LocalTime.MAX);

        lettuceReactiveClient.expire("withExpire", expireAt.getSecond())
                .block();
//                .subscribe(System.out::println);

        lettuceReactiveClient.expireat("withExpireAt", expireAt.atZone(ZoneId.of("Asia/Tehran")).toInstant())
                .block();
//                .subscribe(System.out::println);
    }
}
