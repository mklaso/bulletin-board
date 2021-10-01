import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  // port number that clients and server will use to connect
  public static int portNumber;
  public static int boardWidth;
  public static int boardHeight;
  public static ArrayList<String> availNoteColours = new ArrayList<String>();
  public static BulletinBoard bBoard;

  public static void main(String argv[]) throws Exception {

    // parse cmd arguments and build bulletin board object / note colour list
    try {
      portNumber = Integer.parseInt(argv[0]);
      boardWidth = Integer.parseInt(argv[1]);
      boardHeight = Integer.parseInt(argv[2]);

      // 3rd argument should be start of note colour(s)
      for (int idx = 3; idx < argv.length; idx++) {
        availNoteColours.add(argv[idx]); // add specified note colours to available note colours for clients to use
      }

    } catch (NumberFormatException nfe) {
      System.out.println(
          "Error: The first three arguments must be integers representing the port number, followed by the width and height of the bulletin board.");
      System.exit(1);
    } catch (Exception e) {
      System.out.println(
          "Missing argument(s), please try again and provide a port number, width, height, and colour(s) for the notes.");
      System.exit(1);
    }

    // create global bulletin board with the chosen width, height from cmd
    bBoard = new BulletinBoard(boardWidth, boardHeight, new ArrayList<Note>());

    // listening socket, clients can try to connect
    ServerSocket socket = new ServerSocket(portNumber);
    System.out.println("Server is running and accepting connections at port number: " + portNumber + ".\n");

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
    private String methodType = "";
    private String serverStatusCode = ""; // set the code [e.g 201, 400, etc.] according to the client request
    private String serverReasonPhrase = ""; // set the information associated with the status code [e.g "Note was
                                            // created
    // successfully."]
    private String serverResponseMsg = ""; // combination of the two above [e.g "201 - Created, Note was created
    // successfully."]
    private BufferedReader input;
    private PrintWriter output;
    private final Object lock = new Object(); // basically mutex lock setup for synchronization

    ClientRequest(Socket socket) {
      this.socket = socket;
    }

    public void run() {

      // let client know what colour notes are available once connected
      try {
        output = new PrintWriter(socket.getOutputStream(), true);

        output.print("The available note colours for use are: ");
        for (int idx = 0; idx < availNoteColours.size() - 1; idx++) {
          output.print(availNoteColours.get(idx) + ", "); // add specified note colours to available note colours for
                                                          // clients to use
        }
        output.print(
            availNoteColours.get(availNoteColours.size() - 1) + ".\nIMPORTANT - Note colours are case sensitive.\n");

      } catch (IOException io) {
        System.out.println("Error: " + socket);
      }

      // main code goes here, setup should probably be something like:
      // parse the stream output from the client socket, determine what the method
      // type from the request is

      synchronized (lock) {
        // critical section

        // read input (request message) from client and try to parse it, then service it

        // try {
        // input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // } catch (IOException io) {

        // }

        serverStatusCode = "200"; // default "OK" message, only 201 for POST
        serverReasonPhrase = " - OK: WORKING (FOR TESTING PURPOSES CURRENTLY)";
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
      output.println(serverResponseMsg);
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
