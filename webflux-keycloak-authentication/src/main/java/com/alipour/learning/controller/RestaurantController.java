package com.alipour.learning.controller;

import com.alipour.learning.entity.Menu;
import com.alipour.learning.entity.MenuItem;
import com.alipour.learning.entity.Restaurant;
import com.alipour.learning.repository.MenuItemRepository;
import com.alipour.learning.repository.MenuRepository;
import com.alipour.learning.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/restaurant")
@SecurityRequirement(name = "Keycloak")
public class RestaurantController {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @GetMapping("/public/list")
    //Public API
    public Flux<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/public/{restaurantId}/menu")
    //Public API
    public Mono<Menu> getMenu(@PathVariable(name = "restaurantId") Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/public/menu/all")
    //Public API
    public Flux<Menu> getMenu() {
        return menuRepository.findAll();
    }

    @GetMapping("/public/menu/{menuId}")
    //Public API
    public Flux<MenuItem> getMenuItem(@PathVariable(name = "menuId") Long menuId) {
        return menuItemRepository.findAllByMenuId(menuId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PostMapping("/menu")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<Menu> createMenu(@RequestBody Menu menu) {

        return menuRepository.save(menu)
                .filter(p -> Objects.nonNull(p.getMenuItems()))
                .flatMap(entity -> Flux.fromIterable(menu.getMenuItems())
                        .map(orderItem -> {
                            orderItem.setMenuId(entity.getId());
                            return orderItem;
                        })
                        .flatMap(menuItemRepository::save)
                        .collectList()
                        .map(orderItems -> {
                            entity.setMenuItems(orderItems);
                            return entity;
                        }));
    }

    @PostMapping("/menu/item")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @PutMapping("/menu/item/{itemId}/{price}")
    @PreAuthorize("hasRole('OWNER')")
    public Mono<MenuItem> updateMenuItemPrice(@PathVariable Long itemId
            , @PathVariable BigDecimal price) {
        return menuItemRepository.findById(itemId)
                .map(menuItem -> {
                    menuItem.setPrice(price);
                    return menuItem;
                })
                .flatMap(menuItemRepository::save);
    }
}
