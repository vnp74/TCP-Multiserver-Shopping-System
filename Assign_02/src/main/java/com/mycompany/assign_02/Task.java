/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.assign_02;

/**
 *
 * @author VRAJ
 */

/**
 * The Task interface defines a general structure for tasks that can be
 * executed,
 * specifically providing methods to perform a task and retrieve its result.
 * This is useful for defining operations that involve processing and outputting
 * a result.
 */
public interface Task {
    void executeTask(); // Executes the primary operation of the task.

    String getResult(); // Retrieves the result of the task after execution
}
