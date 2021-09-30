import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  // private int portNumber;
  // private String IPAddress;

  public static void main(String argv[]) throws Exception {

    // when implemented, this should probably just call the GUI to launch

    // setup will be: parse text from IP Address and Port Number textfields upon
    // hitting the "connect" button on the GUI

    // just an example to test if client and server can connect properly
    try {
      Socket socket = new Socket("192.168.100.183", 55555); // ip-addr, port-num for now
      System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
      Scanner scanner = new Scanner(System.in);
      Scanner in = new Scanner(socket.getInputStream());
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      while (scanner.hasNextLine()) {
        out.println(scanner.nextLine());
        System.out.println(in.nextLine());
      }
    } catch (Exception e) {

    } finally {

    }

    // for now just to test if the connection to the server works:

  }

}