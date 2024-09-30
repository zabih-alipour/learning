package com.alipour.learning.repository;

import com.alipour.learning.entity.Restaurant;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ReactiveCrudRepository<Restaurant, Long> {
}
