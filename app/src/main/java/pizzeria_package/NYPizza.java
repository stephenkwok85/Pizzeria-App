package pizzeria_package;
import java.util.List;

/**
 * Factory class for creating various types of New York-style pizzas.
 * Implements the {@code PizzaFactory} interface to produce different
 * NY-style pizzas with specific crusts and toppings.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class NYPizza implements PizzaFactory {

    /**
     * Creates a NY-style Deluxe pizza with Brooklyn crust and preset toppings.
     *
     * @return A new NY-style Deluxe pizza.
     */
    @Override
    public Pizza createDeluxe() {
        Pizza nyDeluxe = new Deluxe("NY", Crust.BROOKLYN);
        nyDeluxe.setStyle("NY");
        nyDeluxe.setCrust(Crust.BROOKLYN);
        nyDeluxe.setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM));
        return nyDeluxe;
    }

    /**
     * Creates a NY-style Meatzza pizza with hand-tossed crust and preset toppings.
     *
     * @return A new NY-style Meatzza pizza.
     */
    @Override
    public Pizza createMeatzza() {
        Pizza nyMeatzza = new Meatzza("NY", Crust.HAND_TOSSED);
        nyMeatzza.setStyle("NY");
        nyMeatzza.setCrust(Crust.HAND_TOSSED);
        nyMeatzza.setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM));
        return nyMeatzza;
    }

    /**
     * Creates a NY-style BBQ Chicken pizza with thin crust and preset toppings.
     *
     * @return A new NY-style BBQ Chicken pizza.
     */
    @Override
    public Pizza createBBQChicken() {
        Pizza nyBBQ = new BBQChicken("NY", Crust.THIN);
        nyBBQ.setStyle("NY");
        nyBBQ.setCrust(Crust.THIN);
        nyBBQ.setToppings(List.of(Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR));
        return nyBBQ;
    }

    /**
     * Creates a NY-style Build Your Own pizza with hand-tossed crust.
     * This pizza has no toppings by default and allows for customization.
     *
     * @return A new NY-style Build Your Own pizza.
     */
    @Override
    public Pizza createBuildYourOwn() {
        Pizza nyBuildYourOwn = new BuildYourOwn("NY", Crust.HAND_TOSSED);
        nyBuildYourOwn.setStyle("NY");
        nyBuildYourOwn.setCrust(Crust.HAND_TOSSED);
        return nyBuildYourOwn;
    }
}