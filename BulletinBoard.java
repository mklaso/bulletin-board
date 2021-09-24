import java.util.ArrayList;

public class BulletinBoard {

  // port number, width, height, list of colours
  private int width, height;
  private ArrayList<String> availNoteColours = new ArrayList<String>();

  // global array holding all the notes on the board
  public static ArrayList<Note> notesOnBoard = new ArrayList<Note>();

  BulletinBoard(int width, int height, ArrayList<String> availNoteColours) {
    this.width = width;
    this.height = height;
    this.availNoteColours = availNoteColours;
  }
}
