package com.ankit.performance.tutorial.forkjoin;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Example {

  public static void main(String[] args) {
    Random random = new Random();
    List<Long> data = random.longs(10, 1, 5).boxed().collect(toList());
    System.out.println("data" + data);
    ForkJoinPool pool = new ForkJoinPool();
    SumAction task = new SumAction(data);
    pool.invoke(task);
  }

  static class SumTask1 extends RecursiveTask<Long> {
    private static final int SEQUENTIAL_THRESHOLD = 5;

    private List<Long> data;

    public SumTask1(List<Long> data) {
      this.data = data;
    }

    private long computeSumDirectly() {
      long sum = 0;
      for (Long l : data) {
        sum += l;
      }
      return sum;
    }

    @Override
    protected Long compute() {
      if (data.size() <= SEQUENTIAL_THRESHOLD) { // base case
        long sum = computeSumDirectly();
        System.out.println(Thread.currentThread().getName());
        System.out.format("Sum of %s: %d\n", data.toString(), sum);
        return sum;
      } else { // recursive case
        // Calcuate new range
        int mid = data.size() / 2;
        SumTask1 firstSubtask = new SumTask1(data.subList(0, mid));
        SumTask1 secondSubtask = new SumTask1(data.subList(mid, data.size()));

        // queue the first task
        firstSubtask.fork();

        // Return the sum of all subtasks
        return secondSubtask.compute() + firstSubtask.join();
      }
    }
  }

  static class SumAction extends RecursiveAction {
    private static final int SEQUENTIAL_THRESHOLD = 5;
    private List<Long> data;

    public SumAction(List<Long> data) {
      this.data = data;
    }

    @Override
    protected void compute() {
      if (data.size() <= SEQUENTIAL_THRESHOLD) { // base case
        long sum = computeSumDirectly();
        System.out.println(Thread.currentThread().getName());
        System.out.format("Sum of %s: %d\n", data.toString(), sum);
      } else { // recursive case
        // Calcuate new range
        int mid = data.size() / 2;
        SumAction firstSubtask = new SumAction(data.subList(0, mid));
        SumAction secondSubtask = new SumAction(data.subList(mid, data.size()));

        firstSubtask.fork(); // queue the first task
        secondSubtask.compute(); // compute the second task
        firstSubtask.join(); // wait for the first task result
        // Or simply call
        // invokeAll(firstSubtask, secondSubtask);
      }
    }

    private long computeSumDirectly() {
      long sum = 0;
      for (Long l : data) {
        sum += l;
      }
      return sum;
    }
  }
}
