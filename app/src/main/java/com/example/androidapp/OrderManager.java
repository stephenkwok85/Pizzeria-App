package com.example.androidapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pizzeria_package.Order;
import pizzeria_package.Pizza;

/**
 * Singleton class to manage orders in the application.
 * Handles adding pizzas to orders, retrieving orders,
 * completing orders, and managing order numbers.
 */
public class OrderManager {
    private static OrderManager instance;
    private final Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderNumber = 1;
    private int currentOrderNumber = 0;
    private int lastCreatedOrderNumber = 0;

    private OrderManager() {}

    /**
     * Retrieves the singleton instance of the OrderManager.
     *
     * @return The OrderManager instance.
     */
    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void completeOrder(int orderNumber) {
        Order order = orders.get(orderNumber);
        if (order != null && !order.isPlaced()) {
            order.placeOrder();
        } else {
            throw new IllegalArgumentException("Order not found or already completed.");
        }

        if (orderNumber == currentOrderNumber) {
            currentOrderNumber = 0;
        }
    }

    /**
     * Adds a pizza to the current order. Creates a new order if no current order exists.
     *
     * @param pizza The pizza to be added to the current order.
     */
    public void addOrderToCurrentOrder(Pizza pizza) {
        if (currentOrderNumber == 0) {
            currentOrderNumber = nextOrderNumber++;
            lastCreatedOrderNumber = currentOrderNumber; // Store the original order number
            orders.put(currentOrderNumber, new Order());
        }
        orders.get(currentOrderNumber).addPizza(pizza);
    }

    /**
     * Retrieves the current active order number.
     *
     * @return The current order number.
     */
    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    /**
     * Retrieves the pizzas in a specific order by order number.
     *
     * @param orderNumber The order number to retrieve.
     * @return A list of pizzas in the order, or null if the order doesn't exist or is completed.
     */
    public List<Pizza> getOrder(int orderNumber) {
        Order order = orders.get(orderNumber);
        return (order != null && !order.isPlaced()) ? order.getPizzas() : null;
    }

    /**
     * Deletes a placed order by its order number.
     *
     * @param orderNumber The order number to delete.
     * @return True if the order was successfully deleted, false otherwise.
     */
    public boolean deletePlacedOrder(int orderNumber) {
        if (orders.containsKey(orderNumber) && orders.get(orderNumber).isPlaced()) {
            orders.remove(orderNumber);
            reuseCanceledOrderNumber(orderNumber);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of order numbers for all placed orders.
     *
     * @return A list of placed order numbers.
     */
    public List<Integer> getPlacedOrderNumbers() {
        List<Integer> placedOrderNumbers = new ArrayList<>();
        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
            if (entry.getValue().isPlaced()) {
                placedOrderNumbers.add(entry.getKey());
            }
        }
        return placedOrderNumbers;
    }

    public List<Pizza> getPlacedOrder(int orderNumber) {
        Order order = orders.get(orderNumber);
        if (order != null && order.isPlaced()) {
            return order.getPizzas();
        }
        return null;
    }

    /**
     * Removes a pizza from a specific order by order number.
     *
     * @param orderNumber The order number from which the pizza will be removed.
     * @param pizza The pizza to be removed from the order.
     */
    public void removePizzaFromOrder(int orderNumber, Pizza pizza) {
        Order order = orders.get(orderNumber);
        if (order != null && !order.isPlaced()) {
            order.removePizza(pizza);
        } else {
            throw new IllegalArgumentException("Order not found or already completed.");
        }
    }

    /**
     * Clears all orders from the system, but ensures the order number goes back to the original number
     * if the current order wasn't completed.
     */
    public void clearAllOrders() {
        orders.clear();
        currentOrderNumber = 0;

        if (lastCreatedOrderNumber != 0 && currentOrderNumber == 0) {
            nextOrderNumber = lastCreatedOrderNumber;
        } else {
            nextOrderNumber = 1; 
        }
    }

    /**
     * Sets the next order number to a specific value (used when canceling an order).
     */
    public void reuseCanceledOrderNumber(int canceledOrderNumber) {
        this.nextOrderNumber = canceledOrderNumber;
    }
    
}
