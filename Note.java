public class Note {

  private String message, noteColour;
  private int xCoord, yCoord, width, height;
  private Boolean isPinned;

  Note(String message, String noteColour, int xCoord, int yCoord, int width, int height) {
    this.message = message;
    this.noteColour = noteColour;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.width = width;
    this.height = height;
    this.isPinned = false; // note defaults to unpinned
  }

  /**
   * Sets the note's pin status: pinned | unpinned
   * 
   * @param status - determines if note should change current status
   */
  public void setPinStatus(String status) {
    if (status.equals("PIN")) {
      this.isPinned = true;
    } else {
      this.isPinned = false;
    }
  }
}
