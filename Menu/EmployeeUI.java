package org.example;

import java.util.Scanner;

public class EmployeeUI {
    private final Manager manager;
    private final Scanner scanner;

    public EmployeeUI(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void showEmployeeMenu() {
        while (true) {
            System.out.println("\n=== Управление сотрудниками ===");
            System.out.println("1. Нанять сотрудника");
            System.out.println("2. Уволить сотрудника");
            System.out.println("3. Изменить должность");
            System.out.println("4. Список сотрудников");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput();
            switch (choice) {
                case 1 -> hireEmployee();
                case 2 -> fireEmployee();
                case 3 -> updateEmployeeRole();
                case 4 -> listEmployees();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void hireEmployee() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите должность: ");
        String role = scanner.nextLine();
        int newId = manager.getEmployees().size() + 1;
        manager.hireEmployee(name, role, newId);
        System.out.println("Сотрудник успешно нанят с ID: " + newId);
    }

    private void fireEmployee() {
        manager.listEmployees();
        System.out.print("Введите ID сотрудника: ");
        int id = readIntInput();
        manager.fireEmployee(id);
    }

    private void updateEmployeeRole() {
        manager.listEmployees();
        System.out.print("Введите ID сотрудника: ");
        int id = readIntInput();
        System.out.print("Введите новую должность: ");
        String role = scanner.nextLine();
        manager.updateEmployeeRole(id, role);
        System.out.println("Должность успешно обновлена!");
    }

    private void listEmployees() {
        manager.listEmployees();
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