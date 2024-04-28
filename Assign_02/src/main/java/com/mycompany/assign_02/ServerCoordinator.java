package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerCoordinator {
    private ServerSocket serverSocket;

    public ServerCoordinator(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerCoordinator running on port " + port);
    }

    public void start() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                System.out.println(
                        "Connection established with Client: " + clientSocket.getInetAddress().getHostAddress());

                Object object = in.readObject();
                if (object instanceof BookOrder) {
                    handleBookOrder((BookOrder) object, out);
                } else if (object instanceof MovieOrder) {
                    handleMovieOrder((MovieOrder) object, out);
                } else {
                    out.writeObject("Unknown order type received.");
                }
            } catch (SocketException e) {
                System.err.println("SocketException: Possible connection reset by client - " + e.getMessage());
            } catch (IOException e) {
                System.err.println("IOException: Error in input/output - " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException: Error reading object - " + e.getMessage());
            }
        }
    }

    private void handleBookOrder(BookOrder bookOrder, ObjectOutputStream clientOut) {
        try (Socket serverBookSocket = new Socket("localhost", 8001);
                ObjectOutputStream out = new ObjectOutputStream(serverBookSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(serverBookSocket.getInputStream())) {

            out.writeObject(bookOrder);
            out.flush();

            String result = (String) in.readObject();
            clientOut.writeObject(result);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing book order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMovieOrder(MovieOrder movieOrder, ObjectOutputStream clientOut) throws IOException {
        try (Socket serverMovieSocket = new Socket("localhost", 8002);
                ObjectOutputStream out = new ObjectOutputStream(serverMovieSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(serverMovieSocket.getInputStream())) {
            out.writeObject(movieOrder);
            out.flush();

            String result = (String) in.readObject();
            clientOut.writeObject(result);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to deserialize the response from ServerMovie: " + e.getMessage());
            clientOut.writeObject("Error processing movie order.");
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
