package org.example;

public class ProductCell {
    private final int cellId;
    private final int warehouseId;
    private int productId;

    public ProductCell(int cellId, int warehouseId) {
        this.cellId = cellId;
        this.warehouseId = warehouseId;
        this.productId = -1;
    }

    public int getCellId() {
        return cellId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getProductId() {
        return productId;
    }

    public boolean isFull() {
        return productId != -1;
    }

    public boolean addProduct(int productId) {
        if (isFull()) {
            return false;
        }
        this.productId = productId;
        return true;
    }

    public boolean removeProduct(int productId) {
        if (!isFull() || this.productId != productId) {
            return false;
        }
        this.productId = -1;
        return true;
    }
}