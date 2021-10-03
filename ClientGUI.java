import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ClientGUI {
  private Socket socket;
  private JFrame window;
  JTextArea clientOutputBox = new JTextArea(6, 1); // 6 rows, 1 column to display text

  public ClientGUI() {
    this.window = new JFrame();
  }

  public void startGUI() {
    window.setSize(900, 600);
    window.setTitle("Bulletin Board Application");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setupGUI();
    window.setVisible(true);
  }

  // adding all the components to GUI, setup controller logic
  public void setupGUI() {
    // note: this isn't how the GUI is actually going to look, this is just to test
    // client-server connection with the added GUI component
    JPanel connectPanel = new JPanel();
    JLabel ipLabel = new JLabel("Enter IP address:", JLabel.CENTER);
    JLabel portLabel = new JLabel("Enter port number:", JLabel.CENTER);
    JTextField ipField = new JTextField();
    JTextField portField = new JTextField();
    JButton connect = new JButton("Connect");
    connect.setFont(new Font("Serif", Font.BOLD, 50));
    connect.setFocusPainted(false);

    // # rows, # cols, hgap, vgap
    GridLayout layout = new GridLayout(3, 2, 5, 2);
    connectPanel.setLayout(layout);

    connectPanel.add(ipLabel);
    connectPanel.add(ipField);
    connectPanel.add(portLabel);
    connectPanel.add(portField);
    connectPanel.add(connect);
    connectPanel.add(clientOutputBox);
    window.add(connectPanel);

    // button event handlers
    connect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        connect(ipField.getText(), Integer.parseInt(portField.getText())); // add error checking to this later
        connect.setEnabled(false);
        connect.setText("Connected");
      }
    });

  }

  public void connect(String IPAddress, int portNumber) {

    try {
      // connect to server
      socket = new Socket(IPAddress, portNumber); // ip-addr, port-num for now
      System.out.println("[CLIENT]: Successfully connected to server at port: " + portNumber + ".\n"); // remove later
      clientOutputBox.append("[CLIENT]: Successfully connected to server at port: " + portNumber + ".\n");

      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // read initial server response and display to client window
      String response;
      while ((response = input.readLine()) != null) {
        System.out.println("[SERVER]: " + response); // remove later
        clientOutputBox.append("[SERVER]: " + response + "\n");
      }

    } catch (Exception e) {
      System.out.println("Problem connecting to server.\n");
      System.exit(1);
    }
  }
}
