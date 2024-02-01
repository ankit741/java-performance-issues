package com.ankit.performance.tutorial.server;

import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
  public static void main(String[] args) {
    try {
      // At first we create a socket
      Socket clientsock = new Socket("localhost", 8080);
      DataOutputStream out = new DataOutputStream(clientsock.getOutputStream());
      out.writeUTF("Hey this is a client message!");
      out.flush();
      out.close();
      clientsock.close();
      Thread.sleep(2000);
    } catch (Exception e) {
      System.out.println("There is an error in the system!");
      System.out.println(String.format("%s : %s",e.getMessage(),e));
    }
  }
}
