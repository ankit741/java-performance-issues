package com.ankit.performance.tutorial.memory;

import com.ankit.performance.tutorial.memory.model.Student;
import java.util.HashMap;
import java.util.Map;

// VM args ::  -Xms50m -Xmx50m -Xloggc:gc.log -XX:+UseG1GC

public class BasicMemoryTest {
  static Map<Integer, Student> map = new HashMap<Integer, Student>();

  public static void main(String[] args) throws InterruptedException {
    printAvailableCores();
    printMemoryUtilization();
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

  public static void fillMemory(int n) {
    int j = 0;
    for (int i = 0; i < n; i++) {
      map.put(j++, new Student(i, "test-" + i, "99" + i));
    }
  }
}
