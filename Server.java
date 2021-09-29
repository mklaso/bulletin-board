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

    // listening socket, clients can try to connect
    ServerSocket socket = new ServerSocket(portNumber);

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
    private String serverReasonPhrase; // set the information associated with the status code [e.g "Note was created successfully."]
    private String serverResponseMsg; // combination of the two above [e.g "201 - Created, Note was created successfully."]

    ClientRequest(Socket socket) {
      this.socket = socket;
    }

    public void run() {

      // main code goes here, setup should probably be something like:
      // parse the stream output from the client socket, determine what the method
      // type from the request is
      // once method type is known, execute the request ( bunch of if statements, then
      // call on methods to do the request )

      if (methodType.equals("POST")) {
        createNote();
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
        // this should send an error response back to the client, the print statement
        // here is just a placeholder for now
        System.out.println("Invalid request type, either not recognized or unsupported.");
      }

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
