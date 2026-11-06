import model.Product;
import service.InventoryService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        InventoryService inventoryService =
                new InventoryService("data/inventory.csv");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Remove Product");
            System.out.println("5. Calculate Inventory Value");
            System.out.println("6. View Low Stock Products");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Product name: ");
                    String name = scanner.nextLine();

                    System.out.print("Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Price: ");
                    double price = scanner.nextDouble();

                    System.out.print("Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    inventoryService.addProduct(
                            name, category, price, quantity
                    );
                    System.out.println("Product added successfully!");
                    break;

                case 2:
                    List<Product> products =
                            inventoryService.getAllProducts();

                    products.forEach(System.out::println);
                    break;

                case 3:
                    System.out.print("Enter product name: ");
                    String searchName = scanner.nextLine();

                    inventoryService.searchByName(searchName)
                            .forEach(System.out::println);
                    break;

                case 4:
                    System.out.print("Enter product ID: ");
                    String id = scanner.nextLine();

                    if (inventoryService.removeProduct(id)) {
                        System.out.println("Product removed successfully!");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 5:
                    System.out.println(
                            "Total inventory value: "
                                    + inventoryService.getTotalInventoryValue()
                    );
                    break;

                case 6:
                    inventoryService.getLowStockProducts()
                            .forEach(System.out::println);
                    break;

                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}