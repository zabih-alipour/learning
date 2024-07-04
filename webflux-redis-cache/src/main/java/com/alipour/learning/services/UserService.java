package com.alipour.learning.services;

import com.alipour.learning.dtos.UserDto;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.cache.annotation.CacheValue;
import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class UserService {

    public Flux<UserDto> list() {
        return Flux.range(1, 10)
                .map(String::valueOf)
                .delayElements(Duration.ofMillis(500))
                .map("user_"::concat)
                .map(UserDto::new);
    }

    @Cacheable("users-list-2")
    public Flux<UserDto> list2() {
        return Flux.range(1, 10)
                .map(String::valueOf)
                .delayElements(Duration.ofMillis(500))
                .map("user_"::concat)
                .map(UserDto::new);
    }
    @Cacheable(cacheNames = {"users-list-3"})
    public List<UserDto> list3() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return IntStream.range(1, 10)
                .mapToObj(String::valueOf)
                .map("user_"::concat)
                .map(UserDto::new)
                .toList();
    }
}
