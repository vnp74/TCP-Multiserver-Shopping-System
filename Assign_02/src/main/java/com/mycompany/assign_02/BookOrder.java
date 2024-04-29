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
public class BookOrder implements Task, Serializable {
    private int quantity;
    private double unitPrice;
    private double tax;
    private double totalBill;
    private static final double TAX_RATE = 0.10; // 10% tax rate for books

    public BookOrder(int quantity, double unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.tax = 0;
        this.totalBill = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalBill() {
        return totalBill;
    }

    /*
     * Executes the task of calculating the total bill for the book order.
     * This includes computing the tax based on the tax rate.
     */
    @Override
    public void executeTask() {
        double subtotal = quantity * unitPrice;
        tax = subtotal * TAX_RATE;
        totalBill = subtotal + tax;
    }

    // returns result string
    @Override
    public String getResult() {
        return "Number of Books: " + quantity + "Price: " + unitPrice + "Tax: " + tax + "Total Bill: " + totalBill;

    }

}
