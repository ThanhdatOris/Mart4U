package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ctut.mart4u.model.CartItem;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllCartItems();

    @Query("DELETE FROM cart_items")
    void deleteAllCartItems();
}