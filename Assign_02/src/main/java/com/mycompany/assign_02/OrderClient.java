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

    public OrderClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public static void main(String[] args) {
        try {
            OrderClient client = new OrderClient("localhost", 8000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
