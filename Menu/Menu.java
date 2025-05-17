package org.example;

import java.util.Scanner;

public class Menu {
    private final Manager manager;
    private final Scanner scanner;
    private final WarehouseUI warehouseUI;
    private final SalesPointUI salesPointUI;
    private final EmployeeUI employeeUI;
    private final ProductUI productUI;
    private final CustomerUI customerUI;
    private final CustomerMenu customerMenu;

    public Menu(Manager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
        this.warehouseUI = new WarehouseUI(manager, scanner);
        this.salesPointUI = new SalesPointUI(manager, scanner);
        this.employeeUI = new EmployeeUI(manager, scanner);
        this.productUI = new ProductUI(manager, scanner);
        this.customerUI = new CustomerUI(manager, scanner);
        this.customerMenu = new CustomerMenu(manager, scanner);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Добро пожаловать в магазин Ягодки! ===");
            System.out.println("Главное меню:");
            System.out.println("1. Управление складами");
            System.out.println("2. Управление пунктами выдачи");
            System.out.println("3. Управление сотрудниками");
            System.out.println("4. Управление товарами");
            System.out.println("5. Управление покупателями");
            System.out.println("6. Зайти от лица покупателя");
            System.out.println("0. Выход");
            System.out.print("Выберите раздел: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> warehouseUI.showWarehouseMenu();
                case 2 -> salesPointUI.showSalesPointMenu();
                case 3 -> employeeUI.showEmployeeMenu();
                case 4 -> productUI.showProductMenu();
                case 5 -> customerUI.showCustomerMenu();
                case 6 -> customerMenu.showCustomerMenu();
                case 0 -> {
                    System.out.println("Выход из программы...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }
}