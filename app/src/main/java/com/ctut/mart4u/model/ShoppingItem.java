package com.ctut.mart4u.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "shopping_items",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class ShoppingItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int quantity;
    private boolean isBought;

    private double price;

    @ColumnInfo(name = "category_id", index = true)
    private int CategoryId;
    // Constructor
    public ShoppingItem(String name, int quantity, boolean isBought) {
        this.name = name;
        this.quantity = quantity;
        this.isBought = isBought;
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

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public double getPrice() {return price;};

    public void setPrice(double price) { this.price = price;}

    public int getCategoryId() {
        return CategoryId;
    }
    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

}