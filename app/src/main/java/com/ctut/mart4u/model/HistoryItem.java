package com.ctut.mart4u.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_items")
public class HistoryItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int quantity;
    private String purchaseDate;

    // Constructor
    public HistoryItem(String name, int quantity, String purchaseDate) {
        this.name = name;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}