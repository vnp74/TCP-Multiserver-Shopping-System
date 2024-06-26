package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCoordinator {
    private ServerSocket serverSocket; // Listen for client connections
    private static int clientNumber = 0; // Static variable to keep track of client numbers

    // Sets up a server on the specified port.
    public ServerCoordinator(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerCoordinator running on port " + port);
    }

    // Accepts client connections continuously and handles each in a new thread.
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                synchronized (ServerCoordinator.class) {
                    clientNumber++; // Increment client number safely within synchronized block
                }
                int currentClientNumber = clientNumber; // Capture the current client number for use in the thread
                new Thread(() -> handleClient(clientSocket, currentClientNumber)).start();
            } catch (IOException e) {
                System.err.println("IOException: Error accepting client connection - " + e.getMessage());
            }
        }
    }

    // Manages client requests and delegates processing based on order type.
    private void handleClient(Socket clientSocket, int clientNum) {
        try (clientSocket;
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            System.out.println("Connection established with Client #" + clientNum);
            Object object = in.readObject();

            if (object instanceof BookOrder) {
                handleBookOrder((BookOrder) object, out);
            } else if (object instanceof MovieOrder) {
                handleMovieOrder((MovieOrder) object, out);
            } else {
                out.writeObject("Unknown order type received.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing client request: " + e.getMessage());
        }
    }

    // Forwards book orders to a ServerBook and returns results to client.
    private void handleBookOrder(BookOrder bookOrder, ObjectOutputStream clientOut) {
        try (Socket serverBookSocket = new Socket("localhost", 2502);
                ObjectOutputStream out = new ObjectOutputStream(serverBookSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(serverBookSocket.getInputStream())) {

            System.out.println("ServerCoordinator Received Client Object: Sending to Server for Book ....");
            out.writeObject(bookOrder);
            out.flush();

            String result = (String) in.readObject();
            clientOut.writeObject(result);
            System.out.println("Return Order Back to Original Client ....");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing book order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Forwards movie orders to a ServerMovie and returns results to client.
    private void handleMovieOrder(MovieOrder movieOrder, ObjectOutputStream clientOut) {
        try (Socket serverMovieSocket = new Socket("localhost", 2503);
                ObjectOutputStream out = new ObjectOutputStream(serverMovieSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(serverMovieSocket.getInputStream())) {

            System.out.println("ServerCoordinator Received Client Object: Sending to Server for Movie ....");
            out.writeObject(movieOrder);
            out.flush();

            String result = (String) in.readObject();
            clientOut.writeObject(result);
            System.out.println("Return Order Back to Original Client ....");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing movie order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerCoordinator coordinator = new ServerCoordinator(2501);
            coordinator.start();
        } catch (IOException e) {
            System.err.println("ServerCoordinator failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
