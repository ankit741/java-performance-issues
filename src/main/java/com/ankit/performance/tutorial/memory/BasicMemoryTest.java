package com.ankit.performance.tutorial.memory;

import com.ankit.performance.tutorial.memory.model.Student;
import java.util.HashMap;
import java.util.Map;

// VM args ::  -Xms50m -Xmx50m -XX:+PrintGCDetails -Xss1m -Xloggc:gc.log -XX:+UseG1GC
//key takeaways file - basic-memory.md

public class BasicMemoryTest {
  static Map<Integer, Student> map = new HashMap<Integer, Student>();

  public static void main(String[] args) throws InterruptedException {
    printAvailableCores();
    printMemoryUtilization();
    //fillStack(5);
    fillMemory(20000000);
    Thread.sleep(2000);
    printMemoryUtilization();
  }

  private static void printAvailableCores() {
    /* Total number of processors or cores available to the JVM */
    System.out.println(
        "Available processors (cores): " + Runtime.getRuntime().availableProcessors());
  }

  private static void printMemoryUtilization() {
    int mb = 1024 * 1024;
    /* Returns the maximum amount of memory available to the Java Virtual Machine set by the '-mx' or '-Xmx' flags. */
    /* This will return Long.MAX_VALUE if there is no preset limit */
    long max = Runtime.getRuntime().maxMemory() / mb;
    /* Total memory currently in use by the JVM */
    /* Returns the total memory allocated from the system which can at most reach the maximum memory value returned by the previous function. */
    long total = Runtime.getRuntime().totalMemory() / mb;
    /* Total amount of free memory available to the JVM */
    /* Returns the free memory *within* the total memory  returned by the previous function. */
    long free = Runtime.getRuntime().freeMemory() / mb;

    System.out.println(
        String.format("Max: %s, Total: %s, Free: %s, Used: %s", max, total, free, (total - free)));
  }

  public static void fillMemory(int n) throws InterruptedException {
    int j = 0;
    for (int i = 0; i < n; i++) {
      if (i == 200000) {
        /*
        the Old Generation is almost full, indicating that,
        even with a Full GC, the Old Generation is ever-growing, a clear sign of a memory leak.
        */
        printMemoryUtilization();
        Thread.sleep(20000);
      }
      map.put(j++, new Student(i, "test-" + i, "99" + i));
    }
  }

  /**
   * Parameters and local variables are allocated on the stack (with reference types,
   * the object lives on the heap and a variable in the stack references that object on the heap).
   */
  public static int fillStack(int number) throws InterruptedException {
    Thread.sleep(1);
    return number * fillStack(number - 1);
  }

}
