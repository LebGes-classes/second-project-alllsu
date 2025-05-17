package org.example;

import java.util.Scanner;

public class ProductUI {
    private final Manager manager;
    private final Scanner scanner;

    public ProductUI(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showProductMenu() {
        while (true) {
            System.out.println("\n=== Управление товарами ===");
            System.out.println("1. Закупить товар");
            System.out.println("2. Переместить на склад");
            System.out.println("3. Обновить информацию");
            System.out.println("4. Список доступных товаров");
            System.out.println("5. Просмотреть детали товара");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput();
            switch (choice) {
                case 1 -> purchaseProduct();
                case 2 -> moveToWarehouse();
                case 3 -> updateProductInfo();
                case 4 -> listAvailableProducts();
                case 5 -> getProductDetails();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void purchaseProduct() {
        System.out.print("Введите название: ");
        String name = scanner.nextLine();
        System.out.print("Введите цену закупки: ");
        double purchasePrice = readDoubleInput();
        System.out.print("Введите цену продажи: ");
        double salePrice = readDoubleInput();
        manager.purchaseProduct(name, purchasePrice, salePrice);
        System.out.println("Товар успешно закуплен!");
    }

    private void moveToWarehouse() {
        manager.printAvailableProducts();
        System.out.print("Введите ID товара: ");
        int productId = readIntInput();
        System.out.print("Введите ID склада: ");
        int warehouseId = readIntInput();
        manager.moveProductToWarehouse(productId, warehouseId);
    }

    private void updateProductInfo() {
        manager.printAvailableProducts();
        System.out.print("Введите ID товара: ");
        int productId = readIntInput();
        System.out.print("Введите новое название: ");
        String newName = scanner.nextLine();
        manager.updateProductName(productId, newName);
        System.out.println("Информация о товаре обновлена!");
    }

    private void listAvailableProducts() {
        manager.printAvailableProducts();
    }

    private void getProductDetails() {
        manager.printAvailableProducts();
        System.out.print("Введите ID товара: ");
        int productId = readIntInput();
        manager.getProductInfoById(productId);
    }

    private int readIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число: ");
            }
        }
    }

    private double readDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите число: ");
            }
        }
    }
}