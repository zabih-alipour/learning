package com.alipour.learning.inheritance;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 */
public class InheritanceMappingApp {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        Shape circle = new Circle();
        String json = toJson(circle);
        System.out.println("json = " + json);

        Shape shape = fromJson(json, Shape.class);
        System.out.println("shape = " + shape);

        shape = fromJson(json, Circle.class);
        System.out.println("circle = " + shape);


    }

    public void SerDe(@JsonSubTypes({
            @JsonSubTypes.Type(value = Circle.class, name = "Circle")
    }) Shape shape) {

    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
