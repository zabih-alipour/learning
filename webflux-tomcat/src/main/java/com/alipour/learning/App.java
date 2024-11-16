package com.alipour.learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.getAllStackTraces()
                .keySet()
                .stream()
                .sorted(Comparator.comparingLong(Thread::threadId))
                .forEach(thread -> {
                    System.out.println("thread.getName() = " + thread.getName());
                    System.out.println("thread.threadId() = " + thread.threadId());
                    System.out.println("thread.getThreadGroup() = " + thread.getThreadGroup());
                    System.out.println("thread.getPriority() = " + thread.getPriority());
                    System.out.println("thread.getState() = " + thread.getState());
                    System.out.println("thread.isDaemon() = " + thread.isDaemon());
                    System.out.println("thread.isAlive() = " + thread.isAlive());
                    System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
                    System.out.println("thread.isAlive() = " + thread.isAlive());
                    System.out.println("-----------------------------------------------------");
                });
    }
}
