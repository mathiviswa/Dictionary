package com.wuhan.dict.server;

import com.wuhan.dict.common.constant.Constant;
import com.wuhan.dict.common.ui.DicScrollPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

// Main class for the dictionary server application
public class ServerMainSystem extends JFrame {
    private static ServerMainSystem instance;
    private static DicService service = new DicService(); // Service for handling dictionary operations
    protected DicScrollPanel scrollPanel = null; // Panel for displaying dictionary data
    protected JLabel lbMsg; // Label for displaying messages

    // Constructor
    public ServerMainSystem() {
        initUI(); // Initialize the user interface
        initDatas(); // Initialize server data
    }

    // Method to get the singleton instance of ServerMainSystem
    public static synchronized ServerMainSystem getInstance() {
        if (instance == null) {
            instance = new ServerMainSystem();
        }
        return instance;
    }

    // Method to initialize server data
    public void initDatas() {
        showDictList(service.getAllData()); // Display dictionary data on startup
    }

    // Method to initialize the user interface
    private void initUI() {
        setTitle("Dictionary - Server"); // Set the title of the window
        setSize(800, 400); // Set the size of the window
        JPanel panelResult = new JPanel(new BorderLayout()); // Panel for displaying dictionary data
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);

        scrollPanel = new DicScrollPanel(); // Create the scroll panel for dictionary data
        panelResult.add(scrollPanel, BorderLayout.CENTER);
        panelResult.setBorder(BorderFactory.createTitledBorder(lineBorder, "Dictionary Data", TitledBorder.LEFT, TitledBorder.CENTER));

        JPanel panelStatus = new JPanel(new BorderLayout()); // Panel for displaying messages
        lbMsg = new JLabel();
        lbMsg.setPreferredSize(new Dimension(600, 30));
        panelStatus.add(lbMsg, BorderLayout.CENTER);
        panelStatus.setBorder(BorderFactory.createTitledBorder(lineBorder, "Message", TitledBorder.LEFT, TitledBorder.CENTER));

        // Add panels to the content pane
        this.getContentPane().add(panelResult, BorderLayout.CENTER);
        this.getContentPane().add(panelStatus, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        setLocationRelativeTo(null); // Center the window on the screen
        this.setVisible(true); // Make the window visible
    }

    // Method to display dictionary data
    public void showDictList(String resultStr) {
        lbMsg.setText(""); // Clear the message label
        if (resultStr == null || resultStr.trim().length() <= 0) {
            return;
        }
        JSONObject obj = new JSONObject(resultStr);
        if (obj.has(Constant.RESULT_CODE) && obj.getInt(Constant.RESULT_CODE) == 200 && obj.get(Constant.RESULT_DATA) != null) {
            JSONArray array = obj.getJSONArray(Constant.RESULT_DATA);
            Vector<Vector<Object>> tableData = new Vector<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                Vector<Object> eleVector = new Vector<>();
                eleVector.add((i + 1));
                eleVector.add(item.getString(Constant.FIELD_KEY));
                eleVector.add(item.getString(Constant.FIELD_VALUE));
                tableData.addElement(eleVector);
            }
            scrollPanel.setTableData(tableData); // Set table data in the scroll panel
        }
        lbMsg.setText(obj.getString(Constant.RESULT_MSG)); // Set message text
    }

    // Method to start the server
    public static void start(int port) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port); // Create a server socket
            System.out.println("Server established! port:" + port);
            while (true) {
                System.out.println("Waiting for client to connect...");
                Socket socket = serverSocket.accept(); // Accept client connection
                System.out.println("A client has initiated connection！");
                MyDicClientHandler clientHandler = new MyDicClientHandler(service, socket); // Create a client handler
                Thread t = new Thread(clientHandler); // Create a thread for the client handler
                t.start(); // Start the thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please input correct command: ");
            return;
        }
        String portStr = args[0];
        if (!portStr.matches("^(\\d{1,4})$")) {
            System.out.println("Use correct 4 number port");
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerMainSystem.getInstance(); // Create an instance of ServerMainSystem
            }
        });
        start(Integer.valueOf(portStr)); // Start the server with the specified port
    }
}
