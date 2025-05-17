package org.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

public class Manager {
    private final List<Warehouse> warehouses;
    private final List<SalesPoint> salesPoints;
    private final List<Employee> employees;
    private final List<Product> availableProducts;
    private final List<Customer> customers;
    private final List<ProductCell> cells;
    private final ExcelManager<Warehouse> warehouseExcelManager;
    private ExcelManager<SalesPoint> salesPointExcelManager;
    private final ExcelManager<Employee> employeeExcelManager;
    private ExcelManager<Product> productExcelManager;
    private ExcelManager<Customer> customerExcelManager;
    private final ExcelManager<ProductCell> productCellExcelManager;

    public Manager() {
        this.warehouses = new ArrayList<>();
        this.salesPoints = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.availableProducts = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.cells = new ArrayList<>();
        ExcelUtil excelUtil = new ExcelUtil();

        this.warehouseExcelManager = new ExcelManager<>(
                excelUtil,
                "Warehouses",
                new String[]{"ID", "Cell Count", "Responsible Employee ID"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell idCell = row.getCell(0);
                    Cell cellCountCell = row.getCell(1);
                    Cell employeeIdCell = row.getCell(2);
                    if (idCell != null && cellCountCell != null) {
                        int id = (int) idCell.getNumericCellValue();
                        int cellCount = (int) cellCountCell.getNumericCellValue();
                        int employeeId = employeeIdCell != null ? (int) employeeIdCell.getNumericCellValue() : -1;
                        Warehouse warehouse = Warehouse.open(cellCount);
                        warehouse.setId(id);
                        if (employeeId != -1) {
                            warehouse.assignResponsibleEmployee(employeeId);
                        }
                        return warehouse;
                    }
                    return null;
                },
                (warehouse, row) -> {
                    row.createCell(0).setCellValue(warehouse.getId());
                    row.createCell(1).setCellValue(warehouse.getCells().size());
                    row.createCell(2).setCellValue(warehouse.getResponsibleEmployeeId());
                },
                Warehouse::getId
        );

        this.salesPointExcelManager = new ExcelManager<>(
                excelUtil,
                "SalesPoints",
                new String[]{"ID", "Address", "Revenue"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell idCell = row.getCell(0);
                    Cell addressCell = row.getCell(1);
                    Cell revenueCell = row.getCell(2);
                    if (idCell != null && addressCell != null) {
                        int id = (int) idCell.getNumericCellValue();
                        String address = addressCell.getStringCellValue();
                        double revenue = revenueCell != null ? revenueCell.getNumericCellValue() : 0.0;
                        SalesPoint salesPoint = new SalesPoint(id, address, customers, productExcelManager, salesPointExcelManager, customerExcelManager);
                        salesPoint.addPurchase(revenue);
                        return salesPoint;
                    }
                    return null;
                },
                (salesPoint, row) -> {
                    row.createCell(0).setCellValue(salesPoint.getId());
                    row.createCell(1).setCellValue(salesPoint.getAddress());
                    row.createCell(2).setCellValue(salesPoint.getRevenue());
                },
                SalesPoint::getId
        );

        this.employeeExcelManager = new ExcelManager<>(
                excelUtil,
                "Employees",
                new String[]{"ID", "Name", "Role"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell roleCell = row.getCell(2);
                    if (idCell != null && nameCell != null && roleCell != null) {
                        int id = (int) idCell.getNumericCellValue();
                        String name = nameCell.getStringCellValue();
                        String role = roleCell.getStringCellValue();
                        Employee employee = Employee.hire(name, role);
                        employee.setId(id);
                        return employee;
                    }
                    return null;
                },
                (employee, row) -> {
                    row.createCell(0).setCellValue(employee.getId());
                    row.createCell(1).setCellValue(employee.getName());
                    row.createCell(2).setCellValue(employee.getRole());
                },
                Employee::getId
        );

        this.productExcelManager = new ExcelManager<>(
                excelUtil,
                "Products",
                new String[]{"ID", "Name", "Purchase Price", "Sale Price", "Status"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell purchasePriceCell = row.getCell(2);
                    Cell salePriceCell = row.getCell(3);
                    Cell statusCell = row.getCell(2);
                    if (idCell != null && nameCell != null && purchasePriceCell != null && salePriceCell != null) {
                        int id = (int) idCell.getNumericCellValue();
                        String name = nameCell.getStringCellValue();
                        double purchasePrice = purchasePriceCell.getNumericCellValue();
                        double salePrice = salePriceCell.getNumericCellValue();
                        String status = statusCell != null ? statusCell.getStringCellValue() : "В наличии";
                        Product product = Product.purchase(name, purchasePrice, salePrice);
                        product.setId(id);
                        product.setStatus(status);
                        return product;
                    }
                    return null;
                },
                (product, row) -> {
                    row.createCell(0).setCellValue(product.getId());
                    row.createCell(1).setCellValue(product.getName());
                    row.createCell(2).setCellValue(product.getPurchasePrice());
                    row.createCell(3).setCellValue(product.getSalePrice());
                    row.createCell(4).setCellValue(product.getStatus());
                },
                Product::getId
        );

        this.customerExcelManager = new ExcelManager<>(
                excelUtil,
                "Customers",
                new String[]{"ID", "Name", "Total Purchases"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell totalPurchasesCell = row.getCell(2);
                    if (idCell != null && nameCell != null) {
                        int id = (int) idCell.getNumericCellValue();
                        String name = nameCell.getStringCellValue();
                        double totalPurchases = totalPurchasesCell != null ? totalPurchasesCell.getNumericCellValue() : 0.0;
                        Customer customer = Customer.create(name);
                        customer.setId(id);
                        customer.addPurchase(totalPurchases);
                        return customer;
                    }
                    return null;
                },
                (customer, row) -> {
                    row.createCell(0).setCellValue(customer.getId());
                    row.createCell(1).setCellValue(customer.getName());
                    row.createCell(2).setCellValue(customer.getTotalPurchases());
                },
                Customer::getId
        );

        this.productCellExcelManager = new ExcelManager<>(
                excelUtil,
                "ProductCells",
                new String[]{"Warehouse ID", "Cell ID", "Product ID"},
                row -> {
                    if (row.getRowNum() == 0) return null;
                    Cell warehouseIdCell = row.getCell(0);
                    Cell cellIdCell = row.getCell(1);
                    Cell productIdCell = row.getCell(2);
                    if (warehouseIdCell != null && cellIdCell != null) {
                        int warehouseId = (int) warehouseIdCell.getNumericCellValue();
                        int cellId = (int) cellIdCell.getNumericCellValue();
                        int productId = productIdCell != null ? (int) productIdCell.getNumericCellValue() : -1;
                        ProductCell cell = new ProductCell(cellId, warehouseId);
                        if (productId != -1) {
                            cell.addProduct(productId);
                        }
                        return cell;
                    }
                    return null;
                },
                (cell, row) -> {
                    row.createCell(0).setCellValue(cell.getWarehouseId());
                    row.createCell(1).setCellValue(cell.getCellId());
                    row.createCell(2).setCellValue(cell.isFull() ? cell.getProductId() : -1);
                },
                ProductCell::getCellId
        );

        loadData();
    }

    private void loadData() {
        warehouseExcelManager.load(warehouses);
        productCellExcelManager.load(cells);
        salesPointExcelManager.load(salesPoints);
        employeeExcelManager.load(employees);
        productExcelManager.load(availableProducts);
        customerExcelManager.load(customers);
    }

    private void saveData() {
        warehouseExcelManager.save(warehouses);
        productCellExcelManager.save(cells);
        salesPointExcelManager.save(salesPoints);
        employeeExcelManager.save(employees);
        productExcelManager.save(availableProducts);
        customerExcelManager.save(customers);
    }

    public void getProductInfoById(int productId) {
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Ошибка: Продукт с ID " + productId + " не найден.");
            return;
        }
        System.out.printf("Продукт ID: %d, Название: %s, Цена закупки: %.2f, Цена продажи: %.2f, Статус: %s%n",
                product.getId(), product.getName(), product.getPurchasePrice(), product.getSalePrice(), product.getStatus());
    }

    public void addWarehouse(int cellCount, int id) {
        if (cellCount <= 0) {
            System.out.println("Ошибка: Количество ячеек должно быть положительным");
            return;
        }
        Warehouse warehouse = Warehouse.open(cellCount);
        warehouse.setId(id);
        warehouses.add(warehouse);
        for (ProductCell cell : warehouse.getCells()) {
            cells.add(cell);
        }
        saveData();
        System.out.println("Склад ID " + id + " создан");
    }

    public void closeWarehouse(int warehouseId) {
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse == null) {
            System.out.println("Ошибка: Склад с ID " + warehouseId + " не найден.");
            return;
        }
        if (!warehouse.close()) {
            return;
        }
        warehouseExcelManager.removeEntityById(warehouseId);
        cells.removeIf(cell -> cell.getWarehouseId() == warehouseId);
        warehouses.remove(warehouse);
        saveData();
        System.out.println("Склад ID: " + warehouseId + " закрыт.");
    }

    public void addSalesPoint(int id, String address) {
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Ошибка: Адрес не может быть пустым");
            return;
        }
        SalesPoint salesPoint = new SalesPoint(id, address, customers, productExcelManager, salesPointExcelManager, customerExcelManager);
        salesPoints.add(salesPoint);
        saveData();
        System.out.println("Пункт продаж ID " + id + " создан");
    }

    public void closeSalesPoint(int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        if (salesPoint == null) {
            System.out.println("Ошибка: Пункт продаж с ID " + salesPointId + " не найден.");
            return;
        }
        if (!salesPoint.close()) {
            return;
        }
        salesPointExcelManager.removeEntityById(salesPointId);
        salesPoints.remove(salesPoint);
        saveData();
        System.out.println("Пункт продаж ID: " + salesPointId + " закрыт.");
    }

    public void hireEmployee(String name, String role, int id) {
        if (name == null || name.trim().isEmpty() || role == null || role.trim().isEmpty()) {
            System.out.println("Ошибка: Имя и роль не могут быть пустыми");
            return;
        }
        Employee employee = Employee.hire(name, role);
        employee.setId(id);
        employees.add(employee);
        saveData();
        System.out.println("Сотрудник " + name + " нанят");
    }

    public void fireEmployee(int employeeId) {
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Ошибка: Сотрудник с ID " + employeeId + " не найден.");
            return;
        }
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getResponsibleEmployeeId() == employeeId) {
                System.out.println("Ошибка: Сотрудник является ответственным за склад ID: " + warehouse.getId());
                return;
            }
        }
        employeeExcelManager.removeEntityById(employeeId);
        employees.remove(employee);
        saveData();
        System.out.println("Сотрудник ID: " + employeeId + " уволен.");
    }

    public void purchaseProduct(String name, double purchasePrice, double salePrice) {
        if (name == null || name.trim().isEmpty() || purchasePrice < 0 || salePrice < 0) {
            System.out.println("Ошибка: Некорректные данные продукта");
            return;
        }
        Product product = Product.purchase(name, purchasePrice, salePrice);
        product.setId(availableProducts.size() + 1);
        availableProducts.add(product);
        saveData();
        System.out.println("Закуплен продукт: " + name + ", ID: " + product.getId());
    }

    public void moveProductToWarehouse(int productId, int warehouseId) {
        Product product = findProductById(productId);
        Warehouse warehouse = findWarehouseById(warehouseId);

        if (product == null || warehouse == null) {
            System.out.println("Ошибка: Продукт или склад не найдены");
            return;
        }

        if (warehouse.addProductToCell(productId)) {
            saveData();
            System.out.println("Продукт успешно перемещен на склад ID " + warehouseId);
        } else {
            System.out.println("Ошибка: Нет свободных ячеек на складе");
        }
    }

    public void moveProductToSalesPoint(int productId, int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        Product product = findProductById(productId);

        if (salesPoint == null || product == null) {
            System.out.println("Ошибка: Пункт продаж или продукт не найдены.");
            return;
        }

        for (ProductCell cell : cells) {
            if (cell.isFull() && cell.getProductId() == productId) {
                cell.removeProduct(productId);
                break;
            }
        }
        salesPoint.addProduct(product, availableProducts);
        saveData();
        System.out.println("Продукт ID: " + productId + " перемещен в пункт продаж ID: " + salesPointId);
    }

    public void assignResponsibleEmployee(int warehouseId, int employeeId) {
        Warehouse warehouse = findWarehouseById(warehouseId);
        Employee employee = findEmployeeById(employeeId);

        if (warehouse == null || employee == null) {
            System.out.println("Ошибка: Склад или сотрудник не найдены.");
            return;
        }
        warehouse.assignResponsibleEmployee(employeeId);
        warehouseExcelManager.updateEntity(warehouse);
        saveData();
        System.out.println("Сотрудник ID: " + employeeId + " назначен ответственным за склад ID: " + warehouseId);
    }

    public void sellProduct(int productId, int salesPointId, int customerId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        Customer customer = findCustomerById(customerId);

        if (salesPoint == null || customer == null) {
            System.out.println("Ошибка: Пункт продаж или клиент не найдены.");
            return;
        }

        if (salesPoint.sellProduct(productId, salesPointId, customerId)) {
            saveData();
            System.out.println("Продукт ID: " + productId + " продан клиенту ID: " + customerId +
                    " в пункте продаж ID: " + salesPointId);
        } else {
            System.out.println("Продажа не удалась: продукт не найден.");
        }
    }

    public void returnProduct(int productId, int salesPointId, int quantity) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        Product product = findProductById(productId);

        if (salesPoint == null || product == null) {
            System.out.println("Ошибка: Пункт продаж или продукт не найдены.");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Ошибка: Количество должно быть положительным.");
            return;
        }
        if (salesPoint.returnProduct(product, quantity)) {
            saveData();
            System.out.println("Продукт ID: " + productId + " возвращен в пункт продаж ID: " + salesPointId +
                    ", Количество: " + quantity);
        } else {
            System.out.println("Возврат не удался: некорректное количество.");
        }
    }

    public void addCustomer(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Ошибка: Имя клиента не может быть пустым.");
            return;
        }
        Customer customer = Customer.create(name);
        customer.setId(customers.size() + 1);
        customers.add(customer);
        saveData();
        System.out.println("Добавлен клиент: " + name + ", ID: " + customer.getId());
    }

    public void deleteCustomer(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Ошибка: Клиент с ID " + customerId + " не найден.");
            return;
        }
        customerExcelManager.removeEntityById(customerId);
        customer.clearCart();
        customers.remove(customer);
        saveData();
        System.out.println("Клиент ID: " + customerId + " удален.");
    }

    public void updateProductName(int productId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Ошибка: Название продукта не может быть пустым.");
            return;
        }
        Product product = findProductById(productId);
        if (product != null) {
            product.setName(newName);
            productExcelManager.updateFieldById(productId, 1, newName);
            saveData();
            System.out.println("Название продукта ID: " + productId + " изменено на: " + newName);
        } else {
            System.out.println("Ошибка: Продукт с ID " + productId + " не найден.");
        }
    }

    public void updateEmployeeRole(int employeeId, String newRole) {
        if (newRole == null || newRole.trim().isEmpty()) {
            System.out.println("Ошибка: Роль сотрудника не может быть пустой.");
            return;
        }
        Employee employee = findEmployeeById(employeeId);
        if (employee != null) {
            employee.setRole(newRole);
            employeeExcelManager.updateFieldById(employeeId, 2, newRole);
            saveData();
            System.out.println("Роль сотрудника ID: " + employeeId + " изменена на: " + newRole);
        } else {
            System.out.println("Ошибка: Сотрудник с ID " + employeeId + " не найден.");
        }
    }

    public void updateCustomerName(int customerId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Ошибка: Имя клиента не может быть пустым.");
            return;
        }
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            customer.setName(newName);
            customerExcelManager.updateFieldById(customerId, 1, newName);
            saveData();
            System.out.println("Имя клиента ID: " + customerId + " изменено на: " + newName);
        } else {
            System.out.println("Ошибка: Клиент с ID " + customerId + " не найден.");
        }
    }

    public void printWarehouseInfo(int warehouseId) {
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse != null) {
            System.out.println(warehouse.warehouseInfo(availableProducts, employees));
        } else {
            System.out.println("Ошибка: Склад с ID " + warehouseId + " не найден.");
        }
    }

    public void printSalesPointInfo(int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        if (salesPoint != null) {
            System.out.println("Информация о пункте выдачи:");
            System.out.println("ID: " + salesPoint.getId());
            System.out.println("Адрес: " + salesPoint.getAddress());
            System.out.println("Выручка: " + salesPoint.getRevenue());
        } else {
            System.out.println("Пункт выдачи с ID " + salesPointId + " не найден.");
        }
    }

    public void listSalesPoints() {
        if (salesPoints.isEmpty()) {
            System.out.println("Список пунктов продаж пуст.");
        } else {
            System.out.println("Список пунктов продаж:");
            for (int i = 0; i < salesPoints.size(); i++) {
                SalesPoint sp = salesPoints.get(i);
                System.out.printf("%d. ID: %d, Адрес: %s, Выручка: %.2f%n",
                        i + 1, sp.getId(), sp.getAddress(), sp.getRevenue());
            }
        }
    }

    public void printWarehouseProducts(int warehouseId) {
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse != null) {
            System.out.println("Товары на складе ID " + warehouseId + ":");
            System.out.println(warehouse.listProducts(availableProducts));
        } else {
            System.out.println("Склад не найден");
        }
    }

    public void printSalesPointProducts(int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        if (salesPoint != null) {
            System.out.println(salesPoint.listProducts());
        } else {
            System.out.println("Ошибка: Пункт продаж с ID " + salesPointId + " не найден.");
        }
    }

    public void printAvailableProducts() {
        System.out.println("Доступные продукты:");
        if (availableProducts.isEmpty()) {
            System.out.println("Нет доступных продуктов.");
        } else {
            for (Product p : availableProducts) {
                System.out.printf(" - ID: %d, Название: %s, Цена продажи: %.2f, Статус: %s%n",
                        p.getId(), p.getName(), p.getSalePrice(), p.getStatus());
            }
        }
    }

    public void printSalesPointRevenue(int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        if (salesPoint != null) {
            System.out.println(salesPoint.getRevenueInfo());
        } else {
            System.out.println("Ошибка: Пункт продаж с ID " + salesPointId + " не найден.");
        }
    }

    public void printCustomerInfo(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            System.out.printf("Клиент ID: %d, Имя: %s, Сумма покупок: %.2f%n",
                    customer.getId(), customer.getName(), customer.getTotalPurchases());
        } else {
            System.out.println("Ошибка: Клиент с ID " + customerId + " не найден.");
        }
    }

    public void printWarehouseValue(int warehouseId) {
        Warehouse warehouse = findWarehouseById(warehouseId);
        if (warehouse != null) {
            double value = warehouse.totalValue(availableProducts);
            System.out.printf("Общая стоимость инвентаря на складе ID %d: %.2f%n", warehouseId, value);
        } else {
            System.out.println("Ошибка: Склад с ID " + warehouseId + " не найден.");
        }
    }

    public void printSalesPointValue(int salesPointId) {
        SalesPoint salesPoint = findSalesPointById(salesPointId);
        if (salesPoint != null) {
            double value = salesPoint.totalValue();
            System.out.printf("Общая стоимость инвентаря в пункте продаж ID %d: %.2f%n", salesPointId, value);
        } else {
            System.out.println("Ошибка: Пункт продаж с ID " + salesPointId + " не найден.");
        }
    }

    public void listEmployees() {
        if (employees.isEmpty()) {
            System.out.println("Список сотрудников пуст.");
        } else {
            System.out.println("Список сотрудников:");
            for (int i = 0; i < employees.size(); i++) {
                Employee emp = employees.get(i);
                System.out.printf("%d. ID: %d, Имя: %s, Должность: %s%n",
                        i + 1, emp.getId(), emp.getName(), emp.getRole());
            }
        }
    }

    public void listCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Список покупателей пуст.");
        } else {
            System.out.println("Список покупателей:");
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                System.out.printf("%d. ID: %d, Имя: %s, Общая сумма покупок: %.2f%n",
                        i + 1, customer.getId(), customer.getName(), customer.getTotalPurchases());
            }
        }
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public List<Warehouse> getWarehouses() {
        return new ArrayList<>(warehouses);
    }

    public List<SalesPoint> getSalesPoints() {
        return new ArrayList<>(salesPoints);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    private Product findProductById(int productId) {
        return availableProducts.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);
    }

    private Warehouse findWarehouseById(int warehouseId) {
        return warehouses.stream()
                .filter(w -> w.getId() == warehouseId)
                .findFirst()
                .orElse(null);
    }

    private SalesPoint findSalesPointById(int salesPointId) {
        return salesPoints.stream()
                .filter(sp -> sp.getId() == salesPointId)
                .findFirst()
                .orElse(null);
    }

    private Employee findEmployeeById(int employeeId) {
        return employees.stream()
                .filter(e -> e.getId() == employeeId)
                .findFirst()
                .orElse(null);
    }

    private Customer findCustomerById(int customerId) {
        return customers.stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);
    }
}