package com.ankit.performance.tutorial.memory.model;

public class BigObject {
  private final byte[] byteArray = new byte[1024 * 1024];
  private int id = 0;
  private String objectName;

  public BigObject (int id) {
    this.objectName = "O " + id;
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
