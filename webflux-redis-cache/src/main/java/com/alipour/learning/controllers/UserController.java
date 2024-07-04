package com.alipour.learning.controllers;

import com.alipour.learning.dtos.UserDto;
import com.alipour.learning.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<UserDto> list() {
        return userService.list();
    }

    @GetMapping("/cache/list/2")
    public Flux<UserDto> list2() {
        return userService.list2();
    }
    @GetMapping("/cache/list/3")
    public List<UserDto> list3() {
        return userService.list3();
    }
}
