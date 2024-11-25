package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up window insets to ensure proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Chicago Pizza Button
        ImageButton chicagoButton = findViewById(R.id.chicagoButton);
        chicagoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChicagoPizzaActivity.class);
            startActivity(intent);
        });

        // NY Pizza Button
        ImageButton nyButton = findViewById(R.id.nyButton);
        nyButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NYPizzaActivity.class);
            startActivity(intent);
        });

        // Current Order Button
        ImageButton currentOrderButton = findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
            startActivity(intent);
        });

        // Orders Placed Button
        ImageButton ordersPlacedButton = findViewById(R.id.ordersPlacedButton);
        ordersPlacedButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrdersPlacedActivity.class);
            startActivity(intent);
        });
    }
}