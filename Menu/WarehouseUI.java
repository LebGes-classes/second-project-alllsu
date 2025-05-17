package org.example;

import java.util.Scanner;

public class WarehouseUI {
    private final Manager manager;
    private final Scanner scanner;

    public WarehouseUI(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showWarehouseMenu() {
        while (true) {
            System.out.println("\n=== Управление складами ===");
            System.out.println("1. Добавить склад");
            System.out.println("2. Закрыть склад");
            System.out.println("3. Назначить ответственного");
            System.out.println("4. Просмотреть информацию о складе");
            System.out.println("5. Просмотреть товары на складе");
            System.out.println("6. Рассчитать стоимость товаров");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput();
            switch (choice) {
                case 1 -> addWarehouse();
                case 2 -> closeWarehouse();
                case 3 -> assignResponsible();
                case 4 -> showWarehouseInfo();
                case 5 -> listWarehouseProducts();
                case 6 -> calculateWarehouseValue();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addWarehouse() {
        System.out.print("Введите количество ячеек: ");
        int cellCount = readIntInput();
        int newId = manager.getWarehouses().size() + 1;
        manager.addWarehouse(cellCount, newId);
        System.out.println("Склад успешно добавлен с ID: " + newId);
    }

    private void closeWarehouse() {
        System.out.print("Введите ID склада: ");
        int id = readIntInput();
        manager.closeWarehouse(id);
    }

    private void assignResponsible() {
        manager.listEmployees();
        System.out.print("Введите ID склада: ");
        int warehouseId = readIntInput();
        System.out.print("Введите ID сотрудника: ");
        int employeeId = readIntInput();
        manager.assignResponsibleEmployee(warehouseId, employeeId);
    }

    private void showWarehouseInfo() {
        System.out.print("Введите ID склада: ");
        int id = readIntInput();
        manager.printWarehouseInfo(id);
    }

    private void listWarehouseProducts() {
        System.out.print("Введите ID склада: ");
        int id = readIntInput();
        manager.printWarehouseProducts(id);
    }

    private void calculateWarehouseValue() {
        System.out.print("Введите ID склада: ");
        int id = readIntInput();
        manager.printWarehouseValue(id);
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