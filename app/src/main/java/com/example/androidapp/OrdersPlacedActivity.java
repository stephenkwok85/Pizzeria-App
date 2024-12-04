package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pizzeria_package.Order;
import pizzeria_package.Pizza;

public class OrdersPlacedActivity extends AppCompatActivity {

    private Spinner placedOrderNumberDropdown;
    private ListView placedOrderList;
    private EditText orderTotalField;
    private Button exportOrderButton;
    private Button cancelOrderButton;

    private OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_placed);

        placedOrderNumberDropdown = findViewById(R.id.placedOrderNumberDropdown);
        placedOrderList = findViewById(R.id.placedOrderList);
        orderTotalField = findViewById(R.id.orderTotalField);
        exportOrderButton = findViewById(R.id.exportOrderButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);

        orderManager = OrderManager.getInstance();

        populatePlacedOrderDropdown();

        placedOrderNumberDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                if (!selectedItem.trim().isEmpty()) {
                    int selectedOrderNumber = Integer.parseInt(selectedItem);
                    displayOrderDetails(selectedOrderNumber);
                } else {
                    placedOrderList.setAdapter(null);
                    orderTotalField.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                placedOrderList.setAdapter(null);
                orderTotalField.setText("");
            }
        });

        exportOrderButton.setOnClickListener(view -> {
            int selectedOrderNumber = (int) placedOrderNumberDropdown.getSelectedItem();
            exportOrderToFile(selectedOrderNumber);
        });

        cancelOrderButton.setOnClickListener(view -> {
            int selectedOrderNumber = (int) placedOrderNumberDropdown.getSelectedItem();
            cancelOrder(selectedOrderNumber);
        });
    }

    private void populatePlacedOrderDropdown() {
        List<Integer> placedOrderNumbers = orderManager.getPlacedOrderNumbers();
        List<String> displayItems = new ArrayList<>();
        displayItems.add(" ");
        for (int orderNumber : placedOrderNumbers) {
            displayItems.add(String.valueOf(orderNumber));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, displayItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placedOrderNumberDropdown.setAdapter(adapter);
        placedOrderNumberDropdown.setSelection(0);
    }

    private void displayOrderDetails(int orderNumber) {
        List<Pizza> pizzas = orderManager.getOrder(orderNumber);
        if (pizzas != null && !pizzas.isEmpty()) {
            ArrayAdapter<Pizza> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, pizzas);
            placedOrderList.setAdapter(adapter);

            double totalCost = 0;
            for (Pizza pizza : pizzas) {
                totalCost += pizza.price();
            }
            double tax = totalCost * 0.07;
            totalCost += tax;

            orderTotalField.setText(String.format("$%.2f", totalCost));
        } else {
            Toast.makeText(this, "Order not found or invalid.", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportOrderToFile(int orderNumber) {
        List<Pizza> pizzas = orderManager.getOrder(orderNumber);
        if (pizzas == null) {
            Toast.makeText(this, "Order not found or invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        File directory = new File(getFilesDir(), "ExportedOrders");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory, "Order_" + orderNumber + ".txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Order Number: " + orderNumber + "\n");
            writer.write("Pizzas:\n");
            for (Pizza pizza : pizzas) {
                writer.write(pizza.toString() + "\n");
            }
            writer.write("Total (including tax): " + orderTotalField.getText().toString() + "\n");
            Toast.makeText(this, "Order exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to export order.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelOrder(int orderNumber) {
        boolean success = orderManager.deletePlacedOrder(orderNumber);
        if (success) {
            Toast.makeText(this, "Order cancelled successfully.", Toast.LENGTH_SHORT).show();
            populatePlacedOrderDropdown();
        } else {
            Toast.makeText(this, "Failed to cancel order. Ensure the order exists and is valid.", Toast.LENGTH_SHORT).show();
        }
    }
}
