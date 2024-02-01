package com.ankit.performance.tutorial.server.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BlockingServerThreaded {
  static final String response = "HTTP/1.1 200 OK\r\n\r\nHello world";

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(9090)) {
      while (true) {
        Socket s = serverSocket.accept(); // blocking
        System.out.println(s);
        new Thread(
                () -> {
                  try {
                    handle(s);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                })
            .start();
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new UncheckedIOException(e);
    }
  }

  private static void handle(Socket socket) throws IOException {
    System.out.println(
        Thread.currentThread().getName() + "->state" + Thread.currentThread().getState());
    // Read the input stream, and return “200 OK”
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println(in.readLine());
      OutputStream out = socket.getOutputStream();
      out.write(response.getBytes(StandardCharsets.UTF_8));
    } finally {
      socket.close();
    }
  }
}
