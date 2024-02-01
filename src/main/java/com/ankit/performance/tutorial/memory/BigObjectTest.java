package com.ankit.performance.tutorial.memory;

import com.ankit.performance.tutorial.memory.model.BigObject;
import java.util.ArrayList;
import java.util.List;

public class BigObjectTest {

  public static void main(String[] args) {
    new Thread(
            () -> {
              List largeObjects1 = new ArrayList();
              List largeObjects2 = new ArrayList();

              for (int i = 0; i < 1000; i++) {
                BigObject largeObject = new BigObject(i);
                largeObjects1.add(largeObject);
                largeObjects2.add(largeObject);
              }
              System.out.println("See the results in profiler ::");
              try {
                Thread.sleep(Long.MAX_VALUE);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
