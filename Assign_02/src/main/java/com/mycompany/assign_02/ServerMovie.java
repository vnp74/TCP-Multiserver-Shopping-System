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
public class ServerMovie {
    private ServerSocket serverSocket;

    public ServerMovie(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("ServerMovie running on port " + port);
    }

    public static void main(String[] args) {
        try {
            ServerMovie serverMovie = new ServerMovie(8003);
            serverMovie.start();
        } catch (Exception e) {
            System.err.println("ServerMovie failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {

    }
}
