package com.ankit.performance.tutorial.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Two categories of Sockets: A server socket - It awaits a request from a client. A client socket -
 * It establishes communication between client and server. The client has to know two things about
 * the server:
 *
 * <p>The server’s IP address. The port number.
 */
public class Server {
  static final String response = "HTTP/1.1 200 OK\r\n\r\nHello world";
  public static void main(String[] args) throws IOException {

    Thread server =
        new Thread(
            () -> {
              ServerSocket s = null;
              try {
                s = new ServerSocket(8080);
              } catch (IOException e) {
                e.printStackTrace();
              }
              try {
                while (true) {
                  // This creates a new socket at port 8080.
                  Socket sock = s.accept();
                 // Thread.sleep(2000);
                  DataInputStream dis = new DataInputStream(sock.getInputStream());
                  String sockmsg = (String) dis.readUTF();
                  System.out.println("The message received is " + sockmsg);
                  // Now we close the socket
                  s.close();
                }
                //
              } catch (Exception e) {
                System.out.println("There was an error with the file."+e);
              }
            });

    Thread Monitor =
        new Thread(
            () -> {
              while (true) {
                System.out.println("Monitor thread state :" + Thread.currentThread().getState());
                System.out.println("server thread state :" + server.currentThread().getState());
                try {
                  Thread.sleep(3000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });

    server.start();
    Monitor.start();
  }
}
