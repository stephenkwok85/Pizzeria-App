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
    private static final double TAX_RATE = 0.06625;

    private EditText orderNumberInput;
    private ListView currentOrderList;
    private TextView subtotalInput, taxInput, orderTotalInput;
    private Button searchOrderButton, removePizzaButton, clearAllOrdersButton, completeOrderButton;

    private List<Pizza> currentOrderPizzas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int selectedPizzaIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        initializeViews();
        setupListView();
        setupListeners();
    }

    /**
     * Initializes all views for the activity.
     */
    private void initializeViews() {
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
    }

    /**
     * Sets up the ListView with single-choice mode and item selection listener.
     */
    private void setupListView() {
        currentOrderList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        currentOrderList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPizzaIndex = position;
            Toast.makeText(this, "Selected Pizza: " + (position + 1), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Sets up button listeners for various actions.
     */
    private void setupListeners() {
        searchOrderButton.setOnClickListener(v -> searchOrder());
        removePizzaButton.setOnClickListener(v -> removeSelectedPizza());
        clearAllOrdersButton.setOnClickListener(v -> clearAllOrders());
        completeOrderButton.setOnClickListener(v -> completeOrder());
    }

    /**
     * Searches for an order and updates the UI accordingly.
     */
    private void searchOrder() {
        String orderNumberText = orderNumberInput.getText().toString();
        if (orderNumberText.isEmpty()) {
            showToast("Please enter an order number.");
            return;
        }

        try {
            int orderNumber = Integer.parseInt(orderNumberText);
            List<Pizza> pizzasInOrder = OrderManager.getInstance().getOrder(orderNumber);

            if (pizzasInOrder != null) {
                currentOrderPizzas.clear();
                currentOrderPizzas.addAll(pizzasInOrder);
                updateListView();
                updateOrderSummary();
            } else {
                showToast("Order not found or already completed.");
            }
        } catch (NumberFormatException e) {
            showToast("Invalid order number!");
        }
    }

    /**
     * Removes the selected pizza from the current order.
     */
    private void removeSelectedPizza() {
        if (selectedPizzaIndex < 0 || selectedPizzaIndex >= currentOrderPizzas.size()) {
            showToast("Please select a pizza to remove.");
            return;
        }

        Pizza selectedPizza = currentOrderPizzas.get(selectedPizzaIndex);
        currentOrderPizzas.remove(selectedPizzaIndex);
        updateListView();
        updateOrderSummary();

        showToast("Selected pizza removed.");
    }

    /**
     * Clears all orders from the system.
     */
    private void clearAllOrders() {
        OrderManager.getInstance().clearAllOrders();
        currentOrderPizzas.clear();
        updateListView();
        updateOrderSummary();
        showToast("All orders have been cleared.");
    }

    /**
     * Completes the current order.
     */
    private void completeOrder() {
        try {
            int orderNumber = Integer.parseInt(orderNumberInput.getText().toString());
            if (currentOrderPizzas.isEmpty()) {
                showToast("Order #" + orderNumber + " has no pizzas. Cannot complete the order.");
                return;
            }

            OrderManager.getInstance().completeOrder(orderNumber);
            currentOrderPizzas.clear();
            updateListView();
            updateOrderSummary();
            orderNumberInput.setText("");
            showToast("Order #" + orderNumber + " has been completed!");
        } catch (NumberFormatException e) {
            showToast("Invalid order number!");
        }
    }

    /**
     * Updates the ListView with the current order details.
     */
    private void updateListView() {
        List<String> pizzaDescriptions = new ArrayList<>();
        for (Pizza pizza : currentOrderPizzas) {
            pizzaDescriptions.add(formatPizzaDescription(pizza));
        }
        adapter.clear();
        adapter.addAll(pizzaDescriptions);
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates the order summary (subtotal, tax, total).
     */
    private void updateOrderSummary() {
        double subtotal = currentOrderPizzas.stream().mapToDouble(Pizza::price).sum();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalInput.setText(String.format("%.2f", subtotal));
        taxInput.setText(String.format("%.2f", tax));
        orderTotalInput.setText(String.format("%.2f", total));
    }

    /**
     * Formats the description of a pizza for display in the ListView.
     */
    private String formatPizzaDescription(Pizza pizza) {
        StringBuilder description = new StringBuilder();
        description.append(pizza.getSize().toString()).append(" ");
        description.append(pizza.getStyle()).append(" ");
        description.append(pizza.getClass().getSimpleName()).append(" - ");
        description.append("$").append(String.format("%.2f", pizza.price()));

        if (!pizza.getToppings().isEmpty()) {
            description.append(" (Toppings: ");
            description.append(String.join(", ", pizza.getToppings().toString()));
            description.append(")");
        }
        return description.toString();
    }

    /**
     * Displays a toast message.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
