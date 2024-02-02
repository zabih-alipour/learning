package com.alipour.redission;

import com.alipour.redission.configs.RedissionConfig;
import com.alipour.redission.dtos.Student;
import org.redisson.api.RLocalCachedMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.map.WriteMode;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 */
public class L01LocalCacheMap2 {
    static RedissionConfig config = new RedissionConfig();

    public static void main(String[] args) {

        final RedissonReactiveClient reactiveClient = config.reactiveClient();
        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.name("student-cache");
        mapOptions.syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);
        mapOptions.reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.LOAD);
        mapOptions.codec(new TypedJsonJacksonCodec(String.class, Student.class));
        mapOptions.writeMode(WriteMode.WRITE_THROUGH);
        mapOptions.writeRetryAttempts(2);

        final RLocalCachedMapReactive<Integer, Student> cachedMap = reactiveClient.getLocalCachedMap(mapOptions);
        Flux.interval(Duration.ofSeconds(1))
                .doFirst(() -> System.out.println("-------------- Getting Student 1 in 1 second interval --------------"))
//                .doFinally(signalType -> System.out.println("-".repeat(68)))
                .flatMap(i -> cachedMap.get(1))
                .doOnNext(System.out::println)
                .subscribe();


    }

}
