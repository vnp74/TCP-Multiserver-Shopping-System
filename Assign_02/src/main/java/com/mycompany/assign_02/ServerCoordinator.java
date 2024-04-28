package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCoordinator {
    private ServerSocket serverSocket;

    public ServerCoordinator(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerCoordinator running on port " + port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException e) {
                System.err.println("IOException: Error accepting client connection - " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket;
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            System.out.println("Connection established with Client: " + clientSocket.getInetAddress().getHostAddress());
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

    private void handleBookOrder(BookOrder bookOrder, ObjectOutputStream clientOut) {
        try (Socket serverBookSocket = new Socket("localhost", 8001);
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

    private void handleMovieOrder(MovieOrder movieOrder, ObjectOutputStream clientOut) {
        try (Socket serverMovieSocket = new Socket("localhost", 8002);
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
            ServerCoordinator coordinator = new ServerCoordinator(8000);
            coordinator.start();
        } catch (IOException e) {
            System.err.println("ServerCoordinator failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
