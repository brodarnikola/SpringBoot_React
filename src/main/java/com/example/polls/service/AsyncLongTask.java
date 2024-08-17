package com.example.polls.service;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class AsyncLongTask  {

    @Async
    public CompletableFuture<String> task1() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate long-running task
            return "Task 1 Result";
        });
    }

    @Async
    public CompletableFuture<String> task2() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate long-running task
            return "Task 2 Result";
        });
    }

    public CompletableFuture<String> runTasksInParallel() {
        CompletableFuture<String> future1 = task1();
        CompletableFuture<String> future2 = task2();

        return future1.thenCombine(future2, (result1, result2) -> result1 + " and " + result2);
    }

}
