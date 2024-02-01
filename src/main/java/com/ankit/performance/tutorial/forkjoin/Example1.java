package com.ankit.performance.tutorial.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Fork/Join parallelism is among the simplest and most effective design techniques for obtaining
 * good parallel performance. Fork/join algorithms are parallel versions of familiar divide−
 * and−conquer algorithms, taking the typical form: Result solve(Problem problem) { if (problem is
 * small) directly solve problem else { split problem into independent parts fork new subtasks to
 * solve each part join all subtasks compose result from subresults } }
 *
 * <p>2.1 Work−Stealing The heart of a fork/join framework lies in its lightweight scheduling
 * mechanics. FJTask adapts the basic tactics pioneered in the Cilk work−stealing scheduler:
 */
public class Example1 {

  public static void main(String[] args) {
    // 39 sec vs 15 sec
    /*        long start=System.currentTimeMillis();
    System.out.println(fib(50));
    long end=System.currentTimeMillis();
    System.out.println("time taken in sec"+(end-start)/1000F);*/
    long start1 = System.currentTimeMillis();
    final int numberOfProcessors = Runtime.getRuntime().availableProcessors();
    final ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);
    int ar[] = {1, 2, 3, 4};
    task t = new task(ar, 1, 4);
    forkJoinPool.submit(t);
    System.out.println("Array: " + Arrays.toString(ar));
    t.join();
    System.out.println("Array: " + t.endval);
    long end1 = System.currentTimeMillis();
    System.out.println("time taken in sec" + (end1 - start1) / 1000F);
  }

  static class task extends RecursiveAction {
    int ar[], start, end, endval;

    task(int[] ar, int start, int end) {
      this.ar = ar;
      this.start = start;
      this.end = end;
    }

    @Override
    protected void compute() {
      if (end == start) {
        System.out.println(Thread.currentThread().getName());
        endval = ar[end - 1] = ar[end - 1] * ar[end - 1];
      } else {
        task t1 = new task(ar, start, (start + end) / 2);

        task t2 = new task(ar, ((start + end) / 2) + 1, end);
        t1.fork();
        t2.fork();
        t1.join();
        t2.join();
        endval = t1.endval + t2.endval;
      }
    }
  }
}
