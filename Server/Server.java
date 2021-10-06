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

      // indicates there are no accepted note colours
      if (argv.length < 4) {
        System.out
            .println("Error: There must be at least one note colour, please try again and supply one or more colours.");
        System.exit(1);
      }

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

            // read input (request message) from client, parse it, then service it
            String[] requestData = response.split("_");
            methodType = requestData[0];

            // System.out.println("Client request looks like: " + response);

            // for testing, remove later
            for (int i = 0; i < requestData.length; i++) {
              System.out.println("arg " + i + ": " + requestData[i]);
            }

            if (methodType.equals("POST")) {
              createNote(Integer.parseInt(requestData[1]), Integer.parseInt(requestData[2]),
                  Integer.parseInt(requestData[3]), Integer.parseInt(requestData[4]), requestData[5], requestData[6]);
            } else if (methodType.equals("GET")) {
              if (requestData.length == 2) {
                getNotes("", "", "", 1);
              } else {
                getNotes(requestData[1], requestData[2], requestData[3], 2);
              }
            } else if (methodType.equals("PIN")) {
              pinNote(Integer.parseInt(requestData[1]), Integer.parseInt(requestData[2]));
            } else if (methodType.equals("UNPIN")) {
              unpinNote(Integer.parseInt(requestData[1]), Integer.parseInt(requestData[2]));
            } else if (methodType.equals("SHAKE")) {
              shakeBoard();
            } else if (methodType.equals("CLEAR")) {
              clearBoard();
            }
            // now send the serverResponseMsg back to the client socket
            serverResponseMsg = serverStatusCode + serverReasonPhrase;
            output.println(serverResponseMsg + "\r\n");
          }
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

    public void createNote(int x, int y, int width, int height, String colour, String msg) {

      Note note = new Note(x, y, width, height, colour, msg);
      bBoard.notesOnBoard.add(note);

      System.out.println("A note with lower left x,y coordinates of (" + note.getXCoord() + "," + note.getYCoord()
          + "), height: " + note.getHeight() + ", width: " + note.getWidth() + ", colour: " + note.getNoteColour()
          + ", message: \"" + note.getMessage() + "\" was succesfully created.");
      System.out.println("---------------------------------------------");

    }

    public void getNotes(String contains, String refers, String colour, int type) {
      // type 1 request - all pins
      if (type == 1) {
        for (Note n : bBoard.notesOnBoard) {
          if (n.getPinStatus() == true) {
            output.println("Note with x-coordinate: " + n.getXCoord() + " and y-coordinate: " + n.getYCoord() + " is "
                + n.getNoteColour() + " colour" + " has a message: " + n.getMessage());
          }
        }
      }

      boolean colour_exists = true;
      boolean refers_exists = true;
      boolean coord_exists = true;
      String[] coords = contains.split(" ");
      int XCoord, YCoord;

      // type 2 request - filling in the boxes
      try {
        XCoord = Integer.parseInt(coords[0]);
        YCoord = Integer.parseInt(coords[1]);

        if (contains.equals("ALL"))
          // empty coordinates
          coord_exists = false;
        if (colour.equals("ALL"))
          // empty colour
          colour_exists = false;
        if (refers.equals("ALL"))
          // empty search string target
          refers_exists = false;

        // matching coordinate
        if (coord_exists && !refers_exists && !colour_exists) {
          for (Note n : bBoard.notesOnBoard) {
            if (n.getXCoord() <= XCoord && n.getYCoord() <= YCoord) {
              output.println("Note with x-coordinate: " + n.getXCoord() + " and y-coordinate: " + n.getYCoord() + " is "
                  + n.getNoteColour() + " colour" + " has a message: " + n.getMessage());
            }
          }
        }

        // matching colour
        else if (!coord_exists && !refers_exists && colour_exists) {
          for (Note n : bBoard.notesOnBoard) {
            if (n.getNoteColour().equals(colour)) {
              output.println("Note with x-coordinate: " + n.getXCoord() + " and y-coordinate: " + n.getYCoord() + " is "
                  + n.getNoteColour() + " colour" + " has a message: " + n.getMessage());
            }
          }
        }
        // matching search string
        // else if (!coord_exists && refers_exists && !colour_exists) {
        // for (Note n : bBoard.notesOnBoard) {
        // if (n.getMessage().indexOf(refers) != -1 ? true : false) == true) {
        // output.println("Note with x-coordinate: " + n.getXCoord() + " and
        // y-coordinate: " + n.getYCoord() + " is " + n.getNoteColour() + " colour" +"
        // has a message: " + n.getMessage());
        // }
        // }
        // }

        // matching coordinate and search string
        // else if (coord_exists && refers_exists && !colour_exists) {
        // for (Note n : bBoard.notesOnBoard) {
        // if (n.getXCoord() <= xCoord && n.getYCoord() <= yCoord &&
        // (n.getMessage().indexOf(refers) != -1 ? true : false) == true) {
        // output.println("Note with x-coordinate: " + n.getXCoord() + " and
        // y-coordinate: " + n.getYCoord() + " is " + n.getNoteColour() + " colour" +"
        // has a message: " + n.getMessage());
        // }
        // }
        // }

        // matching coordinate and colour on the board
        else if (coord_exists && !refers_exists && colour_exists) {
          for (Note n : bBoard.notesOnBoard) {
            if (n.getXCoord() <= XCoord && n.getYCoord() <= YCoord && n.getNoteColour().equals(colour)) {
              output.println("Note with x-coordinate: " + n.getXCoord() + " and y-coordinate: " + n.getYCoord() + " is "
                  + n.getNoteColour() + " colour" + " has a message: " + n.getMessage());
            }
          }
        }

        // matching search string and colour
        // else if (!coord_exists && refers_exists && colour_exists) {
        // for (Note n : bBoard.notesOnBoard) {
        // if (&& n.getNoteColour().equals(colour) && (n.getMessage().indexOf(refers) !=
        // -1 ? true : false) == true) {
        // output.println("Note with x-coordinate: " + n.getXCoord() + " and
        // y-coordinate: " + n.getYCoord() + " is "
        // + n.getNoteColour() + " colour" + " has a message: " + n.getMessage());
        // }
        // }
        // }
      } catch (NumberFormatException nfe) {
        System.out.println("Invalid pin data given.");
      }

    }

    public void pinNote(int xCoord, int yCoord) {

      System.out.println("pinned note.");
      System.out.println("---------------------------------------");
      for (Note n : bBoard.notesOnBoard) {
        if (n.getXCoord() <= xCoord && n.getYCoord() <= yCoord) {
          // pin the note requested by the client, increase the number of pins by 1
          // current note
          n.increasePinCount();
          n.setPinStatus("PIN");
          System.out
              .println("Note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord() + ") is pinned.");
          System.out.println("This note has been pinned" + n.getPinnedCount() + "time(s).");
          System.out.println("---------------------------------------");
        } else {
          System.out.println("The note's coordinate is out of range, cannot be pinned.");
          System.out.println("---------------------------------------");
        }
      }
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
          System.out.println("The bulletin board still has " + n.getPinnedCount() + " pinned note(s). ");
          System.out.println("---------------------------------------");
        } else if (n.getXCoord() <= xCoord && n.getYCoord() <= yCoord && n.getPinnedCount() == 1) {
          // unpin the only pinned note
          n.lowerPinCount();
          n.setPinStatus("UNPIN");
          System.out.println("The " + n.getNoteColour() + " note with lower left x,y coordinates (" + n.getXCoord()
              + "," + n.getYCoord() + "), width: " + n.getWidth() + ", height: " + n.getHeight() + " was unpinned.");
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
        System.out.println(n.getNoteColour() + " note with X Coordinate: " + n.getXCoord() + ", Y Coordinate: "
            + n.getYCoord() + ", width: " + n.getWidth() + ", height: " + n.getHeight() + " was removed.");
      }
      System.out.println("---------------------------------------");
    }

    public void clearBoard() {
      System.out.println("---------------------------------------");
      System.out.println("Removing unpinned notes.");
      for (Note n : bBoard.notesOnBoard) {
        if (!n.getPinStatus()) {
          bBoard.notesOnBoard.remove(n);
          System.out
              .println(n.getNoteColour() + " unpinned note with X Coordinate: " + n.getXCoord() + ", Y Coordinate: "
                  + n.getYCoord() + ", width: " + n.getWidth() + ", height: " + n.getHeight() + " was removed.");
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
