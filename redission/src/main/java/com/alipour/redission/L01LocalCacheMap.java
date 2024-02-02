package com.alipour.redission;

import com.alipour.redission.configs.RedissionConfig;
import com.alipour.redission.dtos.Course;
import com.alipour.redission.dtos.Student;
import org.redisson.api.RLocalCachedMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.map.WriteMode;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * Hello world!
 */
public class L01LocalCacheMap {
    static RedissionConfig config = new RedissionConfig();

    public static void main(String[] args) {
        final RedissonReactiveClient reactiveClient = config.reactiveClient();
        reactiveClient.getKeys()
                .getKeys()
                .doFirst(() -> System.out.println("-------------- Redis total key list --------------"))
                .doFinally(signalType -> System.out.println("--------------------------------------------------"))
                .subscribe(System.out::println);



        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.name("student-cache");
        mapOptions.syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);
        mapOptions.reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);
        mapOptions.codec(new TypedJsonJacksonCodec(String.class, Student.class));
        mapOptions.writeMode(WriteMode.WRITE_THROUGH);
        mapOptions.writeRetryAttempts(2);

        final RLocalCachedMapReactive<Integer, Student> cachedMap = reactiveClient.getLocalCachedMap(mapOptions);
        final Student jake = Student.builder()
                .name("Jake")
                .age(42)
                .courses(List.of(Course.builder().title("Math").build()))
                .build();
        cachedMap.put(1, jake)
                .subscribe();
    }

}
