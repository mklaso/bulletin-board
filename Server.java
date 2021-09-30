import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  // port number that clients and server will use to connect
  public static int portNumber;

  public static void main(String argv[]) throws Exception {

    // get port number used to run server on from cmd line
    try {
      portNumber = Integer.parseInt(argv[0]);
    } catch (NumberFormatException nfe) {
      System.out.println("Error: The first argument must be an integer representing the port number.");
      System.exit(1);
    }

    System.out.println("port number is: " + portNumber + "\n");

    // listening socket, clients can try to connect
    ServerSocket socket = new ServerSocket(portNumber);

    System.out.println("port number isasgfsdgsdg: " + portNumber + "\n");

    while (true) {
      // listen for connection request
      Socket connection = socket.accept();

      ClientRequest request = new ClientRequest(connection);

      // creates a new thread to service each client request
      Thread reqThread = new Thread(request);

      // start the thread
      reqThread.start();

    }

  }

  private static class ClientRequest implements Runnable {
    private Socket socket;
    private String methodType;
    private String serverStatusCode; // set the code [e.g 201, 400, etc.] according to the client request
    private String serverReasonPhrase; // set the information associated with the status code [e.g "Note was created
                                       // successfully."]
    private String serverResponseMsg; // combination of the two above [e.g "201 - Created, Note was created
                                      // successfully."]
    private final Object lock = new Object(); // basically mutex lock setup for synchronization

    ClientRequest(Socket socket) {
      this.socket = socket;
    }

    public void run() {
      System.out.println("inside run() funtion...\n");
      try {
        Scanner in = new Scanner(socket.getInputStream());
        // BufferedReader input = new BufferedReader(new
        // InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        while (in.hasNextLine()) {
          output.println(in.nextLine().toUpperCase());
        }

      } catch (Exception e) {
        System.out.println("Error: " + socket);
      } finally {
        try {
          socket.close();
        } catch (IOException e) {
        }
      }

      // main code goes here, setup should probably be something like:
      // parse the stream output from the client socket, determine what the method
      // type from the request is

      synchronized (lock) {
        // critical section
        serverStatusCode = "200"; // default "OK" message, only 201 for POST
        if (methodType.equals("POST")) {
          createNote();
          serverStatusCode = "201";
        } else if (methodType.equals("GET")) {
          getNotes();
        } else if (methodType.equals("PIN")) {
          pinNote();
        } else if (methodType.equals("UNPIN")) {
          unpinNote();
        } else if (methodType.equals("SHAKE")) {
          shakeBoard();
        } else if (methodType.equals("CLEAR")) {
          clearBoard();
        } else if (methodType.equals("DISCONNECT")) {
          disconnectClient();
        } else {
          // this should send an error response back to the client
          // this is just a placeholder example for now
          System.out.println("Invalid request type, either not recognized or unsupported.");
        }
      }

      // now send the serverResponseMsg back to the client socket
      serverResponseMsg = serverStatusCode + serverReasonPhrase;

      // output to socket part here

    }

    public void createNote() {

    }

    public void getNotes() {

    }

    public void pinNote() {

    }

    public void unpinNote() {

    }

    public void shakeBoard() {

    }

    public void clearBoard() {

    }

    public void disconnectClient() {

    }
  }
}
