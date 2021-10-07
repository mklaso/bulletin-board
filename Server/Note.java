public class Note {

  private String message, noteColour;
  private int xCoord, yCoord, width, height, pinnedCount;
  private Boolean isPinned;

  public Note(int xCoord, int yCoord, int width, int height, String noteColour, String message, HashMap<Integer,Integer> pinnList) {
    this.message = message;
    this.noteColour = noteColour;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.width = width;
    this.height = height;
    this.isPinned = false; // note defaults to unpinned
    this.pinnedCount = 0; // starts with 0 pinned note
    this.pinList = new HashMap<Integer,Integer>();
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
  
  public void addTopinList(HashMap<Integer,Integer> pinnList, int xCoord, int yCoord, String status) {
    if (this.pinnedCount >= 0 && !pinnList.containsValue(yCoord)) {
      pinnList.put(xCoord, yCoord);
      setPinStatus(String status);
      increasePinCount();
    }      
  }

  public void removeFrompinList(HashMap<Integer,Integer> pinnList, int xCoord, int yCoord, String status) {
    if (this.pinnedCount > 0) {
      pinnList.remove(xCoord, yCoord);
      setPinStatus(String status);
      lowerPinCount();
    } else {
      System.out
            .println("Error: Cannot remove note that does not exist.");
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

  // note setters
  public void setXCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setYCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public void setNoteColour(String noteColour) {
    this.noteColour = noteColour;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setWidth(int width) {
    this.width = width;
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
