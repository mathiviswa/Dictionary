package com.wuhan.dict.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

// Class responsible for handling client requests in the dictionary server
public class MyDicClientHandler implements Runnable {
    private DicService service; // Reference to the DicService for performing dictionary operations
    private Socket socket; // Socket for communicating with the client

    // Constructor
    public MyDicClientHandler(DicService service, Socket socket) {
        this.service = service;
        this.socket = socket;
    }

    // Method to handle client requests
    @Override
    public void run() {
        try {
            // Create input and output streams for communication with the client
            InputStreamReader inReader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inReader);

            OutputStreamWriter outWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(outWriter);

            String data = null;
            // Continuously read input from the client
            while ((data = reader.readLine()) != null) {
                System.out.println("client request:" + data); // Print the received request from the client
                String response = service.getActionResult(data); // Process the request using the DicService
                System.out.println("server response to client:" + response); // Print the response to be sent to the client
                writer.write(response + "\n"); // Send the response to the client
                writer.flush(); // Flush the writer to ensure the response is sent immediately
                ServerMainSystem.getInstance().initDatas(); // Update server data after each request
            }
        } catch (SocketException ex) {
            System.out.println("No client socket connected"); // Handle case when client disconnects abruptly
        } catch (IOException ex) {
            System.out.println("No input from the client."); // Handle case when there's no input from the client
        } finally {
            // Close the socket when done
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Internet error."); // Handle any errors during socket closure
                }
            }
        }
    }
}
