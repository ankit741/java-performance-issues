package com.ankit.performance.tutorial.thread;

public class Thread2 {

  public static void main(String[] args) throws InterruptedException {
    Thread thread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                // Code that will run in a new thread
                throw new RuntimeException("Intentional Exception");
              }
            });

    thread.setName("Misbehaving thread");

    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
          @Override
          public void uncaughtException(Thread t, Throwable e) {
            System.out.println(
                "A critical error happened in thread "
                    + t.getName()
                    + " the error is "
                    + e.getMessage());
          }
        });
    thread.start();
    Thread.sleep(2000);
    System.out.println("main thread executing");

  }
}
