package com.alipour.learning.controller;

import com.alipour.learning.entity.Order;
import com.alipour.learning.repository.OrderItemRepository;
import com.alipour.learning.repository.OrderRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name = "Keycloak")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @GetMapping("/by-restaurant/{restaurantId}/list")
    public Flux<Order> getRestaurantOrders(@PathVariable Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/by-user/{userId}/list")
    public Flux<Order> getUserOrders(@PathVariable(name = "userId") String userId) {
        return orderRepository.findByUserId(userId);
    }

    @GetMapping("/{orderId}")
    public Mono<Order> getOrderDetails(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .zipWith(orderItemRepository.findByOrderId(orderId).collectList())
                .map(tuple2 -> {
                    tuple2.getT1().setOrderItems(tuple2.getT2());
                    return tuple2.getT1();
                });
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderRepository.save(order)
                .flatMap(entity -> Flux.fromIterable(order.getOrderItems())
                        .map(orderItem -> {
                            orderItem.setOrderId(entity.getId());
                            return orderItem;
                        })
                        .flatMap(orderItemRepository::save)
                        .collectList()
                        .map(orderItems -> {
                            entity.setOrderItems(orderItems);
                            return entity;
                        }));
    }

}
