package pizzeria_package;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order consisting of multiple pizzas. Each order has a unique order number,
 * can be marked as placed, and provides methods for adding and removing pizzas, calculating
 * totals, and displaying order details.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class Order {
    private static int orderCounter = 1;
    private int number;
    private List<Pizza> pizzas;
    private boolean isPlaced;

    /**
     * Constructs a new order with a unique order number. Initializes an empty list of pizzas
     * and sets the order status to "not placed."
     */
    public Order() {
        this.number = orderCounter++;
        this.pizzas = new ArrayList<>();
        this.isPlaced = false;
    }

    /**
     * Returns the unique order number.
     *
     * @return The order number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Adds a pizza to the order.
     *
     * @param pizza The pizza to add to the order.
     */
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    /**
     * Removes a specified pizza from the order.
     *
     * @param pizza The pizza to remove from the order.
     */
    public void removePizza(Pizza pizza) {
        pizzas.remove(pizza);
    }

    /**
     * Returns a copy of the list of pizzas in the order.
     *
     * @return A list of pizzas in the order.
     */
    public List<Pizza> getPizzas() {
        return new ArrayList<>(pizzas);
    }

    /**
     * Clears all pizzas from the order.
     */
    public void clearOrder() {
        pizzas.clear();
    }

    /**
     * Calculates the total cost of the order, including tax.
     *
     * @param taxRate The tax rate to apply to the order subtotal.
     * @return The total cost of the order, including tax.
     */
    public double calculateTotal(double taxRate) {
        double subtotal = pizzas.stream().mapToDouble(Pizza::price).sum();
        return subtotal + (subtotal * taxRate);
    }

    /**
     * Calculates the subtotal of the order, excluding tax.
     *
     * @return The subtotal of the order.
     */
    public double calculateSubtotal() {
        return pizzas.stream().mapToDouble(Pizza::price).sum();
    }

    /**
     * Returns the placed status of the order.
     *
     * @return True if the order has been placed, otherwise false.
     */
    public boolean isPlaced() {
        return isPlaced;
    }

    /**
     * Marks the order as placed, indicating that it is complete and cannot be modified.
     */
    public void placeOrder() {
        this.isPlaced = true;
    }

    /**
     * Returns a string representation of the order, including the order number,
     * list of pizzas, subtotal, and order status.
     *
     * @return A string summary of the order.
     */
    @Override
    public String toString() {
        return "Order Number: " + number + "\n" +
                "Pizzas: " + pizzas + "\n" +
                "Subtotal: $" + String.format("%.2f", calculateSubtotal()) + "\n" +
                "Status: " + (isPlaced ? "Placed" : "In Progress") + "\n";
    }
}