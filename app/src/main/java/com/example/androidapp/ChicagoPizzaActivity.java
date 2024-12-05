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
 * It allows users to select the pizza type (e.g., Deluxe, BBQ Chicken, Meatzza, Build Your Own), choose toppings,
 * and set the size of the pizza. The activity calculates the price based on the selected options and allows the
 * user to add the pizza to their current order.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
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

    /**
     * Initializes the activity and sets up views, listeners, and initial pizza options.
     */
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

    /**
     * Initializes all views for the activity.
     */
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

    /**
     * Sets up the RecyclerView for displaying the toppings.
     */
    private void setupRecyclerView() {
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Topping> toppingsList = Arrays.asList(Topping.values());
        toppingAdapter = new ToppingAdapter(this, toppingsList, MAX_TOPPINGS, this::onToppingSelected);
        toppingsRecyclerView.setAdapter(toppingAdapter);
    }

    /**
     * Sets up the Spinner for selecting pizza types.
     */
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

    /**
     * Sets up listeners for the RadioGroup and the Add to Order button.
     */
    private void setupListeners() {
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> updatePizzaPrice());
        addToOrderButton.setOnClickListener(view -> addOrder());
    }

    /**
     * Sets the pizza options (crust type, toppings, customization options) based on the selected pizza type.
     *
     * @param pizzaType The type of pizza selected (e.g., "Deluxe", "BBQ Chicken").
     */
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

    /**
     * Updates the pizza price based on the selected size and toppings.
     */
    private void updatePizzaPrice() {
        double price = calculateBasePrice();
        if (isCustomizable) {
            price += selectedToppingsCount * TOPPING_PRICE;
        }
        pizzaPrice.setText(String.format("Price: $%.2f", price));
    }

    /**
     * Calculates the base price of the pizza based on the selected size.
     *
     * @return The base price of the pizza.
     */
    private double calculateBasePrice() {
        String selectedType = chooseType.getSelectedItem().toString();
        double basePrice = 0.0;

        switch (selectedType) {
            case "Deluxe":
                if (sSize.isChecked()) basePrice = 16.99;
                else if (mSize.isChecked()) basePrice = 18.99;
                else if (lSize.isChecked()) basePrice = 20.99;
                break;
            case "BBQ Chicken":
                if (sSize.isChecked()) basePrice = 14.99;
                else if (mSize.isChecked()) basePrice = 16.99;
                else if (lSize.isChecked()) basePrice = 19.99;
                break;
            case "Meatzza":
                if (sSize.isChecked()) basePrice = 17.99;
                else if (mSize.isChecked()) basePrice = 19.99;
                else if (lSize.isChecked()) basePrice = 21.99;
                break;
            case "Build Your Own":
                if (sSize.isChecked()) basePrice = 8.99;
                else if (mSize.isChecked()) basePrice = 10.99;
                else if (lSize.isChecked()) basePrice = 12.99;
                break;
        }

        return basePrice;
    }


    /**
     * Handles the topping selection and updates the number of selected toppings.
     *
     * @param count The number of selected toppings.
     */
    private void onToppingSelected(int count) {
        selectedToppingsCount = count;
        updatePizzaPrice();
    }

    /**
     * Adds the selected pizza to the current order.
     */
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

    /**
     * Returns the selected pizza size based on the radio button selection.
     *
     * @return The selected pizza size.
     */
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
