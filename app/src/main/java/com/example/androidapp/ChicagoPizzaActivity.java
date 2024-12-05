package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pizzeria_package.ChicagoPizza;
import pizzeria_package.PizzaFactory;
import pizzeria_package.Size;
import pizzeria_package.Topping;
import pizzeria_package.Pizza;

/**
 * ChicagoPizzaActivity handles the user interface and logic for creating and customizing a Chicago-style pizza.
 * @author Stephen Kwok and Jeongtae Kim
 */
public class ChicagoPizzaActivity extends AppCompatActivity {
    // Constants for pricing
    private static final double TOPPING_PRICE = 1.69;
    private static final int MAX_TOPPINGS = 7;
    private static final double BUILD_YOUR_OWN_SMALL_PRICE = 8.99;
    private static final double BUILD_YOUR_OWN_MEDIUM_PRICE = 10.99;
    private static final double BUILD_YOUR_OWN_LARGE_PRICE = 12.99;
    private static final double DELUXE_SMALL_PRICE = 16.99;
    private static final double DELUXE_MEDIUM_PRICE = 18.99;
    private static final double DELUXE_LARGE_PRICE = 20.99;
    private static final double BBQ_SMALL_PRICE = 14.99;
    private static final double BBQ_MEDIUM_PRICE = 16.99;
    private static final double BBQ_LARGE_PRICE = 19.99;
    private static final double MEATZZA_SMALL_PRICE = 17.99;
    private static final double MEATZZA_MEDIUM_PRICE = 19.99;
    private static final double MEATZZA_LARGE_PRICE = 21.99;

    // UI components
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

        initializeViews();
        setupRecyclerView();
        setupSpinner();

        chooseType.setSelection(3);
        setPizzaOptions("Build Your Own");
        sSize.setChecked(true);

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
        toppingsRecyclerView = findViewById(R.id.toppingsRecyclerView);
        addToOrderButton = findViewById(R.id.addToOrderButton);

        pizzaFactory = new ChicagoPizza();
    }

    private void setupRecyclerView() {
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Topping> toppingsList = Arrays.asList(Topping.values());
        toppingAdapter = new ToppingAdapter(this, toppingsList, MAX_TOPPINGS, this::onToppingSelected);
        toppingsRecyclerView.setAdapter(toppingAdapter);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Deluxe", "BBQ Chicken", "Meatzza", "Build Your Own"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseType.setAdapter(adapter);

        chooseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                setPizzaOptions(selectedType);
                updatePizzaPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupListeners() {
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> updatePizzaPrice());
        addToOrderButton.setOnClickListener(view -> addOrder());
    }

    private void setPizzaOptions(String pizzaType) {
        selectedToppingsCount = 0;
        toppingAdapter.resetSelection();

        String crustType;
        int imageResource;

        switch (pizzaType) {
            case "Deluxe":
                crustType = "Deep Dish";
                imageResource = R.drawable.ch_deluxe;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM
                ), false);
                isCustomizable = false;
                break;
            case "BBQ Chicken":
                crustType = "Pan";
                imageResource = R.drawable.ch_bbq;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR
                ), false);
                isCustomizable = false;
                break;
            case "Meatzza":
                crustType = "Stuffed";
                imageResource = R.drawable.ch_meat;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM
                ), false);
                isCustomizable = false;
                break;
            case "Build Your Own":
            default:
                crustType = "Pan";
                imageResource = R.drawable.ch_build;
                toppingAdapter.resetSelection();
                toppingAdapter.setToppings(Arrays.asList(Topping.values()), true);

                isCustomizable = true;
                break;
        }

        crustField.setText(crustType);
        pizzaImage.setImageResource(imageResource);
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
        String selectedType = chooseType.getSelectedItem().toString();
        double basePrice = 0.0;

        switch (selectedType) {
            case "Deluxe":
                if (sSize.isChecked()) basePrice = DELUXE_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = DELUXE_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = DELUXE_LARGE_PRICE;
                break;
            case "BBQ Chicken":
                if (sSize.isChecked()) basePrice = BBQ_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = BBQ_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = BBQ_LARGE_PRICE;
                break;
            case "Meatzza":
                if (sSize.isChecked()) basePrice = MEATZZA_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = MEATZZA_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = MEATZZA_LARGE_PRICE;
                break;
            case "Build Your Own":
                if (sSize.isChecked()) basePrice = BUILD_YOUR_OWN_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = BUILD_YOUR_OWN_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = BUILD_YOUR_OWN_LARGE_PRICE;
                break;
        }

        return basePrice;
    }

    private void onToppingSelected(int count) {
        selectedToppingsCount = count;
        updatePizzaPrice();
    }

    private void addOrder() {
        String selectedType = (String) chooseType.getSelectedItem();
        Pizza pizza;
        Size pizzaSize = getPizzaSize();

        switch (selectedType) {
            case "Deluxe":
                pizza = pizzaFactory.createDeluxe();
                break;
            case "Meatzza":
                pizza = pizzaFactory.createMeatzza();
                break;
            case "BBQ Chicken":
                pizza = pizzaFactory.createBBQChicken();
                break;
            case "Build Your Own":
            default:
                pizza = pizzaFactory.createBuildYourOwn();
                List<Topping> selectedToppings = new ArrayList<>(toppingAdapter.getSelectedToppings());
                pizza.setToppings(selectedToppings);
                break;
        }

        if (pizza != null) {
            pizza.setSize(pizzaSize);
        }

        OrderManager.getInstance().addOrderToCurrentOrder(pizza);

        int orderNumber = OrderManager.getInstance().getCurrentOrderNumber();

        Toast.makeText(this, "Pizza added to order number: " + orderNumber, Toast.LENGTH_SHORT).show();
    }

    private Size getPizzaSize() {
        if (sSize.isChecked()) {
            return Size.SMALL;
        } else if (mSize.isChecked()) {
            return Size.MEDIUM;
        } else if (lSize.isChecked()) {
            return Size.LARGE;
        } else {
            return Size.SMALL;
        }
    }
}
