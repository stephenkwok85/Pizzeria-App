package com.example.androidapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pizzeria_package.Pizza;
import pizzeria_package.Topping;

/**
 * Activity to manage the current order view.
 * Provides functionality to search, complete, and clear orders.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class CurrentOrderActivity extends AppCompatActivity {
    private EditText orderNumberInput;
    private ListView currentOrderList;
    private TextView subtotalInput, taxInput, orderTotalInput;
    private Button searchOrderButton, removePizzaButton, clearAllOrdersButton, completeOrderButton;

    private List<Pizza> currentOrderPizzas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final double TAX_RATE = 0.06625;

    private int selectedPizzaIndex = -1;

    /**
     * Initializes the activity and its components.
     * Sets up the user interface, event listeners, and the adapter for displaying orders.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        orderNumberInput = findViewById(R.id.orderNumberInput);
        currentOrderList = findViewById(R.id.currentOrderList);
        subtotalInput = findViewById(R.id.subtotalInput);
        taxInput = findViewById(R.id.taxInput);
        orderTotalInput = findViewById(R.id.orderTotalInput);
        searchOrderButton = findViewById(R.id.searchOrderButton);
        removePizzaButton = findViewById(R.id.removePizzaButton);
        clearAllOrdersButton = findViewById(R.id.clearAllOrdersButton);
        completeOrderButton = findViewById(R.id.completeOrderButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        currentOrderList.setAdapter(adapter);

        currentOrderList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        currentOrderList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPizzaIndex = position;
            Toast.makeText(this, "Selected Pizza: " + (position + 1), Toast.LENGTH_SHORT).show();
        });

        searchOrderButton.setOnClickListener(v -> searchOrder());
        removePizzaButton.setOnClickListener(v -> removeSelectedPizza());
        clearAllOrdersButton.setOnClickListener(v -> clearAllOrders());
        completeOrderButton.setOnClickListener(v -> completeOrder());
    }

    /**
     * Searches for an order by its number, loads the pizzas in that order,
     * and updates the list view with the current order.
     */
    private void searchOrder() {
        String orderNumberText = orderNumberInput.getText().toString();
        if (!orderNumberText.isEmpty()) {
            try {
                int orderNumber = Integer.parseInt(orderNumberText);
                List<Pizza> pizzasInOrder = OrderManager.getInstance().getOrder(orderNumber);

                if (pizzasInOrder != null) {
                    currentOrderPizzas.clear();
                    currentOrderPizzas.addAll(pizzasInOrder);
                    updateListView();

                    updateOrderSummary();
                } else {
                    Toast.makeText(this, "Order not found or already completed.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid order number!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter an order number.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes the selected pizza from the current order, confirming the action through a dialog.
     */
    private void removeSelectedPizza() {
        if (selectedPizzaIndex >= 0 && selectedPizzaIndex < currentOrderPizzas.size()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Remove Pizza")
                    .setMessage("Are you sure you want to remove this pizza?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Pizza selectedPizza = currentOrderPizzas.get(selectedPizzaIndex);

                        try {
                            int orderNumber = Integer.parseInt(orderNumberInput.getText().toString());
                            OrderManager.getInstance().removePizzaFromOrder(orderNumber, selectedPizza);

                            currentOrderPizzas.remove(selectedPizzaIndex);

                            updateListView();
                            updateOrderSummary();

                            selectedPizzaIndex = -1;

                            Toast.makeText(this, "Selected pizza removed.", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Invalid order number!", Toast.LENGTH_SHORT).show();
                        } catch (IllegalArgumentException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        } else {
            Toast.makeText(this, "Please select a pizza to remove.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears all orders from the system after confirming the action through a dialog.
     */
    private void clearAllOrders() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Clear All")
                .setMessage("Are you sure you want to clear all orders?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    OrderManager.getInstance().clearAllOrders();
                    currentOrderPizzas.clear();
                    updateListView();
                    updateOrderSummary();

                    Toast.makeText(this, "All orders have been cleared.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    /**
     * Completes the order, confirming the action through a dialog.
     */
    private void completeOrder() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Order Completion")
                .setMessage("Are you sure you want to complete this order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        int orderNumber = Integer.parseInt(orderNumberInput.getText().toString());

                        if (currentOrderPizzas.isEmpty()) {
                            Toast.makeText(this, "Order #" + orderNumber + " has no pizzas. Cannot complete the order.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        OrderManager.getInstance().completeOrder(orderNumber);

                        currentOrderPizzas.clear();
                        updateListView();
                        updateOrderSummary();
                        orderNumberInput.setText("");

                        Toast.makeText(this, "Order #" + orderNumber + " has been completed!", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid order number!", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    /**
     * Updates the list view with the current pizzas in the order, displaying their details.
     */
    private void updateListView() {
        List<String> pizzaDescriptions = new ArrayList<>();
        for (Pizza pizza : currentOrderPizzas) {
            StringBuilder description = new StringBuilder();
            description.append(pizza.getSize().toString()).append(" ");
            description.append(pizza.getStyle()).append(" ");
            description.append(pizza.getClass().getSimpleName()).append(" - ");
            description.append("$").append(String.format("%.2f", pizza.price()));

            if (!pizza.getToppings().isEmpty()) {
                description.append(" (Toppings: ");
                for (Topping topping : pizza.getToppings()) {
                    description.append(topping.toString()).append(", ");
                }
                description.delete(description.length() - 2, description.length());
                description.append(")");
            }
            pizzaDescriptions.add(description.toString());
        }
        adapter.clear();
        adapter.addAll(pizzaDescriptions);
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates the order summary, including subtotal, tax, and total price.
     */
    private void updateOrderSummary() {
        double subtotal = 0.0;
        for (Pizza pizza : currentOrderPizzas) {
            subtotal += pizza.price();
        }
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalInput.setText(String.format("%.2f", subtotal));
        taxInput.setText(String.format("%.2f", tax));
        orderTotalInput.setText(String.format("%.2f", total));
    }
}
