package com.ankit.performance.tutorial.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

  /**
   * more efficient than creating thread on demand.
   * thread are alive as long as executor is alive.
   * There are a few different ways to delegate tasks for execution to an ExecutorService:
   *
   * execute(Runnable)
   * submit(Runnable)
   * submit(Callable)
   * invokeAny(...)
   * invokeAll(...)
   *
   */
  public static void main(String[] args) throws InterruptedException {
    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    ExecutorService scheduleThreadPool = Executors.newScheduledThreadPool(10);
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    Runnable task1 = () -> {
      System.out.println("executing task-1");
    };
    Runnable task2 = () -> {
      System.out.println("executing task-2");
    };
    Runnable task3 = () -> {
      System.out.println("executing task-3");
    };

    executorService.execute(task1);
    executorService.execute(task2);
        /*
           task is added to waiting queue when no thread available.
           task are executed in the order of their submission.
         */
    executorService.execute(task3);

        /*
        soft way of closing executor service
           continues executing submitted task.
           do not accept new task
         */
    executorService.shutdown();

    /**
     *  this will halt running task.
     *  doesn't execute waiting task.
     */
    executorService.shutdownNow();

    /**
     * first shutdown
     * wait for timeout
     * post timeout halt everything
     */
    executorService.awaitTermination(2000, TimeUnit.MILLISECONDS);
  }
}
