package pizzeria_package;
import java.util.List;

/**
 * Factory class for creating various types of Chicago-style pizzas.
 * Implements the {@code PizzaFactory} interface to produce different
 * Chicago-style pizzas with specific crusts and toppings.
 *
 * @author Stephen Kwok and Jeongtae Kim
 */
public class ChicagoPizza implements PizzaFactory {

    /**
     * Creates a Chicago-style Deluxe pizza with deep dish crust and preset toppings.
     *
     * @return A new Chicago-style Deluxe pizza.
     */
    @Override
    public Pizza createDeluxe() {
        Pizza chicagoDeluxe = new Deluxe("Chicago", Crust.DEEP_DISH);
        chicagoDeluxe.setStyle("Chicago");
        chicagoDeluxe.setCrust(Crust.DEEP_DISH);
        chicagoDeluxe.setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM));
        return chicagoDeluxe;
    }

    /**
     * Creates a Chicago-style Meatzza pizza with stuffed crust and preset toppings.
     *
     * @return A new Chicago-style Meatzza pizza.
     */
    @Override
    public Pizza createMeatzza() {
        Pizza chicagoMeatzza = new Meatzza("Chicago", Crust.STUFFED);
        chicagoMeatzza.setStyle("Chicago");
        chicagoMeatzza.setCrust(Crust.STUFFED);
        chicagoMeatzza.setToppings(List.of(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM));
        return chicagoMeatzza;
    }

    /**
     * Creates a Chicago-style BBQ Chicken pizza with pan crust and preset toppings.
     *
     * @return A new Chicago-style BBQ Chicken pizza.
     */
    @Override
    public Pizza createBBQChicken() {
        Pizza chicagoBBQ = new BBQChicken("Chicago", Crust.PAN);
        chicagoBBQ.setStyle("Chicago");
        chicagoBBQ.setCrust(Crust.PAN);
        chicagoBBQ.setToppings(List.of(Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR));
        return chicagoBBQ;
    }

    /**
     * Creates a Chicago-style Build Your Own pizza with pan crust.
     * This pizza has no toppings by default and allows for customization.
     *
     * @return A new Chicago-style Build Your Own pizza.
     */
    @Override
    public Pizza createBuildYourOwn() {
        Pizza chicagoBuildYourOwn = new BuildYourOwn("Chicago", Crust.PAN);
        chicagoBuildYourOwn.setStyle("Chicago");
        chicagoBuildYourOwn.setCrust(Crust.PAN);
        return chicagoBuildYourOwn;
    }
}