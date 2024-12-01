package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pizzeria_package.ChicagoPizza;
import pizzeria_package.Pizza;
import pizzeria_package.PizzaFactory;
import pizzeria_package.BuildYourOwn;
import pizzeria_package.Topping;
import pizzeria_package.Size;

public class ChicagoPizzaActivity extends AppCompatActivity {
    private static final double TOPPING_PRICE = 1.69;
    private static final int MAX_TOPPINGS = 7;
    private static final double DELUXE_S_PRICE = 16.99;
    private static final double DELUXE_M_PRICE = 18.99;
    private static final double DELUXE_L_PRICE = 20.99;
    private static final double BBQ_S_PRICE = 14.99;
    private static final double BBQ_M_PRICE = 16.99;
    private static final double BBQ_L_PRICE = 19.99;
    private static final double MEATZZA_S_PRICE = 17.99;
    private static final double MEATZZA_M_PRICE = 19.99;
    private static final double MEATZZA_L_PRICE = 21.99;
    private static final double BYO_S_PRICE = 8.99;
    private static final double BYO_M_PRICE = 10.99;
    private static final double BYO_L_PRICE = 12.99;

    private Spinner chooseType;
    private TextView crustField;
    private RadioButton sSize, mSize, lSize;
    private RadioGroup sizeGroup;
    private CheckBox sausage, pepperoni, greenPepper, onion, mushroom, bbqChicken, beef, ham, provolone, cheddar, olives, spinach, pineapple, bacon;
    private TextView pizzaPrice;
    private ImageView pizzaImage;
    private Button addToOrderButton;

    private boolean isCustomizable = false;
    private int selectedToppingsCount = 0;

    private PizzaFactory pizzaFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago_pizza);

        // Initialize views
        chooseType = findViewById(R.id.chooseTypeSpinner);
        crustField = findViewById(R.id.crustTypeView);
        sSize = findViewById(R.id.sizeSmall);
        mSize = findViewById(R.id.sizeMedium);
        lSize = findViewById(R.id.sizeLarge);
        sizeGroup = findViewById(R.id.sizeRadioGroup);
        sausage = findViewById(R.id.toppingSausage);
        pepperoni = findViewById(R.id.toppingPepperoni);
        greenPepper = findViewById(R.id.toppingGreenPepper);
        onion = findViewById(R.id.toppingOnion);
        mushroom = findViewById(R.id.toppingMushroom);
        bbqChicken = findViewById(R.id.toppingBBQChicken);
        beef = findViewById(R.id.toppingBeef);
        ham = findViewById(R.id.toppingHam);
        provolone = findViewById(R.id.toppingProvolone);
        cheddar = findViewById(R.id.toppingCheddar);
        olives = findViewById(R.id.toppingOlives);
        spinach = findViewById(R.id.toppingSpinach);
        pineapple = findViewById(R.id.toppingPineapple);
        bacon = findViewById(R.id.toppingBacon);
        pizzaPrice = findViewById(R.id.pizzaPriceField);
        pizzaImage = findViewById(R.id.pizzaImage);
        addToOrderButton = findViewById(R.id.addToOrderButton);

        pizzaFactory = new ChicagoPizza();

        // Set up spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Deluxe", "BBQ Chicken", "Meatzza", "Build Your Own"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseType.setAdapter(adapter);

        // Set default selection for pizza type to Build Your Own
        chooseType.setSelection(3);  // "Build Your Own" is at index 3
        setPizzaOptions("Build Your Own");  // Set the pizza options to Build Your Own

        // Set default size to Small
        sSize.setChecked(true);  // Select the small size radio button

        // Set listener for the pizza type spinner
        chooseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getItemAtPosition(position).toString();
                setPizzaOptions(selectedType);
                updatePizzaPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if no selection
            }
        });

        // Set listener for size radio group
        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updatePizzaPrice();
        });

        // Set up topping listeners
        setupToppingListeners();

        // Update pizza price
        updatePizzaPrice();

        // Add to Order button listener
        addToOrderButton.setOnClickListener(view -> {
            Log.d("PizzaOrder", "Add to Order button clicked");
            addOrder();
        });
    }

    private void setPizzaOptions(String pizzaType) {
        resetToppingSelections();
        selectedToppingsCount = 0;

        String crustType = "";

        switch (pizzaType) {
            case "Deluxe":
                crustType = "Deep Dish";
                selectToppings(sausage, pepperoni, greenPepper, onion, mushroom);
                crustField.setText(crustType);
                lockToppings(true);
                break;
            case "BBQ Chicken":
                crustType = "Pan";
                selectToppings(bbqChicken, greenPepper, provolone, cheddar);
                crustField.setText(crustType);
                lockToppings(true);
                break;
            case "Meatzza":
                crustType = "Stuffed";
                selectToppings(sausage, pepperoni, beef, ham);
                crustField.setText(crustType);
                lockToppings(true);
                break;
            case "Build Your Own":
                crustType = "Pan";
                selectedToppingsCount = 0;
                isCustomizable = true;
                crustField.setText(crustType);
                lockToppings(false);
                break;
        }
    }

    private void selectToppings(CheckBox... toppings) {
        for (CheckBox topping : toppings) {
            topping.setChecked(true);
        }
    }

    private void resetToppingSelections() {
        sausage.setChecked(false);
        pepperoni.setChecked(false);
        greenPepper.setChecked(false);
        onion.setChecked(false);
        mushroom.setChecked(false);
        bbqChicken.setChecked(false);
        beef.setChecked(false);
        ham.setChecked(false);
        provolone.setChecked(false);
        cheddar.setChecked(false);
        olives.setChecked(false);
        spinach.setChecked(false);
        pineapple.setChecked(false);
        bacon.setChecked(false);
    }

    private void lockToppings(boolean lock) {
        sausage.setEnabled(!lock);
        pepperoni.setEnabled(!lock);
        greenPepper.setEnabled(!lock);
        onion.setEnabled(!lock);
        mushroom.setEnabled(!lock);
        bbqChicken.setEnabled(!lock);
        beef.setEnabled(!lock);
        ham.setEnabled(!lock);
        provolone.setEnabled(!lock);
        cheddar.setEnabled(!lock);
        olives.setEnabled(!lock);
        spinach.setEnabled(!lock);
        pineapple.setEnabled(!lock);
        bacon.setEnabled(!lock);
    }

    private void setupToppingListeners() {
        sausage.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        pepperoni.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        greenPepper.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        onion.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        mushroom.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        bbqChicken.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        beef.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        ham.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        provolone.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        cheddar.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        olives.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        spinach.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        pineapple.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
        bacon.setOnCheckedChangeListener((buttonView, isChecked) -> updateToppingCount(isChecked));
    }

    private void updateToppingCount(boolean isChecked) {
        if (isChecked) {
            if (selectedToppingsCount < MAX_TOPPINGS) {
                selectedToppingsCount++;
            } else {
                Toast.makeText(ChicagoPizzaActivity.this, "You can only select up to " + MAX_TOPPINGS + " toppings.", Toast.LENGTH_SHORT).show();
                // Uncheck the last selected topping
                uncheckLastTopping();
            }
        } else {
            selectedToppingsCount--;
        }
        updatePizzaPrice();
    }

    private void uncheckLastTopping() {
        // Check all toppings and uncheck the last one if the limit of MAX_TOPPINGS is reached
        if (sausage.isChecked()) sausage.setChecked(false);
        else if (pepperoni.isChecked()) pepperoni.setChecked(false);
        else if (greenPepper.isChecked()) greenPepper.setChecked(false);
        else if (onion.isChecked()) onion.setChecked(false);
        else if (mushroom.isChecked()) mushroom.setChecked(false);
        else if (bbqChicken.isChecked()) bbqChicken.setChecked(false);
        else if (beef.isChecked()) beef.setChecked(false);
        else if (ham.isChecked()) ham.setChecked(false);
        else if (provolone.isChecked()) provolone.setChecked(false);
        else if (cheddar.isChecked()) cheddar.setChecked(false);
        else if (olives.isChecked()) olives.setChecked(false);
        else if (spinach.isChecked()) spinach.setChecked(false);
        else if (pineapple.isChecked()) pineapple.setChecked(false);
        else if (bacon.isChecked()) bacon.setChecked(false);
    }

    private void updatePizzaPrice() {
        double price = 0.0;

        if (sSize.isChecked()) {
            price += getSelectedPizzaBasePrice(DELUXE_S_PRICE, BBQ_S_PRICE, MEATZZA_S_PRICE, BYO_S_PRICE);
        } else if (mSize.isChecked()) {
            price += getSelectedPizzaBasePrice(DELUXE_M_PRICE, BBQ_M_PRICE, MEATZZA_M_PRICE, BYO_M_PRICE);
        } else if (lSize.isChecked()) {
            price += getSelectedPizzaBasePrice(DELUXE_L_PRICE, BBQ_L_PRICE, MEATZZA_L_PRICE, BYO_L_PRICE);
        }

        // Add topping costs if the pizza is customizable
        if (isCustomizable) {
            price += selectedToppingsCount * TOPPING_PRICE;
        }

        pizzaPrice.setText("Price: $" + String.format("%.2f", price));
    }

    private double getSelectedPizzaBasePrice(double deluxePrice, double bbqPrice, double meatzzaPrice, double byoPrice) {
        String selectedType = chooseType.getSelectedItem().toString();
        switch (selectedType) {
            case "Deluxe":
                return deluxePrice;
            case "BBQ Chicken":
                return bbqPrice;
            case "Meatzza":
                return meatzzaPrice;
            case "Build Your Own":
                return byoPrice;
            default:
                return 0.0;
        }
    }

    private void addOrder() {
        String selectedType = chooseType.getSelectedItem().toString();
        Size pizzaSize = getPizzaSize();
        List<Topping> selectedToppings = getSelectedToppings();

        Pizza pizza = null;

        // Use the pizzaFactory instance to create the pizza based on the selected type
        switch (selectedType) {
            case "Deluxe":
                pizza = pizzaFactory.createDeluxe();
                break;
            case "BBQ Chicken":
                pizza = pizzaFactory.createBBQChicken();
                break;
            case "Meatzza":
                pizza = pizzaFactory.createMeatzza();
                break;
            case "Build Your Own":
                pizza = pizzaFactory.createBuildYourOwn();
                break;
        }

        // Set the pizza size and toppings
        if (pizza != null) {
            pizza.setSize(pizzaSize);
            pizza.setToppings(selectedToppings);
        }

        // Save the order
        OrderManager.addOrderToCurrentOrder(pizza);

        // Retrieve the current order number
        int orderNumber = OrderManager.getCurrentOrderNumber();

        // Show a confirmation message with the order number
        Toast.makeText(ChicagoPizzaActivity.this, "Order #" + orderNumber + " added to your cart", Toast.LENGTH_SHORT).show();
    }

    private Size getPizzaSize() {
        if (sSize.isChecked()) {
            return Size.SMALL;
        } else if (mSize.isChecked()) {
            return Size.MEDIUM;
        } else if (lSize.isChecked()) {
            return Size.LARGE;
        } else {
            return Size.SMALL;  // Default fallback
        }
    }

    private List<Topping> getSelectedToppings() {
        List<Topping> toppings = new ArrayList<>();
        if (sausage.isChecked()) toppings.add(Topping.SAUSAGE);
        if (pepperoni.isChecked()) toppings.add(Topping.PEPPERONI);
        if (greenPepper.isChecked()) toppings.add(Topping.GREEN_PEPPER);
        if (onion.isChecked()) toppings.add(Topping.ONION);
        if (mushroom.isChecked()) toppings.add(Topping.MUSHROOM);
        if (bbqChicken.isChecked()) toppings.add(Topping.BBQ_CHICKEN);
        if (beef.isChecked()) toppings.add(Topping.BEEF);
        if (ham.isChecked()) toppings.add(Topping.HAM);
        if (provolone.isChecked()) toppings.add(Topping.PROVOLONE);
        if (cheddar.isChecked()) toppings.add(Topping.CHEDDAR);
        if (olives.isChecked()) toppings.add(Topping.OLIVES);
        if (spinach.isChecked()) toppings.add(Topping.SPINACH);
        if (pineapple.isChecked()) toppings.add(Topping.PINEAPPLE);
        if (bacon.isChecked()) toppings.add(Topping.BACON);
        return toppings;
    }
}
