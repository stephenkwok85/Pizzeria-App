package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import pizzeria_package.ChicagoPizza;
import pizzeria_package.PizzaFactory;
import pizzeria_package.Topping;

public class ChicagoPizzaActivity extends AppCompatActivity {
    private static final double TOPPING_PRICE = 1.69;
    private static final int MAX_TOPPINGS = 7;

    private Spinner chooseType;
    private TextView crustField;
    private RadioButton sSize, mSize, lSize;
    private RadioGroup sizeGroup;
    private EditText pizzaPrice;
    private ImageView pizzaImage;
    private Button addToOrderButton;
    private RecyclerView toppingsRecyclerView;

    private boolean isCustomizable = false;
    private int selectedToppingsCount = 0;

    private PizzaFactory pizzaFactory;
    private ToppingAdapter toppingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize views
        initializeViews();

        // Set up Spinner
        setupSpinner();

        // Initialize RecyclerView for toppings
        setupRecyclerView();

        // Set default pizza type and size
        chooseType.setSelection(3); // Default to "Build Your Own"
        setPizzaOptions("Build Your Own");
        sSize.setChecked(true);

        // Set up listeners
        setupListeners();
    }

    private void initializeViews() {
        chooseType = findViewById(R.id.chooseTypeSpinner);
        crustField = findViewById(R.id.crustTypeView);
        sSize = findViewById(R.id.sizeSmall);
        mSize = findViewById(R.id.sizeMedium);
        lSize = findViewById(R.id.sizeLarge);
        sizeGroup = findViewById(R.id.sizeRadioGroup);
        pizzaPrice = findViewById(R.id.pizzaPriceField);
        pizzaImage = findViewById(R.id.pizzaImage);
        addToOrderButton = findViewById(R.id.addToOrderButton);
        toppingsRecyclerView = findViewById(R.id.toppingsRecyclerView);

        pizzaFactory = new ChicagoPizza();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Deluxe", "BBQ Chicken", "Meatzza", "Build Your Own"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseType.setAdapter(adapter);

        chooseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Object selectedItem = parent.getItemAtPosition(position);
                if (selectedItem instanceof String) {
                    String selectedType = (String) selectedItem;
                    setPizzaOptions(selectedType);
                    updatePizzaPrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void setupRecyclerView() {
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Topping> toppingsList = Arrays.asList(Topping.values());
        toppingAdapter = new ToppingAdapter(this, toppingsList, MAX_TOPPINGS, this::onToppingSelected);
        toppingsRecyclerView.setAdapter(toppingAdapter);
    }

    private void setupListeners() {
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> updatePizzaPrice());
        addToOrderButton.setOnClickListener(view -> addOrder());
    }

    private void setPizzaOptions(String pizzaType) {
        selectedToppingsCount = 0;
        toppingAdapter.resetSelection();

        String crustType;
        switch (pizzaType) {
            case "Deluxe":
                crustType = "Deep Dish";
                toppingAdapter.preselectToppings(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM);
                isCustomizable = false;
                break;
            case "BBQ Chicken":
                crustType = "Pan";
                toppingAdapter.preselectToppings(Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR);
                isCustomizable = false;
                break;
            case "Meatzza":
                crustType = "Stuffed";
                toppingAdapter.preselectToppings(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM);
                isCustomizable = false;
                break;
            case "Build Your Own":
                crustType = "Pan";
                isCustomizable = true;
                break;
            default:
                crustType = "";
        }

        crustField.setText(crustType);
        toppingAdapter.setCustomizable(isCustomizable);
        updatePizzaPrice();
    }

    private void onToppingSelected(int count) {
        selectedToppingsCount = count;
        updatePizzaPrice();
    }

    private void updatePizzaPrice() {
        double price = calculateBasePrice();
        if (isCustomizable) {
            price += selectedToppingsCount * TOPPING_PRICE;
        }
        pizzaPrice.setText(String.format("Price: $%.2f", price));
    }

    private double calculateBasePrice() {
        if (sSize.isChecked()) return 16.99; // Example price
        if (mSize.isChecked()) return 18.99; // Example price
        if (lSize.isChecked()) return 20.99; // Example price
        return 0.0;
    }

    private void addOrder() {
        Toast.makeText(this, "Order added!", Toast.LENGTH_SHORT).show();
    }
}
