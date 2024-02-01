package com.ankit.performance.tutorial.server.nio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOBlockingServer {
  static final String response = "HTTP/1.1 200 OK\r\n\r\nHello world";

  public static void main(String[] args) {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.bind(new InetSocketAddress(9090));
      while (true) {
        SocketChannel s = channel.accept(); // blocking and never null
        System.out.println(s);
        handle(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new UncheckedIOException(e);
    }
  }

  private static void handle(SocketChannel s) throws IOException {
    System.out.println(
        Thread.currentThread().getName() + "->state" + Thread.currentThread().getState());
    ByteBuffer buf = ByteBuffer.allocateDirect(80);
    int data;
    while ((data = s.read(buf)) != -1) {
      buf.flip();
      while (buf.hasRemaining()) {
        s.write(buf);
      }
      buf.compact();
    }
  }
}
