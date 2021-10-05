import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ClientGUI {
  private Socket socket;
  JTextArea clientOutputBox = new JTextArea(12, 1);
  JComboBox<String> colourCombo = new JComboBox<>(); // holds avail. note colours
  JFrame window;
  PrintWriter output;
  BufferedReader input;

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
    JTextField xField = new JTextField();
    JTextField yField = new JTextField();
    JTextField widthField = new JTextField();
    JTextField heightField = new JTextField();
    JLabel msgLabel = new JLabel("Message");
    JTextField msgField = new JTextField();
    JLabel colourLabel = new JLabel("Colour");
    JComboBox<String> statusCombo = new JComboBox<>();
    JLabel refersLabel = new JLabel("RefersTo=");
    JLabel containsLabel = new JLabel("Contains=");
    JLabel colourLabel2 = new JLabel("Colour=");
    JTextField refersField = new JTextField();
    JTextField containsField = new JTextField();
    JTextField colourField = new JTextField();
    JButton getBtn = new JButton("GET");
    JCheckBox allPinsCheck = new JCheckBox("ALL PINS");
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
    JTextField xGetField = new JTextField();
    JTextField yGetField = new JTextField();
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
        connect(IPAddrField.getText(), Integer.parseInt(portNumField.getText())); // add error checking to this later
        connectBtn.setEnabled(false);
        disconnectBtn.setEnabled(true);
        connectBtn.setText("Connected");
      }
    });

    disconnectBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // sendRequest("DISCONNECT");
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

    window.pack();
  }

  /**
   * Attempt to connect to the server that is listening for
   * connections @portNumber and @IPAddress
   * 
   * @param IPAddress  - ip address to connect to server
   * @param portNumber - port number to connect to server
   */
  public void connect(String IPAddress, int portNumber) {
    try {
      // connect to server
      socket = new Socket(IPAddress, portNumber); // ip-addr, port-num for now
      clientOutputBox.append(" [CLIENT]: Successfully connected to server at port: " + portNumber + ".\n");

      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      clientOutputBox.append(" [SERVER]: The available note colours for use are: ");
      // read initial server response and display to client window
      String response;
      while (input.ready() && (response = input.readLine()) != null) {
        clientOutputBox.append(response + " ");
        colourCombo.addItem(response);
      }
      clientOutputBox.append("\n [SERVER]: IMPORTANT - Note colours are case sensitive.");

    } catch (Exception e) {
      System.out.println("Problem connecting to server.\n");
      System.exit(1);
    }
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
      output = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException io) {

    }
    // NOTE: for each request button, call this method within their actionListener
    if (requestType.equals("POST")) {
      output.println("POST");
    } else if (requestType.equals("GET")) {
      output.println("GET");
    } else if (requestType.equals("PIN")) {
      output.println("PIN");
    } else if (requestType.equals("UNPIN")) {
      output.println("UNPIN");
    } else if (requestType.equals("SHAKE")) {
      output.println("SHAKE");
    } else if (requestType.equals("CLEAR")) {
      output.println("CLEAR");
    }
    output.flush();

    // check what request type is first, see if it matches one of the above ^

    // if it matches one of the above request types, check for valid data

    // if data is valid for the requested type, send request to server over socket

    // else if data is invalid, send error msg to client indicating problem
  }

}