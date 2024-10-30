package com.alipour.learning.springweb_keycloak_policy_enforcer.controllers;

import com.alipour.learning.springweb_keycloak_policy_enforcer.models.Course;
import com.alipour.learning.springweb_keycloak_policy_enforcer.services.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/enrolling")
@AllArgsConstructor
public class EnrollingController {
    private final CacheService cacheService;


    @PostMapping("/{courseId}")
    public ResponseEntity<Boolean> enrollCourse(@PathVariable(name = "courseId") Integer courseId,
                                                @AuthenticationPrincipal Principal principal) {
        cacheService.enrollCourse(courseId, principal.getName());
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Boolean> unrollCourse(@PathVariable(name = "courseId") Integer courseId,
                                                @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(cacheService.unrollCourse(courseId, principal.getName()));
    }

    @GetMapping()
    public ResponseEntity<Set<Course>> getEnrolledCourses(@AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(cacheService.getEnrolledCourses(principal.getName()));
    }

}
