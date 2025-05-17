package org.example;

import java.util.Scanner;

public class CustomerUI {
    private final Manager manager;
    private final Scanner scanner;

    public CustomerUI(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showCustomerMenu() {
        while (true) {
            System.out.println("\n=== Управление покупателями (Админ) ===");
            System.out.println("1. Добавить нового покупателя");
            System.out.println("2. Обновить имя покупателя");
            System.out.println("3. Удалить покупателя");
            System.out.println("4. Просмотреть информацию о покупателе");
            System.out.println("5. Список всех покупателей");
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addCustomer();
                case 2 -> updateCustomerName();
                case 3 -> deleteCustomer();
                case 4 -> viewCustomerInfo();
                case 5 -> listCustomers();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addCustomer() {
        System.out.print("Введите имя покупателя: ");
        String name = scanner.nextLine();
        manager.addCustomer(name);
    }

    private void updateCustomerName() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите новое имя: ");
        String newName = scanner.nextLine();
        manager.updateCustomerName(customerId, newName);
    }

    private void deleteCustomer() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();
        manager.deleteCustomer(customerId);
    }

    private void viewCustomerInfo() {
        System.out.println("\nСписок покупателей:");
        manager.listCustomers();
        System.out.print("Введите ID покупателя: ");
        int customerId = scanner.nextInt();
        manager.printCustomerInfo(customerId);
    }

    private void listCustomers() {
        manager.listCustomers();
    }
}