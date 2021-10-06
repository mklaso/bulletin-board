import java.util.ArrayList;

public class BulletinBoard {

  // port number, width, height, list of colours
  public int width, height;

  // global array holding all the notes on the board
  public ArrayList<Note> notesOnBoard;

  public BulletinBoard(int width, int height, ArrayList<Note> notesOnBoard) {
    this.width = width;
    this.height = height;
    this.notesOnBoard = notesOnBoard;
    this.pinnedCount = 0; //starts with 0 pinned note
  }
}
