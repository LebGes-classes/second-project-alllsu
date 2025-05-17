package org.example;

public class Product  {
    private int id;
    private String name;
    private double purchasePrice;
    private double salePrice;
    private String status;
    private static int nextId = 1;

    public Product(int id, String name, double purchasePrice, double salePrice, String status) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.status = "В наличии";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Product purchase(String name, double purchasePrice, double salePrice) {
        return new Product(nextId++, name, purchasePrice, salePrice, "В наличии");
    }

    //Доступность товара
    public boolean isAvailable() {
        return "В наличии".equals(status);
    }

}