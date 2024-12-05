package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Main activity of the app. 
 * Provides navigation to different sections of the app like Chicago Pizza, NY Pizza, Current Order, and Orders Placed.
 * 
 * @author Stephen Kwok and Jeongtae Kim
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created. Sets up the layout and initializes button listeners.
     *
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton chicagoButton = findViewById(R.id.chicagoButton);
        chicagoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChicagoPizzaActivity.class);
            startActivity(intent);
        });

        ImageButton nyButton = findViewById(R.id.nyButton);
        nyButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NYPizzaActivity.class);
            startActivity(intent);
        });

        ImageButton currentOrderButton = findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
            startActivity(intent);
        });

        ImageButton ordersPlacedButton = findViewById(R.id.ordersPlacedButton);
        ordersPlacedButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrdersPlacedActivity.class);
            startActivity(intent);
        });
    }
}
