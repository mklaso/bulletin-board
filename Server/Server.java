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
    private String outputMsg = "";
    private BufferedReader input;
    private PrintWriter output;
    private final Object lock = new Object(); // basically mutex lock setup for synchronization
    private int clientID;
    private ArrayList<Pin> pinList = new ArrayList<Pin>(); // keep track of pins to make sure no
                                                           // duplicates

    ClientRequest(Socket socket) {
      this.socket = socket;
      this.clientID = clientNumber;
    }

    public void run() {

      // let client know what colour notes are available once connected
      try {
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        output.print("[SERVER]: Board width and height are: (" + bBoard.width + "," + bBoard.height + "). ");
        output.flush();
        output.println("Accepted colours are:");
        for (int idx = 0; idx < availNoteColours.size(); idx++) {
          output.println(availNoteColours.get(idx)); // send client what note colours are available
        }
        output.flush();

        // keep checking for client requests while client is connected to server
        String response;
        while ((response = input.readLine()) != null) {
          synchronized (lock) {
            // critical section
            output = new PrintWriter(socket.getOutputStream(), true);
            // read input (request message) from client, parse it, then service it
            String[] requestData = response.split("_");
            methodType = requestData[0];

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
            serverResponseMsg = serverStatusCode + ": " + serverReasonPhrase + outputMsg;
            output.println("[SERVER]: " + serverResponseMsg);
            output.flush();

            // reset output msg for the next request
            outputMsg = "";
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
        System.out.println("Error: " + e);
        System.out.println("Unexpected error has occurred.");
      }
    }

    public void createNote(int x, int y, int width, int height, String colour, String msg) {

      if (x < 0 || x > bBoard.width || y < 0 || y > bBoard.height) {
        serverStatusCode = "400 - Bad Request";
        serverReasonPhrase = "Note could not be created. ";
        outputMsg += "Note coordinates are outside the bulletin board area. Try again after changing the x,y coordinates of the note. ";
      } else {
        serverStatusCode = "201 - Created";
        serverReasonPhrase = "Note was successfully created. ";

        Note note = new Note(x, y, width, height, colour, msg);
        bBoard.notesOnBoard.add(note);

        outputMsg += "A note with lower left x,y coordinates of (" + note.getXCoord() + "," + note.getYCoord()
            + "), width: " + note.getWidth() + ", height: " + note.getHeight() + ", colour: " + note.getNoteColour()
            + ", message: \"" + note.getMessage() + "\" was created.";
      }
    }

    public void getNotes(String contains, String refers, String colour, int type) {
      // type 1 request - all pins
      if (type == 1) {
        serverStatusCode = "200 - OK";
        serverReasonPhrase = "GET request was successful and notes have been found.";

        for (Note n : bBoard.notesOnBoard) {
          if (n.getPinStatus() == true) {
            outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
          }
        }
      } else {
        // type 2 request - filling in the boxes
        boolean coloursAll = false;
        boolean refersAll = false;
        boolean coordsAll = false;
        int XCoord = 0;
        int YCoord = 0;
        boolean valid = true;

        if (contains.equals("ALL")) {
          // empty coordinates
          coordsAll = true;
        } else {
          try {
            String[] coords = contains.split(" ");
            // there should be two parameters x,y
            if (coords.length != 2) {
              valid = false;
            } else {
              XCoord = Integer.parseInt(coords[0]);
              YCoord = Integer.parseInt(coords[1]);
            }
          } catch (NumberFormatException nfe) {
            serverStatusCode = "400 - Bad Request";
            serverReasonPhrase = "GET request failed. ";
            outputMsg += "Contains= coordinates syntax is incorrect. Please enter valid numbers for x,y; or leave them blank.";
            valid = false;
          }
        }
        if (colour.equals("ALL")) {
          // empty colour
          coloursAll = true;
        }

        if (refers.equals("ALL")) {
          // empty search string target
          refersAll = true;
        }

        if (valid) {

          serverStatusCode = "200 - OK";
          serverReasonPhrase = "GET request was successful and notes have been found. ";

          // ALL - every note on board
          if (coordsAll && coloursAll && refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                  + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
            }
            // specific coords, specific colour, specific refers
          } else if (!coordsAll && !coloursAll && !refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if ((XCoord == n.getXCoord() && YCoord == n.getYCoord()) && (colour.equals(n.getNoteColour()))
                  && n.getMessage().contains(refers)) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
            // specific coords, all colours, all refers
          } else if (!coordsAll && coloursAll && refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if (XCoord == n.getXCoord() && YCoord == n.getYCoord()) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
            // all coords, specific colour, all refers
          } else if (coordsAll && !coloursAll && refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if (colour.equals(n.getNoteColour())) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
            // all coords, all colours, specific refers
          } else if (coordsAll && coloursAll && !refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if (n.getMessage().contains(refers)) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
            // specific coords, specific colour, all refers
          } else if (!coordsAll && !coloursAll && refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if ((XCoord == n.getXCoord() && YCoord == n.getYCoord()) && (colour.equals(n.getNoteColour()))) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }

            }
            // specific coords, all colours, specific refers
          } else if (!coordsAll && coloursAll && !refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if ((XCoord == n.getXCoord() && YCoord == n.getYCoord()) && n.getMessage().contains(refers)) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
            // all coords, specific colour, specific refer
          } else if (coordsAll && !coloursAll && !refersAll) {
            for (Note n : bBoard.notesOnBoard) {
              if ((colour.equals(n.getNoteColour())) && n.getMessage().contains(refers)) {
                outputMsg += "Found note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                    + "), colour: " + n.getNoteColour() + ", with the message: \"" + n.getMessage() + "\". ";
              }
            }
          }
        } else {
          serverStatusCode = "400 - Bad Request";
          serverReasonPhrase = "GET request failed. ";
          outputMsg += "Contains= coordinates syntax is incorrect. Please enter valid numbers for x,y; or leave them blank.";
        }
      }
    }

    public void pinNote(int xCoord, int yCoord) {
      Boolean exists = false;

      // check if there are notes on the board first before pinning
      serverStatusCode = " 200 - OK";
      serverReasonPhrase = "PIN request was successful. ";

      for (int i = 0; i < pinList.size(); i++) {
        if (xCoord == pinList.get(i).x && yCoord == pinList.get(i).y) {
          serverStatusCode = "200 - OK";
          serverReasonPhrase = "No notes pinned. ";
          outputMsg += "Pin already exists in the current (x,y) position on the board. ";
          exists = true;
          break;
        }
      }
      if (exists == false) {
        Boolean initial = true;
        for (Note n : bBoard.notesOnBoard) {
          if (xCoord >= n.getXCoord() && xCoord <= n.getXCoord() + n.getWidth() && yCoord >= n.getYCoord()
              && yCoord <= n.getYCoord() + n.getHeight()) {
            // pin the note requested by the client, increase the number of pins by 1
            // current note

            serverStatusCode = " 200 - OK";
            serverReasonPhrase = "PIN request was successful and the note is pinned. ";
            n.increasePinCount();
            n.setPinStatus("PIN");
            // add new pin to list only once, but continue updating pin count for all notes
            // in the area
            if (initial) {
              pinList.add(new Pin(xCoord, yCoord));
            }
            outputMsg += "Note with lower left x,y coordinates (" + n.getXCoord() + "," + n.getYCoord()
                + ") is pinned.";
            System.out.println("Note was pinned. Pin count is: " + n.getPinnedCount());

          } else {
            System.out.println("The note's coordinate is out of range, cannot be pinned."); // fix this to be a
                                                                                            // different
                                                                                            // response msg
          }
        }
      }
    }

    public void unpinNote(int xCoord, int yCoord) {
      // xCoord and yCoord come from parsed client request msg
      serverStatusCode = " 200 - OK";
      serverReasonPhrase = "UNPIN request was successful, notes unpinned. ";
      for (Note n : bBoard.notesOnBoard) {
        if (xCoord >= n.getXCoord() && xCoord <= n.getXCoord() + n.getWidth() && yCoord >= n.getYCoord()
            && yCoord <= n.getYCoord() + n.getHeight() && n.getPinnedCount() > 1) {
          // unpin the note requested by the client, lower pins if more than 1 pin on
          // current note
          for (int i = 0; i < pinList.size(); i++) {
            if (xCoord == pinList.get(i).x && yCoord == pinList.get(i).y) {
              pinList.remove(i);
              break;
            }
          }

          n.lowerPinCount();
          outputMsg += "The " + n.getNoteColour() + " note with lower left x,y coordinates (" + n.getXCoord() + ","
              + n.getYCoord() + "), width: " + n.getWidth() + ", height: " + n.getHeight() + " was unpinned. ";
          System.out.println("Note was unpinned. Pin count is: " + n.getPinnedCount());
        } else if (xCoord >= n.getXCoord() && xCoord <= n.getXCoord() + n.getWidth() && yCoord >= n.getYCoord()
            && yCoord <= n.getYCoord() + n.getHeight() && n.getPinnedCount() == 1) {
          // unpin the only pinned note
          for (int i = 0; i < pinList.size(); i++) {
            if (xCoord == pinList.get(i).x && yCoord == pinList.get(i).y) {
              pinList.remove(i);
              break;
            }
          }
          n.lowerPinCount();
          n.setPinStatus("UNPIN");
          outputMsg += "The " + n.getNoteColour() + " note with lower left x,y coordinates (" + n.getXCoord() + ","
              + n.getYCoord() + "), width: " + n.getWidth() + ", height: " + n.getHeight() + " was unpinned. ";
          System.out.println("Note's last pin was removed. Pin count is: " + n.getPinnedCount());
        } else {
          System.out.println("The note is not pinned, unable to unpin it."); // fix this to be a different response msg
        }
      }
    }

    public void shakeBoard() {
      serverStatusCode = "200 - OK";
      serverReasonPhrase = "SHAKE request was successful, and unpinned notes were removed. ";

      int numNotes = bBoard.notesOnBoard.size();
      for (int i = numNotes - 1; i >= 0; i--) {
        if (bBoard.notesOnBoard.get(i).getPinStatus() == false) {
          bBoard.notesOnBoard.remove(i);
        }
      }
    }

    public void clearBoard() {
      serverStatusCode = "200 - OK";
      serverReasonPhrase = "CLEAR request was successful. ";
      System.out.println("Removing all notes.");

      if (bBoard.notesOnBoard.size() == 0) {
        outputMsg += "Board is already empty. ";
      } else {
        bBoard.notesOnBoard.clear();
        pinList.clear();
        outputMsg += "All notes, and pins were removed from the board. ";
      }

    }

    public void disconnectClient() throws IOException {
      input.close();
      output.close();
      socket.close();
      System.out.println("Client " + clientID + " disconnected from the server.");
    }
  }
}
