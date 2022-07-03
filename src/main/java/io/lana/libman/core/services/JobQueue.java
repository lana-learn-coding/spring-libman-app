package io.lana.libman.core.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JobQueue {
    private final ExecutorService executorService = Executors.newWorkStealingPool();

    public CompletableFuture<Void> submit(Runnable task) {
        return CompletableFuture.runAsync(task, executorService);
    }
}
