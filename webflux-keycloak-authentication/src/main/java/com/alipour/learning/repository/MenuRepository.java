package com.alipour.learning.repository;

import com.alipour.learning.entity.Menu;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MenuRepository  extends ReactiveCrudRepository<Menu, Long> {

	Mono<Menu> findByRestaurantId(Long restaurantId);
}
