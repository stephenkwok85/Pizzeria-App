package pizzeria_package;

/**
 * The PizzaFactory interface defines the contract for creating different types of pizzas.
 */
public interface PizzaFactory {
    Pizza createDeluxe();
    Pizza createMeatzza();
    Pizza createBBQChicken();
    Pizza createBuildYourOwn();
}

