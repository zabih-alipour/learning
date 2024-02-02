package com.alpour.learning;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SplitStream {
    public static void main(String[] args) {
        final List<Student> students = List.of(
                new Student("Ali", 22, "math"),
                new Student("Ali", 25, "physics"),
                new Student("Hosein", 26, "math"),
                new Student("Hassan", 22, "math")
        );
        System.out.println("------------------------------------- PartitioningBy -----------------------------------------------------");
        //-- partitioningBy make the stream to split two parts and return a map
        final Map<Boolean, List<Student>> partitioningBy = students.stream()
                .collect(Collectors.partitioningBy(p -> p.course().equals("math")));
        System.out.println(partitioningBy);

        System.out.println();
        System.out.println("------------------------------------- GroupingBy ----------------------------------------------------------");
        //-- groupingBy make the stream to split into two or more parts and return a map
        final Map<Integer, List<Student>> groupingBy = students.stream()
                .collect(Collectors.groupingBy(Student::age));
        System.out.println(groupingBy);

        System.out.println();
        System.out.println("------------------------------------- Teeing ----------------------------------------------------------");
        //-- Teeing combines two collectors into one composite
        final List<Long> teeingCount = students.stream()
                .collect(Collectors.teeing(
                        Collectors.filtering(student -> student.course().equals("math"), Collectors.counting()),
                        Collectors.filtering(student -> !student.course().equals("math"), Collectors.counting()),
                        List::of
                ));
        System.out.println("TeeingCount: " + teeingCount);

        final List<List<Student>> teeing = students.stream()
                .collect(Collectors.teeing(
                        Collectors.filtering(student -> student.course().equals("math"), Collectors.toList()),
                        Collectors.filtering(student -> !student.age().equals(22), Collectors.toList()),
                        List::of
                ));
        System.out.println("Teeing on list: " + teeing);

    }

    record Student(String name, Integer age, String course){

    }
}
