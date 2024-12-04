package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pizzeria_package.*;

/**
 * Activity to manage placed orders. Allows users to view order details,
 * cancel orders, and export order information to a text file.
 */
public class OrdersPlacedActivity extends AppCompatActivity {
    private Spinner placedOrderNumberDropdown;
    private ListView placedOrderList;
    private EditText orderTotalField;
    private Button cancelOrderButton, exportOrderButton;

    private static final double TAX_RATE = 0.06625;

    private ArrayAdapter<Integer> spinnerAdapter;
    private ArrayAdapter<String> listAdapter;
    private List<String> pizzaDetailsList = new ArrayList<>();
    private List<Integer> placedOrderNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_placed);

        // Initialize views
        placedOrderNumberDropdown = findViewById(R.id.placedOrderNumberDropdown);
        placedOrderList = findViewById(R.id.placedOrderList);
        orderTotalField = findViewById(R.id.orderTotalField);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);
        exportOrderButton = findViewById(R.id.exportOrderButton);

        // Initialize adapters
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, placedOrderNumbers);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placedOrderNumberDropdown.setAdapter(spinnerAdapter);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pizzaDetailsList);
        placedOrderList.setAdapter(listAdapter);

        // Set item selected listener for the Spinner
        placedOrderNumberDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showOrderDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(OrdersPlacedActivity.this, "No order selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Set button listeners
        cancelOrderButton.setOnClickListener(v -> cancelOrder());
        exportOrderButton.setOnClickListener(v -> exportOrders());

        // Load placed orders into spinner
        refreshPlacedOrders();
    }




    private void refreshPlacedOrders() {
        placedOrderNumbers.clear();
        placedOrderNumbers.addAll(OrderManager.getInstance().getPlacedOrderNumbers());
        spinnerAdapter.notifyDataSetChanged();
    }

    private void showOrderDetails() {
        Integer orderNumber = (Integer) placedOrderNumberDropdown.getSelectedItem();
        if (orderNumber != null) {
            List<Pizza> pizzas = OrderManager.getInstance().getPlacedOrder(orderNumber);
            if (pizzas != null && !pizzas.isEmpty()) {
                displayPizzaDetails(pizzas);
                calculateAndDisplayTotal(pizzas);
            } else {
                Toast.makeText(this, "Order not found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayPizzaDetails(List<Pizza> pizzas) {
        pizzaDetailsList.clear();
        int pizzaNumber = 1;

        for (Pizza pizza : pizzas) {
            pizzaDetailsList.add("Pizza " + pizzaNumber++);
            pizzaDetailsList.add("Category: " + pizza.getClass().getSimpleName());
            pizzaDetailsList.add("Size: " + pizza.getSize());
            pizzaDetailsList.add("Crust: " + pizza.getCrust());
            pizzaDetailsList.add("Price: $" + String.format("%.2f", pizza.price()));
            pizzaDetailsList.add("Toppings: " + getToppingsString(pizza));
            pizzaDetailsList.add("");
        }

        listAdapter.notifyDataSetChanged();
    }


    private String getToppingsString(Pizza pizza) {
        StringBuilder toppings = new StringBuilder();
        for (Topping topping : pizza.getToppings()) {
            toppings.append(topping).append(", ");
        }
        if (toppings.length() > 0) {
            toppings.setLength(toppings.length() - 2);
        }
        return toppings.toString();
    }

    private void calculateAndDisplayTotal(List<Pizza> pizzas) {
        double subtotal = 0.0;
        for (Pizza pizza : pizzas) {
            subtotal += pizza.price();
        }
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        orderTotalField.setText(String.format("%.2f", total));
    }


    private void cancelOrder() {
        Integer orderNumber = (Integer) placedOrderNumberDropdown.getSelectedItem();
        if (orderNumber != null) {
            boolean isDeleted = OrderManager.getInstance().deletePlacedOrder(orderNumber);
            if (isDeleted) {
                Toast.makeText(this, "Order #" + orderNumber + " has been canceled.", Toast.LENGTH_SHORT).show();
                refreshPlacedOrders();
                pizzaDetailsList.clear();
                listAdapter.notifyDataSetChanged();
                orderTotalField.setText("");
            } else {
                Toast.makeText(this, "Failed to cancel order #" + orderNumber, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an order to cancel.", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportOrders() {
        // Export functionality (to be implemented later, similar to the initial plan)
        Toast.makeText(this, "Export functionality is not implemented yet.", Toast.LENGTH_SHORT).show();
    }
}
