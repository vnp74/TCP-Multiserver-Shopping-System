/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assign_02;

import java.io.Serializable;

/**
 *
 * @author VRAJ
 */
public class MovieOrder implements Task, Serializable {
    private int quantity;
    private double unitPrice;
    private double tax;
    private double totalBill;
    private static final double TAX_RATE = 0.30; // tax rate 30%

    public MovieOrder(int quantity, double unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.tax = 0;
        this.totalBill = 0;
    }

    @Override
    public void executeTask() {
        double subtotal = quantity * unitPrice;
        tax = subtotal * TAX_RATE;
        totalBill = subtotal + tax;
    }

    // returns the result string
    @Override
    public String getResult() {
        return "Number of Movies: " + quantity + " Price: " + unitPrice + " Tax: " + tax + " Total Bill: " + totalBill;
    }
}
