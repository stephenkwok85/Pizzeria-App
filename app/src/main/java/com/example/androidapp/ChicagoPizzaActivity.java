package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    private boolean isCustomizable = false;
    private int selectedToppingsCount = 0;

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
                disableExtraToppings();
            } else {
                showToastMessage("Max " + MAX_TOPPINGS + " toppings are allowed.");
                uncheckTopping();
            }
        } else {
            selectedToppingsCount--;
            enableAllToppings();
        }

        updatePizzaPrice();
    }

    private void disableExtraToppings() {
        if (selectedToppingsCount >= MAX_TOPPINGS) {
            sausage.setEnabled(sausage.isChecked());
            pepperoni.setEnabled(pepperoni.isChecked());
            greenPepper.setEnabled(greenPepper.isChecked());
            onion.setEnabled(onion.isChecked());
            mushroom.setEnabled(mushroom.isChecked());
            bbqChicken.setEnabled(bbqChicken.isChecked());
            beef.setEnabled(beef.isChecked());
            ham.setEnabled(ham.isChecked());
            provolone.setEnabled(provolone.isChecked());
            cheddar.setEnabled(cheddar.isChecked());
            olives.setEnabled(olives.isChecked());
            spinach.setEnabled(spinach.isChecked());
            pineapple.setEnabled(pineapple.isChecked());
            bacon.setEnabled(bacon.isChecked());
        }
    }

    private void uncheckTopping() {
        if (sausage.isChecked()) {
            sausage.setChecked(false);
        } else if (pepperoni.isChecked()) {
            pepperoni.setChecked(false);
        } else if (greenPepper.isChecked()) {
            greenPepper.setChecked(false);
        } else if (onion.isChecked()) {
            onion.setChecked(false);
        } else if (mushroom.isChecked()) {
            mushroom.setChecked(false);
        } else if (bbqChicken.isChecked()) {
            bbqChicken.setChecked(false);
        } else if (beef.isChecked()) {
            beef.setChecked(false);
        } else if (ham.isChecked()) {
            ham.setChecked(false);
        } else if (provolone.isChecked()) {
            provolone.setChecked(false);
        } else if (cheddar.isChecked()) {
            cheddar.setChecked(false);
        } else if (olives.isChecked()) {
            olives.setChecked(false);
        } else if (spinach.isChecked()) {
            spinach.setChecked(false);
        } else if (pineapple.isChecked()) {
            pineapple.setChecked(false);
        } else if (bacon.isChecked()) {
            bacon.setChecked(false);
        }
    }

    private void enableAllToppings() {
        if (selectedToppingsCount < MAX_TOPPINGS) {
            sausage.setEnabled(true);
            pepperoni.setEnabled(true);
            greenPepper.setEnabled(true);
            onion.setEnabled(true);
            mushroom.setEnabled(true);
            bbqChicken.setEnabled(true);
            beef.setEnabled(true);
            ham.setEnabled(true);
            provolone.setEnabled(true);
            cheddar.setEnabled(true);
            olives.setEnabled(true);
            spinach.setEnabled(true);
            pineapple.setEnabled(true);
            bacon.setEnabled(true);
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updatePizzaPrice() {
        // Safely get the selected pizza type
        String selectedType = chooseType.getSelectedItem() != null ? chooseType.getSelectedItem().toString() : "";

        // Safely get the selected size
        RadioButton selectedRadioButton = findViewById(sizeGroup.getCheckedRadioButtonId());
        String selectedSize = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        // Calculate the base price based on the pizza type and size
        double basePrice = calculateBasePrice(selectedType, selectedSize);

        // Calculate the total price based on selected toppings (only for Build Your Own)
        int toppingCount = isCustomizable ? selectedToppingsCount : 0; // Only count toppings if it's Build Your Own

        double totalPrice = basePrice + (TOPPING_PRICE * toppingCount);

        // Update the price display
        if (pizzaPrice != null) {
            pizzaPrice.setText(String.format("$%.2f", totalPrice));
        } else {
            Log.e("ChicagoPizzaActivity", "pizzaPrice TextView is null.");
        }
    }

    private double calculateBasePrice(String type, String size) {
        switch (type) {
            case "Deluxe":
                return size.equals("S") ? DELUXE_S_PRICE : size.equals("M") ? DELUXE_M_PRICE : DELUXE_L_PRICE;
            case "BBQ Chicken":
                return size.equals("S") ? BBQ_S_PRICE : size.equals("M") ? BBQ_M_PRICE : BBQ_L_PRICE;
            case "Meatzza":
                return size.equals("S") ? MEATZZA_S_PRICE : size.equals("M") ? MEATZZA_M_PRICE : MEATZZA_L_PRICE;
            case "Build Your Own":
                return size.equals("S") ? BYO_S_PRICE : size.equals("M") ? BYO_M_PRICE : BYO_L_PRICE;
            default:
                return 0.0;
        }
    }

    /**
     * Handles the addition of a pizza order.
     * This method will be invoked when the user selects the "Add to Order" button.
     */
    public void addOrder(View view) {
        Pizza pizza = createPizza();
        setPizzaSize(pizza);
        addPizzaToOrder(pizza);
        showOrderConfirmation();
    }

    /**
     * Creates a pizza object based on the selected pizza type and toppings.
     *
     * @return The created pizza object.
     */
    private Pizza createPizza() {
        PizzaFactory pizzaFactory = new ChicagoPizza();
        Pizza pizza;

        switch (chooseType.getSelectedItem().toString()) {
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
                addCustomToppings((BuildYourOwn) pizza);
                break;
            default:
                throw new IllegalArgumentException("Invalid pizza type selected");
        }
        return pizza;
    }

    /**
     * Sets the pizza size based on the user's selection.
     *
     * @param pizza The pizza to which the size is being set.
     */
    private void setPizzaSize(Pizza pizza) {
        RadioButton selectedSizeButton = findViewById(sizeGroup.getCheckedRadioButtonId());
        String selectedSize = selectedSizeButton.getText().toString();

        switch (selectedSize) {
            case "S":
            case "Small":
                pizza.setSize(Size.SMALL);
                break;
            case "M":
            case "Medium":
                pizza.setSize(Size.MEDIUM);
                break;
            case "L":
            case "Large":
                pizza.setSize(Size.LARGE);
                break;
            default:
                throw new IllegalStateException("Unexpected size: " + selectedSize);
        }
    }

    /**
     * Adds the created pizza to the current order.
     *
     * @param pizza The pizza to add to the order.
     */
    private void addPizzaToOrder(Pizza pizza) {
        // Assuming OrderManager has been set up to manage orders
        //OrderManager.addOrderToCurrentOrder(pizza);
    }

    /**
     * Displays a confirmation message to the user once an order is added.
     */
    private void showOrderConfirmation() {
        //int orderNumber = OrderManager.getCurrentOrderNumber();
        //Toast.makeText(this, "Order #" + orderNumber + " has been added.", Toast.LENGTH_LONG).show();
    }

    /**
     * Resets the UI after an order is placed to allow for a new order.
     */
    private void resetOrderForm() {
        chooseType.setSelection(0);
        sizeGroup.clearCheck();
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

        pizzaPrice.setText("$0.00");
    }

    /**
     * Adds the selected toppings to the "Build Your Own" pizza.
     *
     * @param pizza The pizza to which toppings will be added.
     */
    private void addCustomToppings(BuildYourOwn pizza) {
        if (sausage.isChecked()) pizza.addTopping(Topping.SAUSAGE);
        if (pepperoni.isChecked()) pizza.addTopping(Topping.PEPPERONI);
        if (greenPepper.isChecked()) pizza.addTopping(Topping.GREEN_PEPPER);
        if (onion.isChecked()) pizza.addTopping(Topping.ONION);
        if (mushroom.isChecked()) pizza.addTopping(Topping.MUSHROOM);
        if (bbqChicken.isChecked()) pizza.addTopping(Topping.BBQ_CHICKEN);
        if (beef.isChecked()) pizza.addTopping(Topping.BEEF);
        if (ham.isChecked()) pizza.addTopping(Topping.HAM);
        if (provolone.isChecked()) pizza.addTopping(Topping.PROVOLONE);
        if (cheddar.isChecked()) pizza.addTopping(Topping.CHEDDAR);
        if (olives.isChecked()) pizza.addTopping(Topping.OLIVES);
        if (spinach.isChecked()) pizza.addTopping(Topping.SPINACH);
        if (pineapple.isChecked()) pizza.addTopping(Topping.PINEAPPLE);
        if (bacon.isChecked()) pizza.addTopping(Topping.BACON);

        // Ensure that the maximum number of toppings is not exceeded
        if (pizza.getToppings().size() > MAX_TOPPINGS) {
            pizza.removeTopping(pizza.getToppings().get(MAX_TOPPINGS));
        }
    }
}
