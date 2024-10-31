package com.alipour.learning.springweb_keycloak_policy_enforcer.controllers;

import com.alipour.learning.springweb_keycloak_policy_enforcer.dtos.CourseCreationDto;
import com.alipour.learning.springweb_keycloak_policy_enforcer.models.Course;
import com.alipour.learning.springweb_keycloak_policy_enforcer.services.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CacheService cacheService;

    public CourseController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    // Public API
    @GetMapping
    public ResponseEntity<List<Course>> listCourses() {
        return ResponseEntity.ok().body(cacheService.getCourses());
    }

    // Public API
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable(name = "courseId") Integer courseId) {
        return ResponseEntity.ok().body(cacheService.getCourse(courseId));
    }

    // ADMIN API
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody() CourseCreationDto creationDto) {
        return ResponseEntity.ok().body(cacheService.addCourse(creationDto.getTitle()));
    }

    // ADMIN API
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Boolean> removeCourse(@PathVariable(name = "courseId") Integer courseId) {
        return ResponseEntity.ok().body(cacheService.removeCourse(courseId));
    }

    // ADMIN API
    @GetMapping("/{courseId}/enrolled-users")
    public ResponseEntity<List<String>> getEnrolledUsers(@PathVariable(name = "courseId") Integer courseId) {
        return ResponseEntity.ok(cacheService.getEnrolledUsers(courseId));
    }

}
