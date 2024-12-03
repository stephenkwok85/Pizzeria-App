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

import pizzeria_package.Order;
import pizzeria_package.Pizza;
import pizzeria_package.Topping;

/**
 * Activity to manage the current order view.
 * Provides functionality to search, complete, and clear orders.
 */
public class CurrentOrderActivity extends AppCompatActivity {
    private EditText orderNumberInput;
    private ListView currentOrderList;
    private TextView subtotalInput, taxInput, orderTotalInput;
    private Button searchOrderButton, removePizzaButton, clearAllOrdersButton, completeOrderButton;

    private List<Pizza> currentOrderPizzas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final double TAX_RATE = 0.06625;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        // Initialize views
        orderNumberInput = findViewById(R.id.orderNumberInput);
        currentOrderList = findViewById(R.id.currentOrderList);
        subtotalInput = findViewById(R.id.subtotalInput);
        taxInput = findViewById(R.id.taxInput);
        orderTotalInput = findViewById(R.id.orderTotalInput);
        searchOrderButton = findViewById(R.id.searchOrderButton);
        removePizzaButton = findViewById(R.id.removePizzaButton);
        clearAllOrdersButton = findViewById(R.id.clearAllOrdersButton);
        completeOrderButton = findViewById(R.id.completeOrderButton);

        // Set up ListView adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        currentOrderList.setAdapter(adapter);

        // Set button listeners
        searchOrderButton.setOnClickListener(v -> searchOrder());
        removePizzaButton.setOnClickListener(v -> removeSelectedPizza());
        clearAllOrdersButton.setOnClickListener(v -> clearAllOrders());
        completeOrderButton.setOnClickListener(v -> completeOrder());
    }

    private void searchOrder() {
        try {
            int orderNumber = Integer.parseInt(orderNumberInput.getText().toString());
            List<Pizza> pizzas = OrderManager.getInstance().getOrder(orderNumber);

            if (pizzas != null && !pizzas.isEmpty()) {
                currentOrderPizzas.clear();
                currentOrderPizzas.addAll(pizzas);
                updateListView();
                updateOrderSummary();
            } else {
                Toast.makeText(this, "Order not found or already completed!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid order number!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeSelectedPizza() {
        int position = currentOrderList.getCheckedItemPosition();
        if (position >= 0 && position < currentOrderPizzas.size()) {
            currentOrderPizzas.remove(position);
            updateListView();
            updateOrderSummary();
        } else {
            Toast.makeText(this, "Please select a pizza to remove!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAllOrders() {
        currentOrderPizzas.clear();
        updateListView();
        updateOrderSummary();
    }

    private void completeOrder() {
        try {
            int orderNumber = Integer.parseInt(orderNumberInput.getText().toString());
            OrderManager.getInstance().completeOrder(orderNumber);

            // Clear current order list and reset UI
            currentOrderPizzas.clear();
            updateListView();
            updateOrderSummary();
            orderNumberInput.setText("");

            // Show confirmation message
            Toast.makeText(this, "Order #" + orderNumber + " has been completed!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid order number! Please enter a valid number.", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


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
