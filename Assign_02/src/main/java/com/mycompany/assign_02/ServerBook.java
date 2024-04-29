package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBook {
    private ServerSocket serverSocket; // Server socket to accept connections
    private static int orderCount = 0; // Static counter to keep track of book orders

    // Constructs a server that listens on the specified port.
    public ServerBook(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerBook running on port " + port);
    }

    // Starts the server to accept and process client orders continuously.
    public void start() {
        System.out.println("ServerBook is ready and waiting for client orders...");
        while (true) {
            try (Socket clientSocket = serverSocket.accept(); // Accept a client connection
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                System.out.println(
                        "Connection established with client: " + clientSocket.getInetAddress().getHostAddress());

                Object order = in.readObject();
                if (order instanceof BookOrder) {
                    synchronized (this) {
                        orderCount++; // Increment the order count
                        System.out.println("ServerBook Received Book Object Number: " + orderCount);
                    }
                    processOrder((BookOrder) order, out);
                } else {
                    out.writeObject("Invalid order type received.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error processing order: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void processOrder(BookOrder bookOrder, ObjectOutputStream out) {
        try {
            bookOrder.executeTask(); // Assume executeTask performs the necessary operations on the order
            String result = bookOrder.getResult(); // Assume getResult returns a String with order details
            System.out.println("Computed Total Bill for Book Order. Sending back to client....");
            out.writeObject(result);
        } catch (Exception e) {
            System.err.println("Error processing book order: " + e.getMessage());
            try {
                out.writeObject("Error processing the book order.");
            } catch (IOException ex) {
                System.err.println("Error sending error message to client: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerBook serverBook = new ServerBook(2502); // Ensure this port is intended for ServerBook
            serverBook.start();
        } catch (IOException e) {
            System.err.println("ServerBook failed to Start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
