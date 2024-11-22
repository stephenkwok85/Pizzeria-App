package pizzeria_package;

/**
 * Enumeration representing various pizza toppings. Each topping
 * can be added to customize a pizza order.
 *
 * author Stephen Kwok and Jeongtae Kim
 */
public enum Topping {
    SAUSAGE,
    PEPPERONI,
    GREEN_PEPPER,
    ONION,
    MUSHROOM,
    BBQ_CHICKEN,
    BEEF,
    HAM,
    PROVOLONE,
    CHEDDAR,
    OLIVES,
    SPINACH,
    PINEAPPLE,
    BACON;

    /**
     * Returns a formatted string representation of the topping.
     * Converts the enum name to lowercase and capitalizes the first letter,
     * replacing underscores with spaces (e.g., "GREEN_PEPPER" becomes "Green pepper").
     *
     * @return A formatted string representation of the topping.
     */
    @Override
    public String toString() {
        String name = name().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
