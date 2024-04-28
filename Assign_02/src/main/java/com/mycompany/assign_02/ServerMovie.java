package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMovie {
    private ServerSocket serverSocket;
    private static int orderCount = 0; // Static counter to keep track of orders

    public ServerMovie(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerMovie running on port " + port);
    }

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

    private void processOrder(MovieOrder movieOrder, ObjectOutputStream out) throws IOException {
        try {
            movieOrder.executeTask();
            String result = movieOrder.getResult();
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
