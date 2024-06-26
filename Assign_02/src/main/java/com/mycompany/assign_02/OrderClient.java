package com.mycompany.assign_02;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class OrderClient {
    private Socket socket; // Client socket for connection
    private ObjectOutputStream out; // Stream to send objects to the server
    private ObjectInputStream in; // Stream to receive objects from the server
    private Scanner scanner; // Scanner to read user input from console

    public OrderClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        scanner = new Scanner(System.in);
    }

    // Starts the client interaction loop allowing users to place orders

    public void start() {
        try {
            int option;
            do {
                System.out.println("\nPLEASE PLACE YOUR ORDER BY SELECTING A NUMBER");
                System.out.println("********************************");
                System.out.println("1.Purchase Book");
                System.out.println("2.Purchase Movie");
                System.out.println("3.Exit");
                System.out.print("Enter your option: ");

                option = scanner.nextInt();
                scanner.nextLine(); // consume the rest of the line

                switch (option) {
                    case 1:
                        handleBookOrder();
                        break;

                    case 2:
                        handleMovieOrder();
                        break;

                    case 3:
                        System.out.println("Exiting client...");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } while (option != 3);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    // Handles book order by asking the user for quantity and price, and sending the
    // order to the server.
    private void handleBookOrder() throws IOException {
        System.out.print("Enter the number of books: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter the book price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume the rest of the line
        BookOrder bookOrder = new BookOrder(quantity, price);

        sendOrder(bookOrder);
    }

    // Handles movie order by asking the user for quantity and price, and sending
    // the order to the server.
    private void handleMovieOrder() throws IOException {
        System.out.print("Enter the number of movies: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter the movie price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume the rest of the line
        MovieOrder movieOrder = new MovieOrder(quantity, price);

        sendOrder(movieOrder);
    }

    // Sends an order object to the server and handles the server's response.
    private void sendOrder(Object order) throws IOException {
        System.out.println("SENDING OBJECT TO SERVER........");
        out.writeObject(order);
        out.flush();

        try {
            Object response = in.readObject();
            System.out.println("RECEIVING COMPUTED OBJECT FROM SERVER.......");
            if (response instanceof String) {
                System.out.println(response);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Closes all resources including streams and socket.
    private void cleanUp() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
            if (scanner != null)
                scanner.close();
            System.out.println("Client connection closed.");
        } catch (IOException e) {
            System.err.println("An error occurred while closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            OrderClient client = new OrderClient("localhost", 2501);
            client.start();
        } catch (IOException e) {
            System.err.println("Failed to start OrderClient: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
