package com.alpour.learning.garbege_collectors;

/**
 * To run application:  java -XX:+UseConcMarkSweepGC -Xlog:gc*=debug:file=./CMSGCExample.txt CMSGCExample.java
 * The -XX:+UseConcMarkSweepGC option has been deprecated in Java 9 and was completely removed in Java 14.
 * This means that starting from Java 21, you cannot use this option anymore.
 * The deprecation was due to the maintenance burden it added to the garbage collector code base,
 * which slowed down new development efforts.
 */
public class CMSGCExample {
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
