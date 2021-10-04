public class Note {

  private String message, noteColour;
  private int xCoord, yCoord, width, height;
  private Boolean isPinned;

  public Note(String message, String noteColour, int xCoord, int yCoord, int width, int height) {
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

  // note setters
  public void setxCoord(int xCoord) {
    return this.xCoord = xCoord;
  }

  public void setyCoord(int yCoord) {
    return this.yCoord = yCoord;
  }

  public void setnoteColour(String noteColour) {
    return this.noteColour = noteColour;
  }

  public void setmessage(String message) {
    return this.message = message;
  }

  public void setheight(int height) {
    return this.height = height;
  }

  public void setwidth(int width) {
    return this.width = width;
  }

  // note getters
  public void getxCoord() {
    return xCoord;
  }

  public void getyCoord() {
    return yCoord;
  }

  public void getnoteColour() {
    return noteColour;
  }

  public void getmessage() {
    return message;
  }

  public void getheight() {
    return height;
  }

  public void getwidth() {
    return width;
  }
}
