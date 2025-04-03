package com.ctut.mart4u.model;

public class ShoppingListItem {
    private String name;
    private int quantity;
    private double price;

    public ShoppingListItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}