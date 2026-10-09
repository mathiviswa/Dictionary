package com.wuhan.dict.client;

import com.wuhan.dict.common.bean.ActionBean;
import com.wuhan.dict.common.bean.OpLogBean;
import com.wuhan.dict.common.constant.Constant;
import com.wuhan.dict.common.ui.LogScrollPanel;
import com.wuhan.dict.common.utils.MyDialog;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;

// Main client class for a dictionary application using Swing for GUI
public class ClientMainSystem extends JFrame {
    // Static instance of this frame for global access
    public static ClientMainSystem frame;
    // UI components and client state fields
    private       LogScrollPanel   scrollPanel;
    private       String           host;
    private       int              port;
    private       JButton          btnSearch, btnAdd, btnUpdate, btnDelete;
    private       JTextField       txtFieldInput;
    private       JTextArea        txtAreaDesc;
    private       JLabel           labelValStatus;
    // Network components for communicating with the server
    private       Socket           socket;
    private       BufferedReader   reader;
    private       BufferedWriter   writer;

    // Regex pattern to validate that the input is letters only
    private static Pattern LETTER_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    // List to hold operation logs
    private java.util.List<OpLogBean> logList = new ArrayList<>();

    // Constructor that initializes the client system with host and port
    public ClientMainSystem(String host, int port) {
        System.out.println("init client system address:" + host + ",port:" + port);
        frame = this;
        this.host = host;
        this.port = port;
        init();     // Initialize UI components
        createSocket();     // Establish connection with server
    }

    // Initializes the UI components and event listeners
    private void init() {
        initPanel();    // Setup the main panel and its components
        // Adding action listeners to buttons for executing dictionary operations
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExecute(new ActionBean(Constant.ACTION_QUERY, txtFieldInput.getText(), txtAreaDesc.getText()));
                //clearStatusMsg();
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExecute(new ActionBean(Constant.ACTION_ADD, txtFieldInput.getText(), txtAreaDesc.getText()));
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExecute(new ActionBean(Constant.ACTION_UPDATE, txtFieldInput.getText(), txtAreaDesc.getText()));
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExecute(new ActionBean(Constant.ACTION_DELETE, txtFieldInput.getText(), txtAreaDesc.getText()));
            }
        });
    }

    // Creates the socket and establishes connection with the server
    private void createSocket() {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            //new Thread(this).start();
        } catch (UnknownHostException e) {
            System.out.println("closed...");
            frame.setStatusMsg("The server is not connected because of Unknown Host...");
        } catch (ConnectException ce) {
            MyDialog.warning(frame, "The server is not connected...\nPlease restart Server.");
        } catch (IOException e) {
            MyDialog.warning(frame, "The data cannot reached...\nPlease restart Server.");
        } catch (Exception e) {
            System.out.println("Error reading dic file");
        }
    }

    // Appends operation log and updates the UI accordingly
    private void appendOpLog(String opType, String result) {
        logList.add(new OpLogBean(opType, result));
        showOpLogData(logList);
    }

    // Executes an action based on the type specified in actionBean (e.g., Query, Add, Update, Delete)
    private void actionExecute(ActionBean actionBean) {
        // Check if the action is QUERY, UPDATE, or DELETE
        if (actionBean.getAction().equals(Constant.ACTION_QUERY)
                || actionBean.getAction().equals(Constant.ACTION_UPDATE)
                || actionBean.getAction().equals(Constant.ACTION_DELETE)) {
            // Validate the input word: it must not be null, empty, and must be a valid word (letters only)
            if (actionBean.getKey() == null || actionBean.getKey().trim().isEmpty() || !isValidWord(actionBean.getKey())) {
                // Log the error and show a warning dialog if the word is not valid
                appendOpLog(actionBean.getAction(), "Please input a valid word.");
                MyDialog.warning(frame, "Please input a valid word.");
                return;
            }
        }
        // Check if the action is ADD
        if (actionBean.getAction().equals(Constant.ACTION_ADD)) {
            // Validate the input word for ADD action
            if (actionBean.getKey() == null || actionBean.getKey().trim().isEmpty() || !isValidWord(actionBean.getKey())) {
                // Log and alert if the word is invalid
                appendOpLog(actionBean.getAction(), "Please input a valid word.");
                MyDialog.warning(frame, "Please input a valid word.");
                txtAreaDesc.setText("");    // Clear the description field
                return;
            }
            // Additional validation for the description field in ADD action
            if (actionBean.getValue() == null || actionBean.getValue().trim().isEmpty()) {
                appendOpLog(actionBean.getAction(), "Please input a valid description.");
                MyDialog.warning(frame, "Please input a valid description.");
                return;
            }
        }
        new Thread(new ReceiveMessages(actionBean)).start();

    }

    // Updates the UI with the result data or an error message
    private void showResultData(JSONObject data, String msg) {
        // Check if the data object is null which indicates an issue with processing the request
        if (data == null) {
            // Set an error message prompting a server restart
            labelValStatus.setText("Date cannot proceed, try to restart the server...");
            return;
        }
        // Update the description text area with the value from the result data
        txtAreaDesc.setText(data.getString(Constant.FIELD_VALUE));
        // Update the status label with the provided message and append a newline for readability
        labelValStatus.setText(msg + "\n");
    }

    // Checks if the given string (word) matches the predefined pattern (only letters)
    private static boolean isValidWord(String word) {
        return LETTER_PATTERN.matcher(word).matches();
    }

    // Constructs the search panel with an input field and a search button
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));   // Sets a raised bevel border for the panel
        JPanel panelInput = new JPanel(new BorderLayout());
        JLabel labelInput = new JLabel("Input:", JLabel.CENTER);    // Label for the input field
        labelInput.setPreferredSize(new Dimension(80, 30));     // Sets the preferred size of the label
        txtFieldInput = new JTextField();   // Initializes the text field for input

        btnSearch = new JButton("Search");      // Initializes the search button
        // Adds the text field and button to the input panel
        panelInput.add(txtFieldInput, BorderLayout.CENTER);
        panelInput.add(btnSearch, BorderLayout.EAST);

        // Adds the input label and input panel to the main search panel
        panel.add(labelInput, BorderLayout.WEST);
        panel.add(panelInput, BorderLayout.CENTER);
        return panel;       // Returns the constructed panel
    }

    // Creates a control panel containing Add, Update, and Delete buttons
    private JPanel createCtrlPanel() {
        JPanel panelButton = new JPanel();      // Panel to hold the buttons
        // Initialize buttons for Add, Update, and Delete actions
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        // Add the buttons to the panel
        panelButton.add(btnAdd);
        panelButton.add(btnUpdate);
        panelButton.add(btnDelete);
        return panelButton;     // Returns the panel with buttons
    }

    // Constructs the center panel containing the description text area and control buttons
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel descPanel = new JPanel(new BorderLayout());

        JLabel labelDesc = new JLabel("Description:", JLabel.CENTER);       // Label for the description area
        labelDesc.setPreferredSize(new Dimension(80, 20));
        txtAreaDesc = new JTextArea();      // Text area for entering descriptions
        txtAreaDesc.setLineWrap(true);
        txtAreaDesc.setWrapStyleWord(true);
        JScrollPane JScrollPaneDescrip = new JScrollPane(txtAreaDesc);      // Adds a scroll pane to the text area
        // Adds the description label and scroll pane to the description panel
        descPanel.add(labelDesc, BorderLayout.WEST);
        descPanel.add(JScrollPaneDescrip, BorderLayout.CENTER);
        // Adds the control panel and description panel to the center panel
        panel.add(createCtrlPanel(), BorderLayout.NORTH);
        panel.add(descPanel, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(10, 0, 10, 10));     // Sets padding around the center panel

        return panel;
    }

    // Creates a panel to display response messages
    private JPanel createResponsePanel() {
        JPanel responsePanel = new JPanel(new BorderLayout());
        JLabel labelStatus = new JLabel("Response:", JLabel.CENTER);    // Label for the response area
        labelStatus.setPreferredSize(new Dimension(80, 30));
        responsePanel.add(labelStatus, BorderLayout.WEST);

        labelValStatus = new JLabel();  //Label to show response messages
        labelValStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK));      // Sets a border around the label
        labelValStatus.setPreferredSize(new Dimension(400,30));
        // Adds the status label to the response panel
        responsePanel.add(labelValStatus, BorderLayout.CENTER);
        responsePanel.setBorder(new EmptyBorder(0, 0, 1, 10));  // Adds padding around the panel
        return responsePanel;
    }

    // Constructs a panel to display operation records/logs
    private JPanel createAllDatasPanel() {
        JPanel all = new JPanel();
        all.setLayout(new BorderLayout());
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);    // Sets a border for the panel
        scrollPanel = new LogScrollPanel();     // Initializes the scroll panel to display logs
        all.add(scrollPanel, BorderLayout.CENTER);      // Adds the scroll panel to the main panel
        // Sets a titled border with "Operation records"
        all.setBorder(BorderFactory.createTitledBorder(lineBorder, "Operation records", TitledBorder.LEFT, TitledBorder.CENTER));
        return all;     // Returns the panel for operation records
    }

    private void initPanel() {
        // Set size and title of the frame
        setSize(860, 400);
        setTitle("Dictionary-Client");

        // Create a JPanel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        // Add components to the panel
        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(createCenterPanel(), BorderLayout.CENTER);
        panel.add(createResponsePanel(), BorderLayout.SOUTH);
        // Set default close operation and location of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Add panel to the frame's content pane
        this.getContentPane().add(panel, BorderLayout.CENTER);
        // Add additional panel to the east of the frame
        this.getContentPane().add(createAllDatasPanel(), BorderLayout.EAST);
        // Make the frame visible
        this.setVisible(true);
    }

    // Set status message
    public void setStatusMsg(String msg) {
        labelValStatus.setText(msg);
    }

    // Runnable class to handle receiving messages from server
    private class ReceiveMessages implements Runnable {
        private ActionBean actionBean;

        public ReceiveMessages(ActionBean actionBean) {
            this.actionBean = actionBean;
        }

        @Override
        public void run() {
            try {
                // Construct JSON object with action details
                JSONObject params = new JSONObject();
                String key = actionBean.getKey();
                String value = actionBean.getValue();
                String action = actionBean.getAction();

                params.put(Constant.FIELD_KEY, key);
                params.put(Constant.FIELD_VALUE, value);
                params.put(Constant.FIELD_ACTION, action);

                // Write JSON object to server
                writer.write(params.toString() + "\n");
                writer.flush();
                // Read server response
                try {
                    String resultStr = reader.readLine();
                    System.out.println("client get result,opType:" + action + ",response:" + resultStr);
                    JSONObject obj = new JSONObject(resultStr);
                    // Handle server response
                    if (obj.has(Constant.RESULT_CODE) && obj.getInt(Constant.RESULT_CODE) == 200 && obj.get(Constant.RESULT_DATA) != null) {
                        showResultData(obj.getJSONObject(Constant.RESULT_DATA), obj.getString(Constant.RESULT_MSG));
                    } else {
                        labelValStatus.setText(obj.getString(Constant.RESULT_MSG));
                    }
                    appendOpLog(action, (String) obj.get(Constant.RESULT_MSG));
                } catch (SocketTimeoutException e) {
                    setStatusMsg("Timeout while waiting for server response.");
                }
            } catch (SocketException ex){
                MyDialog.warning(frame, "The server is not connected...\nPlease restart Server.");
            }catch (Exception ex) {
                MyDialog.warning(frame, "The server is not connected...\nPlease restart Server.");
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        }
    }

    // Method to display operation log data in a table
    private void showOpLogData(java.util.List<OpLogBean> logList) {
        Vector<Vector<Object>> tableData = new Vector<>();
        for (int i = 0; i < logList.size(); i++) {
            OpLogBean item = logList.get(i);
            Vector<Object> eleVector = new Vector<>();
            eleVector.add((i + 1));
            eleVector.add(item.getOpType());
            eleVector.add(item.getOpResult());
            tableData.addElement(eleVector);
        }
        scrollPanel.setTableData(tableData);
    }

    // Main method
    public static void main(String[] args) {
        // User can input in command
        // java –jar dictionary-client-1.0-jar-with-dependencies.jar <port> or
        // java –jar dictionary-client-1.0-jar-with-dependencies.jar localhost <port>
        // Parse command line arguments
        String address = "localhost";
        int port;
        if (args.length == 2) {
            address = args[0];
            String portStr = args[1];
            if (!address.matches("^(localhost)$")) {
                System.out.println("Use localhost as server address");
                return;
            } else if (!portStr.matches("^(\\d{1,4})$")) {
                System.out.println("Use correct 4 number port");
                return;
            }
            port = Integer.valueOf(portStr);
        } else if (args.length == 1) {
            String portStr = args[0];
            if (!portStr.matches("^(\\d{1,4})$")) {
                System.out.println("Use correct 4 number port");
                return;
            }
            port = Integer.valueOf(portStr);
        } else {
            System.out.println("Please input correct command: ");
            System.out.println("> dictionary-client-1.0-jar-with-dependencies.jar <server-address> <server-port>");
            return;
        }

        // Start the client
        String finalAddress = address;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ClientMainSystem(finalAddress, port);
                } catch (Exception e) {
                    System.out.println("Client closed...");
                }
            }
        });
    }

}
