package org.example;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private int id;
    private List<ProductCell> cells;
    private int responsibleEmployeeId;
    private static int nextId = 1;
    private ExcelManager excelManager = new ExcelManager();

    public Warehouse(int id, int cellCount) {
        if (cellCount <= 0) {
            throw new IllegalArgumentException("Количество ячеек должно быть положительным");
        }
        this.id = id;
        this.cells = new ArrayList<>(cellCount);
        for (int i = 0; i < cellCount; i++) {
            this.cells.add(new ProductCell(i + 1, id));
        }
        this.responsibleEmployeeId = -1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //Открытие склада
    public static Warehouse open(int cellCount) {
        return new Warehouse(nextId++, cellCount);
    }

    //Получение id
    public static void updateNextId(int id) {
        if (id >= nextId) nextId = id + 1;
    }

    //Закрытие склада
    public boolean close() {
        if(!isEmpty()) {
            System.out.println("Ошибка: Невозможно закрыть склад, так как на нем есть продукты");
            return false;
        }
        cells.clear();
        return true;
    }

    private boolean isEmpty() {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).isFull()){
                return false;
            }
        }
        return true;
    }

    public boolean addProductToCell(int productId) {
        for (ProductCell cell : cells) {
            if (!cell.isFull()) {
                if (cell.addProduct(productId)) {
                    if (excelManager != null) {
                        excelManager.changeCellValue("Cells", cell.getId(), 2, String.valueOf(productId));
                    }
                    return true;
                }
            }
        }
        return false;
    }
    //Ответственный сотрудник
    public void assignResponsibleEmployee(int employeeId) {
        this.responsibleEmployeeId = employeeId;
        if (excelManager != null) {
            excelManager.changeCellValue("Warehouses", this.id, 2, String.valueOf(employeeId));
        }
    }

    public String warehouseInfo(List<Product> availableProducts, List<Employee> employees) {
        String result = "Склад ID: " + getId() + "\n";
        result = result + "Количество ячеек: " + cells.size() + "\n";
        result = result + "Ответственный сотрудник: ";
        if (responsibleEmployeeId == -1) {
            result = result + "Отсутствует\n";
        } else {
            String employeeInfo = "ID " + responsibleEmployeeId;
            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                if (employee.getId() == responsibleEmployeeId) {
                    employeeInfo = employee.getName() + " (ID: " + responsibleEmployeeId + ")";
                    break;
                }
            }
            result = result + employeeInfo + "\n";
        }
        result = result + "Продукты:\n";
        result = result + listProducts(availableProducts);
        return result;
    }

    //Список всех товаров на складе
    public String listProducts(List<Product> allProducts) {
        StringBuilder result = new StringBuilder();
        boolean hasProducts = false;

        for (ProductCell cell : cells) {
            if (cell.isFull()) {
                Integer productId = cell.getProductId();
                Product product = findProductById(allProducts, productId);

                if (product != null) {
                    result.append(" - ")
                            .append(product.getName())
                            .append(" (ID: ").append(productId)
                            .append(", Цена: ").append(product.getSalePrice())
                            .append(")\n");
                    hasProducts = true;
                }
            }
        }

        if (!hasProducts) {
            result.append("На складе нет товаров\n");
        }

        return result.toString();
    }

    private Product findProductById(List<Product> products, Integer productId) {
        if (productId == null) return null;

        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public List<ProductCell> getCells() {
        return new ArrayList<>(cells);
    }

    public int getResponsibleEmployeeId() {
        return responsibleEmployeeId;
    }

    //Общая стоимость товаров на складе
    public double totalValue(List<Product> availableProducts) {
        double totalValue = 0.0;
        for (ProductCell cell : cells) {
            if (cell.isFull()) {
                Integer productId = cell.getProductId();
                for (Product product : availableProducts) {
                    if (product.getId() == productId) {
                        totalValue += product.getSalePrice();
                        break;
                    }
                }
            }
        }
        return totalValue;
    }
}