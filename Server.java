import java.io.*;
import java.net.*;
import java.util.*;
//connection error handler
import java.io.IOException;

public class Server {
  // port number that clients and server will use to connect
  public static int portNumber;
  public static int boardWidth;
  public static int boardHeight;
  public static ArrayList<String> availNoteColours = new ArrayList<String>();
  public static BulletinBoard bBoard;
  public static int clientNumber = 0; // default starts at no clients

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

      clientNumber++; // increase total clients connected
      ClientRequest request = new ClientRequest(connection);

      // creates a new thread to service each client request
      Thread reqThread = new Thread(request);

      System.out.println("Client " + clientNumber + " connected to the server.");

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
    private int clientID;

    ClientRequest(Socket socket) {
      this.socket = socket;
      this.clientID = clientNumber;
    }

    public void run() {

      // let client know what colour notes are available once connected
      try {
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        for (int idx = 0; idx < availNoteColours.size(); idx++) {
          output.println(availNoteColours.get(idx)); // send client what note colours are available
        }
        output.flush();

        // keep checking for client requests while client is connected to server
        String response;
        while ((response = input.readLine()) != null) { // not the right condition, should be something else
          synchronized (lock) {
            // critical section
            methodType = response; // just for now, eventually will need to parse the reponse string, can tokenize
                                   // on spaces, where first token is always the methodType, and the rest of the
                                   // tokens are arguments for the methods

            // read input (request message) from client, parse it, then service it

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
              unpinNote(1, 1); // for now just so it compiles
            } else if (methodType.equals("SHAKE")) {
              shakeBoard();
            } else if (methodType.equals("CLEAR")) {
              clearBoard();
            } else {
              // this should send an error response back to the client
              // this is just a placeholder example for now
              System.out.println("Invalid request type, either not recognized orunsupported.");
            }
          }

          // now send the serverResponseMsg back to the client socket
          serverResponseMsg = serverStatusCode + serverReasonPhrase;
          output.println(serverResponseMsg + "\r\n");
        }
        disconnectClient();
      } catch (IOException io) {
        try {
          disconnectClient();
        } catch (Exception e) {
          System.out.println("Socket already closed.");
        }
      } catch (Exception e) {
        // fill in later
      }
    }

    public void createNote() {
      System.out.println("created note.");

    }

    public void getNotes() {
      System.out.println("got notes.");
    }

    public void pinNote() {
      System.out.println("pinned note.");
    }

    public void unpinNote(int xCoord, int yCoord) {
      // xCoord and yCoord come from parsed client request msg

      System.out.println("---------------------------------------");
      for (Note n : bBoard.notesOnBoard) {
        if (n.getXCoord() <= xCoord && n.getYCoord() <= yCoord && n.getPinnedCount() > 1) {
          // unpin the note requested by the client, lower pins if more than 1 pin on
          // current note
          n.lowerPinCount();
          System.out.println(
              "Note with x-coordinate: " + n.getXCoord() + " and y-coordinate: " + n.getYCoord() + " is unpinned.");
          System.out.println("The bulletin board still has " + n.getPinnedCount() + " pinned notes.");
          System.out.println("---------------------------------------");
        } else if (n.getXCoord() <= xCoord && n.getYCoord() <= yCoord && n.getPinnedCount() == 1) {
          // unpin the only pinned note
          n.lowerPinCount();
          n.setPinStatus("UNPIN");
          System.out
              .println("The only " + n.getNoteColour() + "note with xCoordinate: " + n.getXCoord() + "yCoordinate: "
                  + n.getYCoord() + "width: " + n.getWidth() + "height: " + n.getHeight() + " is unpinned.");
          System.out.println("---------------------------------------");
        } else {
          System.out.println("The note is not pinned, unable to unpin it.");
          System.out.println("---------------------------------------");
        }
      }
    }

    public void shakeBoard() {
      System.out.println("---------------------------------------");
      System.out.println("Removing all the notes.");
      for (Note n : bBoard.notesOnBoard) {
        bBoard.notesOnBoard.remove(n);
        System.out.println(n.getNoteColour() + "note with xCoordinate: " + n.getXCoord() + "yCoordinate: "
            + n.getYCoord() + "width: " + n.getWidth() + "height: " + n.getHeight() + " is removed");
      }
      System.out.println("---------------------------------------");
    }

    public void clearBoard() {
      System.out.println("---------------------------------------");
      System.out.println("Removing unpinned notes.");
      for (Note n : bBoard.notesOnBoard) {
        if (!n.getPinStatus()) {
          bBoard.notesOnBoard.remove(n);
          System.out.println(n.getNoteColour() + "unpinned note with xCoordinate: " + n.getXCoord() + "yCoordinate: "
              + n.getYCoord() + "width: " + n.getWidth() + "height: " + n.getHeight() + " is removed");
        }
      }
      System.out.println("---------------------------------------");
    }

    public void disconnectClient() throws IOException {
      input.close();
      output.close();
      socket.close();
      System.out.println("Client " + clientID + " disconnected from the server.");
    }
  }
}
