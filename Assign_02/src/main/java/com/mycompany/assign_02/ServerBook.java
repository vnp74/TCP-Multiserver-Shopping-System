/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assign_02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author VRAJ
 */
public class ServerBook {
    private ServerSocket serverSocket;

    public ServerBook(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerBook running on port" + port);
    }

    public static void main(String[] args) {
        try {
            ServerBook serverBook = new ServerBook(8001);
            serverBook.start();
        } catch (IOException e) {
            System.err.println("ServerBook failed to Start: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try (Socket clienSocket = serverSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(clienSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clienSocket.getOutputStream())) {

                System.out.println(
                        "Connection established with client: " + clienSocket.getInetAddress().getHostAddress());

                Object order = in.readObject();
                if (order instanceof BookOrder) {
                    processOrder((BookOrder) order, out);
                } else {
                    out.writeObject("Invalid order type received.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void processOrder(BookOrder bookOrder, ObjectOutputStream out) throws IOException {
        try {
            bookOrder.executeTask();
            out.writeObject(bookOrder.getResult());
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
            out.writeObject("Error processing the book order.");
        }
    }
}
