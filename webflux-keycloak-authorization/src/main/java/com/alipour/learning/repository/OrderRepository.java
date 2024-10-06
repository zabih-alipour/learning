package com.alipour.learning.repository;

import com.alipour.learning.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByRestaurantId(Long restaurantId);

}
