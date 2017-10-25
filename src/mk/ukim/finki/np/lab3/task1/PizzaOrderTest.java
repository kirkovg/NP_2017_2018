package mk.ukim.finki.np.lab3.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Item {
    int getPrice();

    int getCount();

    void setCount(int count);

    String getType();

}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super();
    }
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super();
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(String item) {
        super(item);
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
        super();
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
        super();
    }
}

class PizzaItem implements Item {
    private String type;
    private int price;
    private int count;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        switch (type) {
            case "Pepperoni":
                this.type = "Pepperoni";
                this.price = 12;
                break;
            case "Standard":
                this.type = "Standard";
                this.price = 10;
                break;
            case "Vegetarian":
                this.type = "Vegetarian";
                this.price = 8;
                break;
            default:
                throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PizzaItem pizzaItem = (PizzaItem) o;

        return type != null ? type.equals(pizzaItem.type) : pizzaItem.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}

class ExtraItem implements Item {
    private String type;
    private int price;
    private int count;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        switch (type) {
            case "Coke":
                this.type = "Coke";
                this.price = 5;
                break;
            case "Ketchup":
                this.type = "Ketchup";
                this.price = 3;
                break;
            default:
                throw new InvalidExtraTypeException();
        }
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtraItem extraItem = (ExtraItem) o;

        return type != null ? type.equals(extraItem.type) : extraItem.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}


class Order {
    private List<Item> items;
    public boolean locked;

    public Order() {
        items = new ArrayList<>();
        locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (locked) throw new OrderLockedException();
        if (count > 10) throw new ItemOutOfStockException(item.getType());
        if (items.contains(item)) {
            int indexToDelete = items.indexOf(item);
            items.remove(item);
            item.setCount(count);
            items.add(indexToDelete, item);
        } else {
            item.setCount(count);
            items.add(item);
        }

    }

    public int getPrice() {
        return items.stream().mapToInt(item -> item.getCount() * item.getPrice()).sum();
    }

    public void removeItem(int idx) throws OrderLockedException {
        if (locked) throw new OrderLockedException();
        if (items.get(idx) == null) throw new ArrayIndexOutOfBoundsException(idx);
        items.remove(idx);
    }

    public void lock() throws EmptyOrder {
        if (items.size() < 1) throw new EmptyOrder();
        locked = true;
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(String.format("%3s.%-15sx%2s%5s$\n",
                    i+1, items.get(i).getType(), items.get(i).getCount(), items.get(i).getPrice() * items.get(i).getCount()));
        }
        sb.append(String.format("%-22s%5s$","Total: ", this.getPrice()));
        System.out.println(sb.toString());
    }
}


public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}