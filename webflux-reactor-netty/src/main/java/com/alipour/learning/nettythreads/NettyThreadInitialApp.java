package com.alipour.learning.nettythreads;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyThreadInitialApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(NettyThreadInitialApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.getAllStackTraces()
                .forEach((thread, value) -> {
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
