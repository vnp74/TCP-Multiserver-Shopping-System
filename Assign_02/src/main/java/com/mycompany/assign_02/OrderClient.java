/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assign_02;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author VRAJ
 */
public class OrderClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Scanner scanner;

    public OrderClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            int option;

            do {
                System.out.println("Please Place your order by selecting a number:");
                System.out.println("1. Purchase Book");
                System.out.println("2. Purchase Movie");
                System.out.println("3. Exit");
                System.out.println("Enter your option: ");

                option = scanner.nextInt();
                scanner.nextLine();

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
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    private void handleBookOrder() throws IOException {
        System.out.println("Enter the number of books: ");
        int quantity = scanner.nextInt();
        System.out.println("Enter the book price: ");
        double price = scanner.nextDouble();
        BookOrder bookOrder = new BookOrder(quantity, price);

        sendOrder(bookOrder);
    }

    private void handleMovieOrder() throws IOException {
        System.out.println("Enter the number of movies: ");
        int quantity = scanner.nextInt();
        System.out.println("Enter the movie price: ");
        double price = scanner.nextDouble();
        MovieOrder movieOrder = new MovieOrder(quantity, price);

        sendOrder(movieOrder);
    }

    private void sendOrder(Object order) throws IOException {
        out.writeObject(order);
        out.flush();

        try {
            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println((String) response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cleanUp() {
        try {
            in.close();
            out.close();
            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            OrderClient client = new OrderClient("localhost", 8000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
