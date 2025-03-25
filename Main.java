import java.util.InputMismatchException;
import java.util.Scanner;

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

class Order {
    protected int orderId;
    protected String customerName;
    protected String[] orderedItems;
    protected double totalPrice;
    protected String orderStatus;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderedItems = new String[0];
        this.totalPrice = 0.0;
        this.orderStatus = "Pending";
    }

    public Order() {
        this(0, "Unknown");
    }

    public void interact() {
        Scanner scanner = new Scanner(System.in);

        int numItems = 0;
        while (numItems <= 0) {
            System.out.print("Enter number of items ordered (positive number): ");
            try {
                numItems = scanner.nextInt();
                if (numItems <= 0) {
                    throw new InvalidInputException("Number of items must be a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine();

        orderedItems = new String[numItems];
        double[] itemPrices = new double[numItems];

        for (int i = 0; i < numItems; i++) {
            while (true) {
                System.out.print("Enter item " + (i + 1) + ": ");
                orderedItems[i] = scanner.nextLine().trim();
                if (!orderedItems[i].isEmpty()) {
                    break;
                }
                System.out.println("Item name cannot be empty. Please re-enter.");
            }

            while (true) {
                System.out.print("Enter price for " + orderedItems[i] + " (positive number): ");
                try {
                    itemPrices[i] = scanner.nextDouble();
                    if (itemPrices[i] <= 0) {
                        throw new InvalidInputException("Price must be a positive number.");
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid price.");
                    scanner.next();
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                }
            }
            scanner.nextLine();
        }

        calculateTotalPrice(itemPrices);
    }

    public void calculateTotalPrice(double[] itemPrices) {
        double sum = 0;
        for (double price : itemPrices) {
            sum += price;
        }
        totalPrice = sum;
    }

    public void printOrderDetails() {
        StringBuffer details = new StringBuffer();
        details.append("\nOrder ID: ").append(orderId)
                .append("\nCustomer Name: ").append(customerName)
                .append("\nOrdered Items: ");

        for (String item : orderedItems) {
            details.append(item).append(", ");
        }

        if (orderedItems.length > 0) {
            details.setLength(details.length() - 2); // Remove last comma and space
        }

        details.append("\nTotal Price: $").append(String.format("%.2f", totalPrice))
                .append("\nOrder Status: ").append(orderStatus);

        System.out.println(details.toString());
    }
}

class SpecialOrder extends Order {
    private double discount;
    private String specialNote;

    public SpecialOrder(int orderId, String customerName, double discount, String specialNote) {
        super(orderId, customerName);
        this.discount = discount;
        this.specialNote = specialNote;
    }

    @Override
    public void calculateTotalPrice(double[] itemPrices) {
        super.calculateTotalPrice(itemPrices);
        totalPrice -= discount;
        if (totalPrice < 0) totalPrice = 0; // Prevent negative total price
    }

    @Override
    public void printOrderDetails() {
        super.printOrderDetails();
        StringBuffer specialDetails = new StringBuffer();
        specialDetails.append("\nDiscount: $").append(String.format("%.2f", discount))
                .append("\nSpecial Note: ").append(specialNote);
        System.out.println(specialDetails.toString());
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numOrders = 0;
        while (numOrders <= 0) {
            System.out.print("Enter number of orders (positive number): ");
            try {
                numOrders = scanner.nextInt();
                if (numOrders <= 0) {
                    throw new InvalidInputException("Number of orders must be a positive integer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next(); // Clear invalid input
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine(); // Consume newline

        Order[] orders = new Order[numOrders];

        for (int i = 0; i < numOrders; i++) {
            System.out.println("\nEntering details for Order " + (i + 1) + ":");

            int orderId = 0;
            while (orderId <= 0) {
                System.out.print("Enter Order ID (positive integer): ");
                try {
                    orderId = scanner.nextInt();
                    if (orderId <= 0) {
                        throw new InvalidInputException("Order ID must be a positive integer.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    scanner.next();
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                }
            }
            scanner.nextLine();

            String customerName = "";
            while (customerName.isEmpty()) {
                System.out.print("Enter Customer Name: ");
                customerName = scanner.nextLine().trim();
                if (customerName.isEmpty()) {
                    System.out.println("Customer Name cannot be empty.");
                }
            }

            String isSpecialOrder = "";
            while (!isSpecialOrder.equalsIgnoreCase("yes") && !isSpecialOrder.equalsIgnoreCase("no")) {
                System.out.print("Is this a special order? (yes/no): ");
                isSpecialOrder = scanner.nextLine().trim();
                if (!isSpecialOrder.equalsIgnoreCase("yes") && !isSpecialOrder.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            if (isSpecialOrder.equalsIgnoreCase("yes")) {
                double discount = -1;
                while (discount < 0) {
                    System.out.print("Enter Discount (non-negative number): ");
                    try {
                        discount = scanner.nextDouble();
                        if (discount < 0) {
                            throw new InvalidInputException("Discount cannot be negative.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid discount.");
                        scanner.next();
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                    }
                }
                scanner.nextLine();

                System.out.print("Enter Special Note: ");
                String specialNote = scanner.nextLine().trim();

                orders[i] = new SpecialOrder(orderId, customerName, discount, specialNote);
            } else {
                orders[i] = new Order(orderId, customerName);
            }

            orders[i].interact();
        }

        System.out.println("\nAll Orders:");
        for (Order order : orders) {
            order.printOrderDetails();
            System.out.println("-------------------------");
        }

        scanner.close();
    }
}
