package pizzeria_package;

import com.example.androidapp.R;

/**
 * Enumeration representing various pizza toppings. Each topping
 * can be added to customize a pizza order.
 *
 * author Stephen Kwok and Jeongtae Kim
 */
public enum Topping {
    SAUSAGE(R.drawable.sausage),
    PEPPERONI(R.drawable.pepperoni),
    GREEN_PEPPER(R.drawable.green_pepper),
    ONION(R.drawable.onion),
    MUSHROOM(R.drawable.mushroom),
    BBQ_CHICKEN(R.drawable.bbq_chicken),
    BEEF(R.drawable.beef),
    HAM(R.drawable.ham),
    PROVOLONE(R.drawable.provolone),
    CHEDDAR(R.drawable.cheddar),
    OLIVES(R.drawable.olives),
    SPINACH(R.drawable.spinach),
    PINEAPPLE(R.drawable.pineapple),
    BACON(R.drawable.bacon);

    private final int imageResourceId;

    Topping(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    /**
     * Returns the image resource ID associated with this topping.
     *
     * @return The image resource ID.
     */
    public int getImageResourceId() {
        return imageResourceId;
    }

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
