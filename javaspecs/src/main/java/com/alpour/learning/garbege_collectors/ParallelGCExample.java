package com.alpour.learning.garbege_collectors;

/**
 * To run application:  java -XX:+UseParallelGC -Xlog:gc*=debug:file=./ParallelGCExample.txt ParallelGCExample.java
 */
public class ParallelGCExample {
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            // Create a large number of objects
            String[] array = new String[10000];
            for (int j = 0; j < array.length; j++) {
                array[j] = new String("Object " + j);
            }
        }
    }
}
