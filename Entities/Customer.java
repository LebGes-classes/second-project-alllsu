package org.example;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private double totalPurchases;
    private List<Product> cart;
    private static int nextId = 1;

    public Customer(int id, String name, double totalPurchases) {
        this.id = id;
        this.name = name;
        this.totalPurchases = totalPurchases;
        this.cart = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        updateNextId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalPurchases() {
        return totalPurchases;
    }

    public List<Product> getCart() {
        return new ArrayList<>(cart);
    }

    public static void updateNextId(int id) {
        if (id >= nextId) nextId = id + 1;
    }

    public static Customer create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        return new Customer(nextId++, name, 0.0);
    }

    public boolean addToCart(Product product) {
        if (product == null || !product.isAvailable()) {
            return false;
        }
        cart.add(product);
        return true;
    }

    public boolean removeFromCart(int productId) {
        return cart.removeIf(product -> product.getId() == productId);
    }

    public void clearCart() {
        cart.clear();
    }

    public double checkout() {
        return cart.stream()
                .filter(Product::isAvailable)
                .mapToDouble(Product::getSalePrice)
                .sum();
    }

    public void addPurchase(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма покупки не может быть отрицательной");
        }
        this.totalPurchases += amount;
    }

    public String viewCart() {
        if (cart.isEmpty()) {
            return "Корзина пуста";
        }
        StringBuilder result = new StringBuilder("Содержимое корзины:\n");
        for (Product product : cart) {
            result.append(String.format("- %s (ID: %d, Цена: %.2f)\n",
                    product.getName(), product.getId(), product.getSalePrice()));
        }
        result.append(String.format("Итого: %.2f", checkout()));
        return result.toString();
    }
}