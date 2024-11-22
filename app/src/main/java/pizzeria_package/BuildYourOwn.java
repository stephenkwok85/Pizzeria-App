package pizzeria_package;

import java.util.ArrayList;

/**
 * Represents a "Build Your Own" pizza that allows the customer to add custom toppings.
 * This class calculates the price based on the size of the pizza and the number of toppings.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class BuildYourOwn extends Pizza {

    private static final double BASE_PRICE_SMALL = 8.99;
    private static final double BASE_PRICE_MEDIUM = 10.99;
    private static final double BASE_PRICE_LARGE = 12.99;
    private static final double TOPPING_PRICE = 1.69;
    private static final int MAX_TOPPINGS = 7;

    /**
     * Constructs a "Build Your Own" pizza with the specified style and crust.
     * Initializes an empty list of toppings.
     *
     * @param style The style of the pizza (e.g., "NY Style" or "Chicago Style").
     * @param crust The crust type for the pizza.
     */
    public BuildYourOwn(String style, Crust crust) {
        super(style);
        setCrust(crust);
        setToppings(new ArrayList<>());
    }

    /**
     * Adds a topping to the pizza. Ensures the maximum topping limit is not exceeded.
     *
     * @param topping The topping to add.
     * @throws IllegalStateException if the maximum number of toppings is exceeded.
     */
    public void addTopping(Topping topping) {
        if (getToppings().size() < MAX_TOPPINGS) {
            getToppings().add(topping);
        } else {
            throw new IllegalStateException("Maximum of " + MAX_TOPPINGS + " toppings allowed");
        }
    }

    /**
     * Removes a specified topping from the pizza.
     *
     * @param topping The topping to remove.
     */
    public void removeTopping(Topping topping) {
        getToppings().remove(topping);
    }

    /**
     * Calculates the price of the pizza based on its size and number of toppings.
     *
     * @return The total price of the pizza.
     * @throws IllegalStateException if the pizza size is unexpected or invalid.
     */
    @Override
    public double price() {
        double basePrice;
        switch (getSize()) {
            case SMALL:
                basePrice = BASE_PRICE_SMALL;
                break;
            case MEDIUM:
                basePrice = BASE_PRICE_MEDIUM;
                break;
            case LARGE:
                basePrice = BASE_PRICE_LARGE;
                break;
            default:
                throw new IllegalStateException("Unexpected size: " + getSize());
        }
        return basePrice + (TOPPING_PRICE * getToppings().size());
    }
}