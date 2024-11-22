package pizzeria_package;

import java.util.List;

/**
 * Represents a Meatzza pizza with a predefined set of meat toppings and crust.
 * This class provides the price of the pizza based on its size.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class Meatzza extends Pizza {

    private static final double SMALL_PRICE = 17.99;
    private static final double MEDIUM_PRICE = 19.99;
    private static final double LARGE_PRICE = 21.99;

    /**
     * Constructs a Meatzza pizza with the specified style and crust.
     * Initializes the pizza with preset Meatzza toppings.
     *
     * @param style The style of the pizza (e.g., "NY Style" or "Chicago Style").
     * @param crust The crust type for the pizza.
     */
    public Meatzza(String style, Crust crust) {
        super(style);
        setCrust(crust);
        setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM));
    }

    /**
     * Returns the price of the Meatzza pizza based on its size.
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