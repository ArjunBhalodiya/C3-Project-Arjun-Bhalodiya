import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    @Spy Restaurant restaurant;
    int initialMenuSize;

    @BeforeEach
    public void SetupRestaurant() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");

        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        initialMenuSize = restaurant.getMenu().size();
        restaurant = Mockito.spy(restaurant);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>BILL<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void no_chosen_item_then_estimated_restaurant_bill_should_be_zero()
            throws itemNotFoundException {
        assertEquals(0, restaurant.estimatedBillAmount(new ArrayList<String>()));
        assertEquals(0, restaurant.estimatedBillAmount(null));
    }

    @Test
    public void one_or_more_chosen_item_than_estimated_restaurant_bill_should_be_sum_of_item_price()
            throws itemNotFoundException {
        restaurant.addToMenu("Pizza", 499);
        restaurant.addToMenu("Sandwich", 249);

        List<String> chosenDishes = new ArrayList<>();
        chosenDishes.add("Pizza");
        chosenDishes.add("Sandwich");

        assertEquals(499 + 249, restaurant.estimatedBillAmount(new ArrayList<>(chosenDishes)));
    }

    @Test
    public void
    one_or_more_chosen_item_not_available_in_menu_should_throw_item_not_found_exception() {
        restaurant.addToMenu("Pizza", 499);

        List<String> chosenDishes = new ArrayList<>();
        chosenDishes.add("Pizza");
        chosenDishes.add("Sandwich");

        assertThrows(
                itemNotFoundException.class,
                () -> restaurant.estimatedBillAmount(new ArrayList<>(chosenDishes)));
    }
    // <<<<<<<<<<<<<<<<<<<<<<<BILL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    // >>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        LocalTime currentTime = LocalTime.parse("12:00:00");
        Mockito.when(restaurant.getCurrentTime()).thenReturn(currentTime);
        assertEquals(true, restaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        LocalTime currentTime = LocalTime.parse("09:00:00");
        Mockito.when(restaurant.getCurrentTime()).thenReturn(currentTime);
        assertEquals(false, restaurant.isRestaurantOpen());
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {
        restaurant.addToMenu("Sizzling brownie", 319);

        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1()
            throws itemNotFoundException {
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class, () -> restaurant.removeFromMenu("French fries"));
    }
    // <<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}