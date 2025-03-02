package com.example.test1.entity;

import android.util.Log;

// Product.java
public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private String productDescription;
    private double unitPrice;
    private int unitQuantity;
    private boolean isFeatured;
    private int imageResId; // Local drawable resource ID
    private int sales; // Re-add sales count

    public Product(int productId, int categoryId, String productName, String productDescription, double unitPrice, int unitQuantity, boolean isFeatured, int imageResId, int sales) {
        Log.d("Product", "Creating Product with name: " + (productName != null ? productName : "null") + ", imageResId: " + imageResId);
        if (productName == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
        this.isFeatured = isFeatured;
        this.imageResId = imageResId;
        this.sales = sales;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getUnitQuantity() { return unitQuantity; }
    public void setUnitQuantity(int unitQuantity) { this.unitQuantity = unitQuantity; }
    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public int getSales() { return sales; }
    public void setSales(int sales) { this.sales = sales; }
}