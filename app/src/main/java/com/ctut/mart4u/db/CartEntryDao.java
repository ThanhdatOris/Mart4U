package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.ctut.mart4u.model.CartEntry;
import java.util.List;

@Dao
public interface CartEntryDao {
    @Insert
    void insert(CartEntry cartEntry);

    @Update
    void update(CartEntry cartEntry);

    @Delete
    void delete(CartEntry cartEntry);

    @Query("SELECT * FROM cart_entries WHERE userId = :userId")
    List<CartEntry> getCartEntriesByUser(int userId);

    @Query("DELETE FROM cart_entries WHERE userId = :userId")
    void clearCart(int userId);
}