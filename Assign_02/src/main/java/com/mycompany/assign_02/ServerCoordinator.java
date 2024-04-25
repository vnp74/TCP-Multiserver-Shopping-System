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
public class ServerCoordinator {
    private ServerSocket serverSocket;

    public ServerCoordinator(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerCoordinator running on port " + port);
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

    public void start() {
        System.out.println("ServerCoordinator is waiting for the connection...");
    }
}
