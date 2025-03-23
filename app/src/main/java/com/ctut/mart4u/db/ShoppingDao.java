package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ctut.mart4u.model.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingDao {
    @Insert
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item);

    @Delete
    void delete(ShoppingItem item);

    @Query("SELECT * FROM shopping_items")
    List<ShoppingItem> getAllItems();

    @Query("DELETE FROM shopping_items")
    void deleteAllItems();
}