import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {
  public LocalTime openingTime;
  public LocalTime closingTime;
  private String name;
  private String location;
  private List<Item> menu = new ArrayList<Item>();

  public Restaurant(String name, String location, LocalTime openingTime, LocalTime closingTime) {
    this.name = name;
    this.location = location;
    this.openingTime = openingTime;
    this.closingTime = closingTime;
  }

  public boolean isRestaurantOpen() {
    return !getCurrentTime().isBefore(openingTime) && !getCurrentTime().isAfter(closingTime);
  }

  public LocalTime getCurrentTime() {
    return LocalTime.now();
  }

  public List<Item> getMenu() {
    return menu;
  }

  private Item findItemByName(String itemName) {
    for (Item item : menu) {
      if (item.getName().equals(itemName)) return item;
    }
    return null;
  }

  public void addToMenu(String name, int price) {
    Item newItem = new Item(name, price);
    menu.add(newItem);
  }

  public void removeFromMenu(String itemName) throws itemNotFoundException {

    Item itemToBeRemoved = findItemByName(itemName);
    if (itemToBeRemoved == null) throw new itemNotFoundException(itemName);

    menu.remove(itemToBeRemoved);
  }

  public double estimatedBillAmount(ArrayList<String> chosenDishes) throws itemNotFoundException {
    double totalBill = 0;
    if (chosenDishes == null || chosenDishes.size() == 0) return totalBill;

    for (String dishName : chosenDishes) {
      Item dish = findItemByName(dishName);
      if (dish == null) throw new itemNotFoundException(dishName);
      totalBill += dish.getPrice();
    }
    return totalBill;
  }

  public String getName() {
    return name;
  }
}
