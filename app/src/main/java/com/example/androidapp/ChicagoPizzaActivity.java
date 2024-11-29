package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private EditText crustField;
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

        chooseType = findViewById(R.id.chooseTypeSpinner);
        crustField = findViewById(R.id.crustField2);
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

        setupInitialValues();
    }

    private void setupInitialValues() {
        crustField.setText("Pan");
        crustField.setEnabled(false);
        sSize.setChecked(true);

        chooseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getSelectedItem().toString();
                setPizzaOptions(selectedType);
                isCustomizable = selectedType.equals("Build Your Own");
                lockToppings(!isCustomizable);
                updatePizzaPrice();
                updatePizzaImage(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when no item is selected (optional)
            }
        });

        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> updatePizzaPrice());
        setupToppingListeners();
        updatePizzaPrice();
    }

    private void setPizzaOptions(String pizzaType) {
        crustField.setText("");  // Clear crust text field
        resetToppingSelections();

        switch (pizzaType) {
            case "Deluxe":
                crustField.setText("Deep Dish");
                selectToppings(sausage, pepperoni, greenPepper, onion, mushroom);
                break;
            case "BBQ Chicken":
                crustField.setText("Pan");
                selectToppings(bbqChicken, greenPepper, provolone, cheddar);
                break;
            case "Meatzza":
                crustField.setText("Stuffed");
                selectToppings(sausage, pepperoni, beef, ham);
                break;
            case "Build Your Own":
                crustField.setText("Pan");
                selectedToppingsCount = 0;
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
                updatePizzaPrice();
            } else {
                showToastMessage("Max " + MAX_TOPPINGS + " toppings are allowed.");
            }
        } else {
            selectedToppingsCount--;
            updatePizzaPrice();
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updatePizzaPrice() {
        String selectedType = chooseType.getSelectedItem().toString();
        String selectedSize = ((RadioButton) findViewById(sizeGroup.getCheckedRadioButtonId())).getText().toString();

        // Calculate the base price based on the pizza type and size
        double basePrice = calculateBasePrice(selectedType, selectedSize);

        // Calculate the total price based on selected toppings
        int toppingCount = isCustomizable ? selectedToppingsCount : 0;
        double totalPrice = basePrice + (TOPPING_PRICE * toppingCount);

        // Update the price display
        pizzaPrice.setText(String.format("$%.2f", totalPrice));
    }

    private double calculateBasePrice(String type, String size) {
        // Calculate the base price for different types and sizes of pizza
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
        OrderManager.addOrderToCurrentOrder(pizza);
    }

    /**
     * Displays a confirmation message to the user once an order is added.
     */
    private void showOrderConfirmation() {
        int orderNumber = OrderManager.getCurrentOrderNumber();
        Toast.makeText(this, "Order #" + orderNumber + " has been added.", Toast.LENGTH_LONG).show();
    }

    /**
     * Resets the UI after an order is placed to allow for a new order.
     */
    private void resetOrderForm() {
        // Reset all fields to default
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

    /**
     * Updates the pizza image based on the selected pizza type.
     *
     * @param pizzaType The selected pizza type.
     */
    private void updatePizzaImage(String pizzaType) {
        ImageView pizzaImage = findViewById(R.id.pizzaImage);
        int imageResource = 0;

        switch (pizzaType) {
            case "Deluxe":
                imageResource = R.drawable.ch_deluxe;
                break;
            case "BBQ Chicken":
                imageResource = R.drawable.ch_bbq;
                break;
            case "Meatzza":
                imageResource = R.drawable.ch_meat;
                break;
            case "Build Your Own":
                imageResource = R.drawable.ch_build;
                break;
        }

        if (imageResource != 0) {
            pizzaImage.setImageResource(imageResource);
        }
    }
}
