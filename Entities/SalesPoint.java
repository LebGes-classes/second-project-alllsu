package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalesPoint {
    private int id;
    private List<Product> products;
    private double revenue;
    private String address;
    private static int nextId = 1;
    private transient ExcelManager<Product> productExcelManager;
    private transient ExcelManager<SalesPoint> salesPointExcelManager;
    private transient ExcelManager<Customer> customerExcelManager;
    private transient List<Customer> customers;

    public SalesPoint(int id, String address, List<Customer> customers,
                      ExcelManager<Product> productExcelManager,
                      ExcelManager<SalesPoint> salesPointExcelManager,
                      ExcelManager<Customer> customerExcelManager) {
        this.id = id;
        this.products = new ArrayList<>();
        this.address = Objects.requireNonNull(address, "Адрес не может быть null");
        this.revenue = 0.0;
        this.customers = customers != null ? customers : new ArrayList<>();
        this.productExcelManager = productExcelManager;
        this.salesPointExcelManager = salesPointExcelManager;
        this.customerExcelManager = customerExcelManager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        updateNextId(id);
    }

    public String getAddress() {
        return address;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public static void updateNextId(int id) {
        if (id >= nextId) nextId = id + 1;
    }

    public void addPurchase(double amount) {
        this.revenue += amount;
        if (salesPointExcelManager != null) {
            salesPointExcelManager.updateEntity(this);
        }
    }

    public static SalesPoint open(String address, List<Customer> customers,
                                  ExcelManager<Product> productExcelManager,
                                  ExcelManager<SalesPoint> salesPointExcelManager,
                                  ExcelManager<Customer> customerExcelManager) {
        return new SalesPoint(nextId++, address, customers, productExcelManager, salesPointExcelManager, customerExcelManager);
    }

    public boolean close() {
        if (!products.isEmpty()) {
            System.out.println("Нельзя закрыть пункт продаж: в нем есть продукты.");
            return false;
        }
        if (salesPointExcelManager != null) {
            salesPointExcelManager.removeEntityById(id);
        }
        return true;
    }

    public void addProduct(Product product, List<Product> availableProducts) {
        if (product != null && product.isAvailable()) {
            products.add(product);
            availableProducts.remove(product);
            product.setStatus("В пункте продаж");
            if (productExcelManager != null) {
                productExcelManager.updateEntity(product);
            }
        }
    }

    public boolean sellProduct(int productId, int salesPointId, int customerId) {
        Product product = products.stream()
                .filter(p -> p.getId() == productId && p.isAvailable())
                .findFirst()
                .orElse(null);

        Customer customer = customers.stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);

        if (product == null || customer == null) {
            return false;
        }

        double saleAmount = product.getSalePrice();
        product.setStatus("Продано");
        revenue += saleAmount;
        customer.addPurchase(saleAmount);
        products.remove(product);

        if (productExcelManager != null) {
            productExcelManager.removeEntityById(productId);
        }
        if (salesPointExcelManager != null) {
            salesPointExcelManager.updateEntity(this);
        }
        if (customerExcelManager != null) {
            customerExcelManager.updateEntity(customer);
        }

        return true;
    }

    public boolean returnProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }
        product.setStatus("В наличии");
        revenue -= product.getSalePrice() * quantity;
        products.add(product);
        if (productExcelManager != null) {
            productExcelManager.updateEntity(product);
        }
        if (salesPointExcelManager != null) {
            salesPointExcelManager.updateEntity(this);
        }
        return true;
    }

    public String listProducts() {
        if (products.isEmpty()) {
            return "В пункте продаж ID " + id + " нет продуктов.";
        }
        StringBuilder result = new StringBuilder("Продукты в пункте продаж ID " + id + ":\n");
        for (Product p : products) {
            result.append(String.format(" - %s (ID: %d, Цена: %.2f, Статус: %s)\n",
                    p.getName(), p.getId(), p.getSalePrice(), p.getStatus()));
        }
        return result.toString();
    }

    public String getRevenueInfo() {
        return String.format("Выручка пункта продаж ID %d: %.2f", id, revenue);
    }

    public double getRevenue() {
        return revenue;
    }

    public double totalValue() {
        return products.stream()
                .filter(Product::isAvailable)
                .mapToDouble(Product::getSalePrice)
                .sum();
    }
}