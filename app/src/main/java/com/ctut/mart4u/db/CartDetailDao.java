package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.ctut.mart4u.model.CartDetail;
import java.util.List;

@Dao
public interface CartDetailDao {
    @Insert
    long insert(CartDetail cartDetail);

    @Update
    void update(CartDetail cartDetail);

    @Delete
    void delete(CartDetail cartDetail);

    @Query("SELECT * FROM cart_details WHERE userId = :userId")
    List<CartDetail> getCartDetailsByUser(int userId);

    @Query("DELETE FROM cart_details WHERE userId = :userId")
    void clearCart(int userId);
}