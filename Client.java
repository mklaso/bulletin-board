import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  // private int portNumber;
  // private String IPAddress;

  public static void main(String argv[]) throws Exception {

    ClientGUI clientGUI = new ClientGUI();
    clientGUI.startGUI();

    // when implemented, this should probably just call the GUI to launch

    // setup will be: parse text from IP Address and Port Number textfields upon
    // hitting the "connect" button on the GUI

    // just an example to test if client and server can connect properly
    // try {
    // Socket socket = new Socket("192.168.100.183", 55555); // ip-addr, port-num
    // for now
    // System.out.println("[CLIENT]: Successfully connected to server at port:
    // 55555.\n");

    // BufferedReader in = new BufferedReader(new
    // InputStreamReader(socket.getInputStream()));
    // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    // // read server response and display to client window
    // String response;
    // while ((response = in.readLine()) != null) {
    // System.out.println("[SERVER]: " + response);
    // }

    // } catch (Exception e) {
    // System.out.println("Problem connecting to server.\n");
    // System.exit(1);
    // } finally {

    // }
  }

}