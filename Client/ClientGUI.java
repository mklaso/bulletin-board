import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class ClientGUI {
  private Socket socket;
  JTextArea clientOutputBox = new JTextArea(12, 1);
  JComboBox<String> colourCombo = new JComboBox<>(); // holds avail. note colours
  JFrame window;
  PrintWriter output;
  BufferedReader input;
  String SP = " "; // used for sending requests to server

  public ClientGUI() {
    this.window = new JFrame();
    this.startGUI();
  }

  public void startGUI() {
    window.setSize(880, 700);
    window.setTitle("Bulletin Board Client Application");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    setupGUI();
    window.setVisible(true);
  }

  JTextField xField = new JTextField();
  JTextField yField = new JTextField();
  JTextField widthField = new JTextField();
  JTextField heightField = new JTextField();
  JTextField msgField = new JTextField();
  JTextField refersField = new JTextField();
  JTextField containsField = new JTextField();
  JTextField colourField = new JTextField();
  JTextField xGetField = new JTextField();
  JTextField yGetField = new JTextField();
  JCheckBox allPinsCheck = new JCheckBox("ALL PINS");

  // adding all the components to GUI, setup controller logic
  public void setupGUI() {

    JLabel portNumLabel = new JLabel("Port Number");
    JLabel IPAddrLabel = new JLabel("IP Address");
    JTextField portNumField = new JTextField();
    JTextField IPAddrField = new JTextField();
    JButton disconnectBtn = new JButton("Disconnect");
    JButton connectBtn = new JButton("Connect");
    JLabel xLabel = new JLabel("X Coordinate");
    JLabel yLabel = new JLabel("Y Coordinate");
    JLabel widthLabel = new JLabel("Width");
    JLabel heightLabel = new JLabel("Height");

    JLabel msgLabel = new JLabel("Message");

    JLabel colourLabel = new JLabel("Colour");
    JLabel refersLabel = new JLabel("RefersTo=");
    JLabel containsLabel = new JLabel("Contains=");
    JLabel colourLabel2 = new JLabel("Colour=");

    JButton getBtn = new JButton("GET");

    JButton shakeBtn = new JButton("SHAKE");
    JButton clearBtn = new JButton("CLEAR");
    JScrollPane jScrollPane1 = new JScrollPane();
    JButton postBtn = new JButton("POST");
    JButton pinBtn = new JButton("PIN");
    JButton unpinBtn = new JButton("UNPIN");
    JSeparator jSeparator1 = new JSeparator();
    JSeparator jSeparator2 = new JSeparator();
    JSeparator jSeparator3 = new JSeparator();
    JLabel xLabelGet = new JLabel("X");
    JLabel yLabelGet = new JLabel("Y");

    disconnectBtn.setEnabled(false);
    connectBtn.setPreferredSize(new Dimension(135, 35));

    // allow these options for status, colour in GET section
    colourCombo.setBorder(null);

    allPinsCheck.setFont(new Font("Tahoma", 1, 12));
    jScrollPane1.setViewportView(clientOutputBox);

    // making it look nice and organized
    GroupLayout layout = new GroupLayout(window.getContentPane());
    window.getContentPane().setLayout(layout);
    layout
        .setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3, GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup().addGroup(layout
                .createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup()
                    .addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(yLabelGet, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 18,
                            GroupLayout.PREFERRED_SIZE)
                        .addComponent(xLabelGet, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                    .addGap(4, 4, 4)
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(
                                xGetField, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                            .addComponent(yGetField, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18).addComponent(pinBtn).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(
                        unpinBtn)
                    .addGap(39, 39, 39).addComponent(shakeBtn).addGap(10, 10, 10).addComponent(clearBtn)
                    .addGap(12, 12, 12))
                .addGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150).addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(
                                    GroupLayout.Alignment.LEADING,
                                    layout.createSequentialGroup()
                                        .addComponent(msgField, GroupLayout.PREFERRED_SIZE, 356,
                                            GroupLayout.PREFERRED_SIZE)
                                        .addGap(69, 69, 69).addComponent(postBtn, GroupLayout.PREFERRED_SIZE, 108,
                                            GroupLayout.PREFERRED_SIZE))
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(yLabel)
                                            .addGroup(layout.createSequentialGroup().addGap(95, 95, 95).addComponent(
                                                yField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
                                        .addGap(40, 40, 40)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(widthLabel, GroupLayout.Alignment.TRAILING,
                                                GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(heightLabel))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(heightField, GroupLayout.PREFERRED_SIZE, 81,
                                                GroupLayout.PREFERRED_SIZE)
                                            .addComponent(widthField, GroupLayout.PREFERRED_SIZE, 81,
                                                GroupLayout.PREFERRED_SIZE))
                                        .addGap(190, 190, 190)))
                            .addGroup(layout.createSequentialGroup().addComponent(xLabel).addGap(17, 17, 17)
                                .addComponent(xField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                .addGap(214, 214, 214)
                                .addComponent(colourLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18,
                                    18)
                                .addComponent(colourCombo, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup().addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(refersLabel)
                                    .addComponent(containsLabel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(refersField, GroupLayout.PREFERRED_SIZE, 81,
                                            GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37).addComponent(colourLabel2).addGap(18, 18, 18).addComponent(
                                            colourField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(
                                        layout.createSequentialGroup()
                                            .addComponent(containsField, GroupLayout.PREFERRED_SIZE, 81,
                                                GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(allPinsCheck)))
                                .addGap(76, 76, 76)
                                .addComponent(getBtn, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                            .addComponent(msgLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(portNumLabel, GroupLayout.PREFERRED_SIZE, 92,
                                            GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                            portNumField, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(IPAddrLabel, GroupLayout.PREFERRED_SIZE, 92,
                                            GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                            IPAddrField, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)))
                                .addGap(29, 29, 29)
                                .addComponent(connectBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18).addComponent(disconnectBtn))))
                    .addGroup(layout.createSequentialGroup().addGap(142, 142, 142).addComponent(jScrollPane1,
                        GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 152, Short.MAX_VALUE)));
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING,
            layout.createSequentialGroup().addContainerGap(35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(portNumLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(portNumField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(IPAddrLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(IPAddrField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
                    .addComponent(connectBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disconnectBtn, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 56,
                        GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(xLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(xField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addComponent(colourLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(colourCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(yField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(widthLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(widthField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(heightLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(heightField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(msgLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(msgField, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                    .addComponent(postBtn, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup().addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(refersLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(colourLabel2, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(refersField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addComponent(colourField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(containsLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                            .addComponent(containsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addComponent(allPinsCheck)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(getBtn, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addComponent(
                    jSeparator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(clearBtn)
                        .addComponent(shakeBtn).addComponent(unpinBtn).addComponent(pinBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(xLabelGet, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(xGetField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yLabelGet, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(yGetField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))))
                .addGap(8, 8, 8).addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)));

    window.pack();

    // style components used in GUI - mainly buttons/text
    Container parent = window.getContentPane();
    for (Component comp : parent.getComponents()) {
      if (comp instanceof JLabel) {
        comp.setFont(new Font("Tahoma", 1, 12));
      } else if (comp instanceof JTextField) {
        comp.setFont(new Font("Tahoma", 0, 12));
      } else if (comp instanceof JButton) {
        comp.setBackground(new Color(255, 255, 255));
        comp.setFont(new Font("Segoe UI", 1, 20));
        JButton temp = (JButton) comp;
        temp.setBorderPainted(false);
      }
    }

    // action listeners/event handlers (follow same format for pretty much every
    // request button, but call sendRequest() instead)
    connectBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Boolean isConnected = connect(IPAddrField.getText(), portNumField.getText());
        // only apply GUI changes if client connects successfully
        if (isConnected) {
          connectBtn.setEnabled(false);
          disconnectBtn.setEnabled(true);
          connectBtn.setText("Connected");
        }
      }
    });

    disconnectBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        disconnect();
      }
    });

    postBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("POST");
      }
    });

    getBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("GET");
      }
    });

    shakeBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("SHAKE");
      }
    });

    clearBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("CLEAR");
      }
    });

    pinBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("PIN");
      }
    });

    unpinBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendRequest("UNPIN");
      }
    });

    window.pack();
  }

  /**
   * Attempt to connect to the server that is listening for
   * connections @portNumber and @IPAddress
   * 
   * @param IPAddress  - ip address to connect to server
   * @param portNumber - port number to connect to server
   * @return true if client connected to server, false on error
   */
  public Boolean connect(String IPAddress, String portString) {
    try {
      // try to connect to server
      int portNumber = Integer.parseInt(portString);
      Boolean connected = false;
      socket = new Socket();
      // 3 second timeout when trying to connect to wrong/invalid IP
      socket.connect(new InetSocketAddress(IPAddress, portNumber), 3000);
      connected = true;
      // only read from server if socket successfully connects
      if (connected) {
        try {
          // socket is connected, get the accepted note colours from server
          input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          clientOutputBox.append(" [CLIENT]: Successfully connected to server at port: " + portNumber + ".\n");
          clientOutputBox.append(" [SERVER]: The available note colours for use are: ");
          // read initial server response and display to client window
          String response;
          while (input.ready() && (response = input.readLine()) != null) {
            clientOutputBox.append(response + " ");
            colourCombo.addItem(response);
          }
          clientOutputBox.append("\n [SERVER]: IMPORTANT - Note colours are case sensitive.");
        } catch (Exception e) {
          displayErrorMsg("Server response reading error.");
          return false;
        }
      }
    } catch (NumberFormatException nfe) {
      displayErrorMsg("Please enter a valid port number, only integers are accepted.");
      return false;
    } catch (SocketTimeoutException ste) {
      displayErrorMsg("Could not connect to the port or IP address in reasonable time.");
      return false;
    } catch (Exception e) {
      displayErrorMsg("Invalid IP address/port number, or the server is not running.");
      return false;
    }
    return true;
  }

  // helper method to display error msgs directly to client through popup box + on
  // client GUI output box
  public void displayErrorMsg(String msg) {
    clientOutputBox.append(" [CLIENT]: " + msg + "\r\n");
    JOptionPane.showMessageDialog(window, msg, "Error Alert", JOptionPane.WARNING_MESSAGE);
  }

  public void disconnect() {
    try {
      if (input != null && output != null) {
        input.close();
        output.close();
      }
      socket.close();
      System.exit(0);
    } catch (Exception e) {
      System.out.println("Socket already closed.");
      System.exit(0);
    }
  }

  /**
   * Parse the client's request data, and check if data is valid, if it is - make
   * the request to the server. Otherwise indicate the error to the client window,
   * and allow them to try sending a different request.
   * 
   * @param requestType - one of {POST, GET, PIN, UNPIN, CONNECT, DISCONNECT,
   *                    SHAKE, CLEAR}
   */
  public void sendRequest(String requestType) {
    try {
      // for writing request to server
      output = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException io) {
      System.out.println("Could not open socket stream.");
    }

    if (requestType.equals("POST")) {
      Boolean valid = true;
      // check for missing info from user
      if (xField.getText().equals("") || yField.getText().equals("") || widthField.getText().equals("")
          || heightField.getText().equals("") || msgField.getText().equals("")) {
        valid = false;
        displayErrorMsg("One or more fields do not have a specified value, or message is empty.");
      }
      // check for valid number values
      if (valid) {
        try {
          int x = Integer.parseInt(xField.getText());
          int y = Integer.parseInt(yField.getText());
          int width = Integer.parseInt(widthField.getText());
          int height = Integer.parseInt(heightField.getText());

          if (x < 0 || y < 0) {
            displayErrorMsg("Please enter a number greater than or equal to 0 for both x, and y.");
          } else {
            output.println(requestType + SP + x + SP + y + SP + width + SP + height + SP + colourCombo.getSelectedItem()
                + SP + msgField.getText());
          }

        } catch (NumberFormatException nfe) {
          displayErrorMsg("Please enter a valid number, only integers are accepted for: x, y, width, height.");
        }
      }

    } else if (requestType.equals("GET")) {
      // gets all pins coordinates on board
      String refers = refersField.getText();
      String contains = containsField.getText();
      String colour = colourField.getText();

      // empty value indicates all for that input
      if (refers.equals("")) {
        refers = "ALL";
      }
      if (contains.equals("")) {
        contains = "ALL";
      }
      if (colour.equals("")) {
        colour = "ALL";
      }

      if (allPinsCheck.isSelected()) {
        output.println(requestType + SP + "PINS"); // request type 1
      } else {
        output.println(requestType + SP + refers + SP + contains + SP + colour); // request type 2
      }
    } else if (requestType.equals("PIN") || requestType.equals("UNPIN")) {
      try {
        int x = Integer.parseInt(xGetField.getText());
        int y = Integer.parseInt(yGetField.getText());

        if (x < 0 || y < 0) {
          displayErrorMsg("Please enter a number greater than or equal to 0 for both x, and y.");
        } else {
          output.println(requestType + SP + x + SP + y);
        }
      } catch (NumberFormatException nfe) {
        displayErrorMsg("Please enter a valid integer number for both x, and y.");
      }
    } else if (requestType.equals("SHAKE")) {
      output.println(requestType);
    } else if (requestType.equals("CLEAR")) {
      output.println(requestType);
    }
    output.flush();

    // call on readServerResponse method here?
    // readServerResponse();
  }

  public void readServerResponse() {
    // read from server (input)
    // output response to GUI window
  }
}