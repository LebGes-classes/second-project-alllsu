package org.example;

import java.util.Scanner;

public class CustomerMenu {
    private final Manager manager;
    private final Scanner scanner;

    public CustomerMenu(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showCustomerMenu() {
        while (true) {
            System.out.println("\n=== Меню покупателя ===");
            System.out.println("1. Просмотреть товары в пункте продаж");
            System.out.println("2. Добавить товар в корзину");
            System.out.println("3. Удалить товар из корзины");
            System.out.println("4. Просмотреть корзину");
            System.out.println("5. Оформить заказ");
            System.out.println("6. Просмотреть историю покупок");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewSalesPointProducts();
                case 2 -> addToCart();
                case 3 -> removeFromCart();
                case 4 -> viewCart();
                case 5 -> checkout();
                case 6 -> viewPurchaseHistory();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void viewSalesPointProducts() {
        System.out.println("\nДоступные пункты продаж:");
        manager.listSalesPoints();
        System.out.print("Введите ID пункта продаж: ");
        int salesPointId = scanner.nextInt();
        manager.printSalesPointProducts(salesPointId);
    }

    private void addToCart() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();

        System.out.println("\nДоступные пункты продаж:");
        manager.listSalesPoints();
        System.out.print("Введите ID пункта продаж: ");
        int salesPointId = scanner.nextInt();

        System.out.println("\nТовары в пункте продаж ID " + salesPointId + ":");
        manager.printSalesPointProducts(salesPointId);
        System.out.print("Введите ID товара: ");
        int productId = scanner.nextInt();

        Customer customer = manager.getCustomers().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        SalesPoint salesPoint = manager.getSalesPoints().stream()
                .filter(sp -> sp.getId() == salesPointId)
                .findFirst()
                .orElse(null);

        Product product = salesPoint != null ? salesPoint.getProducts().stream()
                .filter(p -> p.getId() == productId && p.isAvailable())
                .findFirst()
                .orElse(null) : null;

        if (customer == null || salesPoint == null || product == null) {
            System.out.println("Ошибка: Неверный ID покупателя, пункта продаж или товара");
            return;
        }

        if (customer.addToCart(product)) {
            System.out.println("Товар добавлен в корзину");
        } else {
            System.out.println("Не удалось добавить товар в корзину");
        }
    }

    private void removeFromCart() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();

        Customer customer = manager.getCustomers().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        if (customer == null) {
            System.out.println("Ошибка: Покупатель не найден");
            return;
        }

        System.out.println("\n" + customer.viewCart());
        System.out.print("Введите ID товара: ");
        int productId = scanner.nextInt();

        if (customer.removeFromCart(productId)) {
            System.out.println("Товар удален из корзины");
        } else {
            System.out.println("Товар не найден в корзине");
        }
    }

    private void viewCart() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();

        Customer customer = manager.getCustomers().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        if (customer == null) {
            System.out.println("Ошибка: Покупатель не найден");
            return;
        }

        System.out.println(customer.viewCart());
    }

    private void checkout() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();

        System.out.println("\nДоступные пункты продаж:");
        manager.listSalesPoints();
        System.out.print("Введите ID пункта продаж: ");
        int salesPointId = scanner.nextInt();

        Customer customer = manager.getCustomers().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        SalesPoint salesPoint = manager.getSalesPoints().stream()
                .filter(sp -> sp.getId() == salesPointId)
                .findFirst()
                .orElse(null);

        if (customer == null || salesPoint == null) {
            System.out.println("Ошибка: Неверный ID покупателя или пункта продаж");
            return;
        }

        if (customer.getCart().isEmpty()) {
            System.out.println("Корзина пуста");
            return;
        }

        for (Product product : customer.getCart()) {
            manager.sellProduct(product.getId(), salesPointId, customerId);
        }
        customer.clearCart();
        System.out.println("Заказ оформлен");
    }

    private void viewPurchaseHistory() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();

        manager.printCustomerInfo(customerId);
    }
}