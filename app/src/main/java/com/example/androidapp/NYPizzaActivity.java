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

import pizzeria_package.NYPizza;
import pizzeria_package.Pizza;
import pizzeria_package.PizzaFactory;
import pizzeria_package.Size;
import pizzeria_package.Topping;

/**
 * Activity to handle NY Pizza ordering.
 * Includes functionality for selecting pizza type, size, toppings, and adding pizzas to the order.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class NYPizzaActivity extends AppCompatActivity {
    private static final double TOPPING_PRICE = 1.69;
    private static final int MAX_TOPPINGS = 7;

    private static final double DELUXE_SMALL_PRICE = 16.99;
    private static final double DELUXE_MEDIUM_PRICE = 18.99;
    private static final double DELUXE_LARGE_PRICE = 20.99;

    private static final double BBQ_SMALL_PRICE = 15.99;
    private static final double BBQ_MEDIUM_PRICE = 17.99;
    private static final double BBQ_LARGE_PRICE = 19.99;

    private static final double MEATZZA_SMALL_PRICE = 17.99;
    private static final double MEATZZA_MEDIUM_PRICE = 19.99;
    private static final double MEATZZA_LARGE_PRICE = 21.99;

    private static final double BUILD_YOUR_OWN_SMALL_PRICE = 9.99;
    private static final double BUILD_YOUR_OWN_MEDIUM_PRICE = 11.99;
    private static final double BUILD_YOUR_OWN_LARGE_PRICE = 13.99;

    private static final String TYPE_DELUXE = "Deluxe";
    private static final String TYPE_BBQ_CHICKEN = "BBQ Chicken";
    private static final String TYPE_MEATZZA = "Meatzza";
    private static final String TYPE_BUILD_YOUR_OWN = "Build Your Own";
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
     * Called when the activity is created. Initializes UI elements, sets up listeners, and configures the pizza options.
     *
     * @param savedInstanceState The saved instance state (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ny_pizza);

        initializeViews();
        setupRecyclerView();
        setupSpinner();
        chooseType.setSelection(3);
        setPizzaOptions("Build Your Own");
        sSize.setChecked(true);
        setupListeners();
    }

    /**
     * Initializes the views by finding them from the layout.
     */
    private void initializeViews() {
        chooseType = findViewById(R.id.chooseTypeSpinner);
        crustField = findViewById(R.id.NYCrustTypeView);
        sSize = findViewById(R.id.sizeSmall);
        mSize = findViewById(R.id.sizeMedium);
        lSize = findViewById(R.id.sizeLarge);
        sizeGroup = findViewById(R.id.sizeRadioGroup);
        pizzaPrice = findViewById(R.id.pizzaPriceField);
        pizzaImage = findViewById(R.id.pizzaImage);
        toppingsRecyclerView = findViewById(R.id.toppingsRecyclerView);
        addToOrderButton = findViewById(R.id.addToOrderButton);

        pizzaFactory = new NYPizza();
    }

    /**
     * Sets up the RecyclerView to display the list of toppings, with a maximum number of selected toppings.
     */
    private void setupRecyclerView() {
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Topping> toppingsList = Arrays.asList(Topping.values());
        toppingAdapter = new ToppingAdapter(this, toppingsList, MAX_TOPPINGS, this::onToppingSelected);
        toppingsRecyclerView.setAdapter(toppingAdapter);
    }

    /**
     * Sets up the Spinner for selecting pizza type.
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
     * Sets up listeners for user interactions, such as size selection and adding the pizza to the order.
     */
    private void setupListeners() {
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> updatePizzaPrice());
        addToOrderButton.setOnClickListener(view -> addOrder());
    }

    /**
     * Configures the pizza options (crust type, toppings, and image) based on the selected pizza type.
     *
     * @param pizzaType The selected pizza type.
     */
    private void setPizzaOptions(String pizzaType) {
        selectedToppingsCount = 0;

        String crustType;
        int imageResource;

        switch (pizzaType) {
            case "Deluxe":
                crustType = "Brooklyn";
                imageResource = R.drawable.ny_deluxe;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM
                ), false);
                isCustomizable = false;
                break;
            case "BBQ Chicken":
                crustType = "Thin";
                imageResource = R.drawable.ny_bbq;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR
                ), false);
                isCustomizable = false;
                break;
            case "Meatzza":
                crustType = "Hand-tossed";
                imageResource = R.drawable.ny_meat;
                toppingAdapter.setToppings(Arrays.asList(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM
                ), false);
                isCustomizable = false;
                break;
            case "Build Your Own":
            default:
                crustType = "Hand-tossed";
                imageResource = R.drawable.ny_build;
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
     * Called when a topping is selected or deselected. Updates the count of selected toppings.
     *
     * @param count The number of selected toppings.
     */
    private void onToppingSelected(int count) {
        selectedToppingsCount = count;
        updatePizzaPrice();
    }

    /**
     * Updates the displayed pizza price based on the selected size and toppings.
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
            case TYPE_DELUXE:
                if (sSize.isChecked()) basePrice = DELUXE_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = DELUXE_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = DELUXE_LARGE_PRICE;
                break;
            case TYPE_BBQ_CHICKEN:
                if (sSize.isChecked()) basePrice = BBQ_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = BBQ_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = BBQ_LARGE_PRICE;
                break;
            case TYPE_MEATZZA:
                if (sSize.isChecked()) basePrice = MEATZZA_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = MEATZZA_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = MEATZZA_LARGE_PRICE;
                break;
            case TYPE_BUILD_YOUR_OWN:
                if (sSize.isChecked()) basePrice = BUILD_YOUR_OWN_SMALL_PRICE;
                else if (mSize.isChecked()) basePrice = BUILD_YOUR_OWN_MEDIUM_PRICE;
                else if (lSize.isChecked()) basePrice = BUILD_YOUR_OWN_LARGE_PRICE;
                break;
        }

        return basePrice;
    }


    /**
     * Adds the current pizza to the order, including the selected toppings and size.
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
     * Gets the selected pizza size.
     *
     * @return The selected size.
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
