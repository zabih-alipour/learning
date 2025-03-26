package com.alpour.learning.streams;

import com.github.javafaker.Faker;

import java.text.BreakIterator;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterMapFlatMap {
    static Faker faker = Faker.instance();

    public static void main(String[] args) {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t Generated Names\n");
        List<String> names = Stream.generate(() -> faker.name().fullName())
                .limit(20)
                .peek(System.out::println)
                .toList();

        printLongestNamesCount(names);
        printUniqueGraphemeClustersByFlatMap(names);
        printUniqueGraphemeClustersByMultiMap(names);

        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t Generated Numbers\n");
        List<Long> numbers = Stream.generate(() -> faker.number().randomNumber())
                .limit(20)
                .peek(System.out::println)
                .toList();



    }

    private static void printLongestNamesCount(List<String> names) {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t Getting longest names count\n");

        long longestNamesCount = names.stream().filter(p -> p.length() > 15)
                .count();
        System.out.println("longestNamesCount = " + longestNamesCount);

        List<String> longestNames = names.stream().filter(p -> p.length() > 15).toList();
        System.out.println("longestNames = " + longestNames);
    }

    private static void printUniqueGraphemeClustersByFlatMap(List<String> names) {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t Print unique Letters\n");

        Collection<String> uniqueLetters = names.stream()
                .flatMap(name -> new Scanner(name).useDelimiter("\\b{g}").tokens())
                .filter(p -> !p.isBlank())
                .collect(Collectors.toSet());

        System.out.println("uniqueLetters = " + uniqueLetters);
    }

    private static void printUniqueGraphemeClustersByMultiMap(List<String> names) {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t Print unique Words With multiMap\n");

        BreakIterator iterator = BreakIterator.getWordInstance();
        Collection<Object> uniqueLetters = names.stream()
                .mapMulti((name, collector) -> {
                    iterator.setText(name);
                    int start = iterator.first();
                    int end = iterator.next();
                    while (end != BreakIterator.DONE) {
                        String gc = name.substring(start, end);
                        start = end;
                        end = iterator.next();
                        collector.accept(gc);
                    }
                })
                .collect(Collectors.toSet());

        System.out.println("uniqueWords = " + uniqueLetters);
    }
}
