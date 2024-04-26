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

    }
}
