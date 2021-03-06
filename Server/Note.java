
public class Note {

  private String message, noteColour;
  private int xCoord, yCoord, width, height, pinnedCount;
  private Boolean isPinned;

  public Note(int xCoord, int yCoord, int width, int height, String noteColour, String message) {
    this.message = message;
    this.noteColour = noteColour;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.width = width;
    this.height = height;
    this.isPinned = false; // note defaults to unpinned
    this.pinnedCount = 0; // starts with 0 pinned note
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

  public void lowerPinCount() {
    if (this.pinnedCount == 0) {
      this.pinnedCount = 0;
    } else {
      this.pinnedCount--;
    }
  }

  public void increasePinCount() {
    this.pinnedCount++;
  }

  // note getters
  public int getXCoord() {
    return this.xCoord;
  }

  public int getYCoord() {
    return this.yCoord;
  }

  public String getNoteColour() {
    return this.noteColour;
  }

  public String getMessage() {
    return this.message;
  }

  public int getHeight() {
    return this.height;
  }

  public int getWidth() {
    return this.width;
  }

  public Boolean getPinStatus() {
    return this.isPinned;
  }

  public int getPinnedCount() {
    return this.pinnedCount;
  }
}
