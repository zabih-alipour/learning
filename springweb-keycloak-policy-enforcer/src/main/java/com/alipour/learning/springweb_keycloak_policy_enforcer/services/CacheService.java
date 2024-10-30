package com.alipour.learning.springweb_keycloak_policy_enforcer.services;

import com.alipour.learning.springweb_keycloak_policy_enforcer.models.Course;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private static final List<Course> COURSES = new ArrayList<>();
    private static final Map<String, Set<Course>> USER_ENROLLED_COURSES = new ConcurrentHashMap<>();

    public CacheService() {
        COURSES.add(new Course(1, "Math"));
        COURSES.add(new Course(2, "Physics"));
        COURSES.add(new Course(3, "Literature"));
        COURSES.add(new Course(4, "Advance Java"));
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(COURSES);
    }

    public Course getCourse(Integer id) {
        return COURSES.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Requested course not found"));
    }

    public Course addCourse(String title) {
        final Course last = COURSES.getLast();
        final Course course = new Course(last.getId() + 1, title);
        COURSES.add(course);
        return course;
    }

    public Boolean removeCourse(Integer id) {
        return COURSES.removeIf(p -> p.getId().equals(id));
    }

    public void enrollCourse(Integer id, String userId) {
        final Course course = COURSES.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Requested course not found"));
        USER_ENROLLED_COURSES.computeIfAbsent(userId, s -> new HashSet<>());
        USER_ENROLLED_COURSES.computeIfPresent(userId, (s, courses) -> {
            courses.add(course);
            return courses;
        });
    }

    public Boolean unrollCourse(Integer id, String userId) {
        return USER_ENROLLED_COURSES.get(userId).removeIf(p -> p.getId().equals(id));
    }

    public Set<Course> getEnrolledCourses(String userId) {
        return USER_ENROLLED_COURSES.get(userId);
    }

    public List<String> getEnrolledUsers(Integer courseId) {
        return USER_ENROLLED_COURSES.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(p -> p.getId().equals(courseId)))
                .map(Map.Entry::getKey)
                .toList();
    }
}
