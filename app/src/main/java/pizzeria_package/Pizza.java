package pizzeria_package;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for a pizza. This class defines the properties of a pizza,
 * including its toppings, crust, size, and style. Subclasses are required to
 * implement the {@code price} method to provide specific pricing based on pizza type.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public abstract class Pizza {
    private ArrayList<Topping> toppings;
    private Crust crust;
    private Size size;
    private String style;

    /**
     * Constructs a pizza with the specified style and initializes an empty list of toppings.
     *
     * @param style The style of the pizza (e.g., "NY Style" or "Chicago Style").
     */
    public Pizza(String style) {
        this.toppings = new ArrayList<>();
        this.style = style;
    }

    /**
     * Returns the style of the pizza.
     *
     * @return The style of the pizza.
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the style of the pizza.
     *
     * @param style The style to set.
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Abstract method to calculate the price of the pizza.
     * Subclasses must provide an implementation based on pizza specifics.
     *
     * @return The price of the pizza.
     */
    public abstract double price();

    /**
     * Returns the size of the pizza.
     *
     * @return The size of the pizza.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the size of the pizza.
     *
     * @param size The size to set.
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Returns the crust type of the pizza.
     *
     * @return The crust of the pizza.
     */
    public Crust getCrust() {
        return crust;
    }

    /**
     * Sets the crust type of the pizza.
     *
     * @param crust The crust type to set.
     */
    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    /**
     * Returns the list of toppings on the pizza.
     *
     * @return A list of toppings on the pizza.
     */
    public List<Topping> getToppings() {
        return toppings;
    }

    /**
     * Sets the list of toppings for the pizza.
     *
     * @param toppings The list of toppings to set.
     */
    public void setToppings(List<Topping> toppings) {
        this.toppings = new ArrayList<>(toppings);
    }

    /**
     * Adds a topping to the pizza if the maximum limit of 7 toppings is not exceeded.
     *
     * @param topping The topping to add.
     * @throws IllegalStateException if more than 7 toppings are added.
     */
    public void addTopping(Topping topping) {
        if (toppings.size() < 7) {
            toppings.add(topping);
        } else {
            throw new IllegalStateException("Maximum of 7 toppings allowed");
        }
    }

    /**
     * Removes a specified topping from the pizza.
     *
     * @param topping The topping to remove.
     */
    public void removeTopping(Topping topping) {
        toppings.remove(topping);
    }

    /**
     * Clears all toppings from the pizza.
     */
    public void clearToppings() {
        toppings.clear();
    }

    /**
     * Returns a string representation of the pizza, including its toppings,
     * crust, size, and price.
     *
     * @return A string summary of the pizza.
     */
    @Override
    public String toString() {
        return "Pizza{" +
                "toppings=" + toppings +
                ", crust=" + crust +
                ", size=" + size +
                ", price=" + String.format("%.2f", price()) +
                '}';
    }
}