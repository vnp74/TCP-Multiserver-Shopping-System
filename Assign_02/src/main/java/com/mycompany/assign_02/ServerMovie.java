package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMovie {
    private ServerSocket serverSocket; // Server socket to accept connections
    private static int orderCount = 0; // Static counter to keep track of orders

    // Sets up a server on the specified port to handle movie orders.
    public ServerMovie(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerMovie running on port " + port);
    }

    // Accepts client connections continuously and processes incoming movie orders.
    public void start() {
        System.out.println("ServerMovie is ready and waiting for client orders...");
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                System.out.println(
                        "Connection established with client: " + clientSocket.getInetAddress().getHostAddress());

                Object order = in.readObject();
                if (order instanceof MovieOrder) {
                    synchronized (this) {
                        orderCount++; // Increment order count
                        System.out.println("ServerMovie Received Movie Object Number: " + orderCount);
                    }
                    processOrder((MovieOrder) order, out);
                } else {
                    out.writeObject("Invalid order type received.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error processing order: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Processes a movie order by calculating the total bill and sending the result
    // back to the client.
    private void processOrder(MovieOrder movieOrder, ObjectOutputStream out) throws IOException {
        try {
            movieOrder.executeTask(); // Calculate total bill based on order details
            String result = movieOrder.getResult(); // Retrieve the computed result
            System.out.println("Computed Total Bill for Movie Order. Sending back to client....");
            out.writeObject(result);
        } catch (IOException e) {
            System.err.println("Error processing movie order: " + e.getMessage());
            out.writeObject("Error processing the movie order.");
        }
    }

    public static void main(String[] args) {
        try {
            ServerMovie serverMovie = new ServerMovie(2503);
            serverMovie.start();
        } catch (IOException e) {
            System.err.println("ServerMovie failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
