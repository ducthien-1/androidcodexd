package com.example.test1.entity;

public class CartItem {
    private Product product;
    private int quantity;
    private boolean isSelected;

    public CartItem(Product product, int quantity, boolean isSelected) {
        this.product = product;
        this.quantity = quantity;
        this.isSelected = isSelected;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
}