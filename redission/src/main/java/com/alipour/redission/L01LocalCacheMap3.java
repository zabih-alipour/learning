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
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 */
public class L01LocalCacheMap3 {
    static RedissionConfig config = new RedissionConfig();

    public static void main(String[] args) {
        final RedissonReactiveClient reactiveClient = config.reactiveClient();
        reactiveClient.getKeys()
                .getKeys()
                .doFirst(() -> System.out.println("-------------- Redis total key list --------------"))
                .doFinally(signalType -> System.out.println("--------------------------------------------------"))
                .subscribe(System.out::println);


        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.name("student-cache3");
        mapOptions.syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);
        mapOptions.reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.LOAD);
        mapOptions.codec(new TypedJsonJacksonCodec(String.class, Student.class));
        mapOptions.writeMode(WriteMode.WRITE_THROUGH);
        mapOptions.writeRetryAttempts(2);

        final RLocalCachedMapReactive<Integer, Student> cachedMap = reactiveClient.getLocalCachedMap(mapOptions);
        doSomeRandomThings(cachedMap).subscribe();
        Flux.interval(Duration.ofSeconds(3))
                .map(ignored -> cachedMap.getCachedMap())
                .subscribe(System.out::println);
    }

    static Mono<Void> doSomeRandomThings(RLocalCachedMapReactive<Integer, Student> cachedMap) {
        Random random = new Random(100);
        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(turn -> {
                    int nexted = Math.abs(random.nextInt() * 100);
                    if (nexted % 2 == 0) {
                        Student student = Student.builder()
                                .name("Jake_" + nexted)
                                .age(nexted)
                                .courses(List.of(Course.builder().title("Math " + nexted).build()))
                                .build();
                        return cachedMap.put(nexted, student);
                    } else {
                        return cachedMap.randomEntries(1)
                                .flatMap(entry -> {
                                    Integer key = entry.keySet().iterator().next();
                                    Student value = entry.values().iterator().next();
                                    value.setName(value.getName().concat("_" + Math.abs(random.nextInt() * 100)));
                                    return cachedMap.fastPut(key, value);
                                });
                    }
                })
                .then();


    }

}
