package org.example;

import java.util.Scanner;

public class SalesPointUI {
    private final Manager manager;
    private final Scanner scanner;

    public SalesPointUI(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showSalesPointMenu() {
        while (true) {
            System.out.println("\n=== Управление пунктами выдачи ===");
            System.out.println("1. Открыть пункт выдачи");
            System.out.println("2. Закрыть пункт выдачи");
            System.out.println("3. Переместить товар в пункт");
            System.out.println("4. Просмотреть информацию о пункте");
            System.out.println("5. Просмотреть товары в пункте");
            System.out.println("6. Рассчитать стоимость товаров");
            System.out.println("7. Показать выручку пункта");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput();
            switch (choice) {
                case 1 -> addSalesPoint();
                case 2 -> closeSalesPoint();
                case 3 -> moveProductToPoint();
                case 4 -> showPointInfo();
                case 5 -> listPointProducts();
                case 6 -> calculatePointValue();
                case 7 -> showPointRevenue();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addSalesPoint() {
        System.out.print("Введите адрес: ");
        String address = scanner.nextLine();
        int newId = manager.getSalesPoints().size() + 1;
        manager.addSalesPoint(newId, address);
        System.out.println("Пункт выдачи успешно добавлен с ID: " + newId);
    }

    private void closeSalesPoint() {
        System.out.print("Введите ID пункта: ");
        int id = readIntInput();
        manager.closeSalesPoint(id);
    }

    private void moveProductToPoint() {
        manager.printAvailableProducts();
        System.out.print("Введите ID товара: ");
        int productId = readIntInput();
        System.out.print("Введите ID пункта: ");
        int pointId = readIntInput();
        manager.moveProductToSalesPoint(productId, pointId);
    }

    private void showPointInfo() {
        System.out.print("Введите ID пункта: ");
        int id = readIntInput();
        manager.printSalesPointInfo(id);
    }

    private void listPointProducts() {
        System.out.print("Введите ID пункта: ");
        int id = readIntInput();
        manager.printSalesPointProducts(id);
    }

    private void calculatePointValue() {
        System.out.print("Введите ID пункта: ");
        int id = readIntInput();
        manager.printSalesPointValue(id);
    }

    private void showPointRevenue() {
        System.out.print("Введите ID пункта: ");
        int id = readIntInput();
        manager.printSalesPointRevenue(id);
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
}