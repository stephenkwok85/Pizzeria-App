package pizzeria_package;

import java.util.List;

/**
 * Represents a Deluxe pizza with a predefined set of toppings and crust.
 * This class provides the price of the pizza based on its size.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class Deluxe extends Pizza {

    private static final double SMALL_PRICE = 16.99;
    private static final double MEDIUM_PRICE = 18.99;
    private static final double LARGE_PRICE = 20.99;

    /**
     * Constructs a Deluxe pizza with the specified style and crust.
     * Initializes the pizza with preset Deluxe toppings.
     *
     * @param style The style of the pizza (e.g., "NY Style" or "Chicago Style").
     * @param crust The crust type for the pizza.
     */
    public Deluxe(String style, Crust crust) {
        super(style);
        setCrust(crust);
        setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM));
    }

    /**
     * Returns the price of the Deluxe pizza based on its size.
     *
     * @return The price of the pizza.
     * @throws IllegalStateException if the pizza size is unexpected or invalid.
     */
    @Override
    public double price() {
        switch (getSize()) {
            case SMALL:
                return SMALL_PRICE;
            case MEDIUM:
                return MEDIUM_PRICE;
            case LARGE:
                return LARGE_PRICE;
            default:
                throw new IllegalStateException("Unexpected size: " + getSize());
        }
    }
}