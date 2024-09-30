package com.alipour.learning.repository;

import com.alipour.learning.entity.MenuItem;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MenuItemRepository extends ReactiveCrudRepository<MenuItem, Long> {

    Flux<MenuItem> findAllByMenuId(@Param("menuId") Long menuId);

}
