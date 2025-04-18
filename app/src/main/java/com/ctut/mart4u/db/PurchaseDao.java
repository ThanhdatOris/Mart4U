package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.ctut.mart4u.model.Purchase;
import java.util.List;

@Dao
public interface PurchaseDao {
    @Insert
    void insert(Purchase purchase);

    @Query("SELECT * FROM purchases WHERE userId = :userId")
    List<Purchase> getPurchasesByUser(int userId);

    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    Purchase getPurchaseById(int purchaseId);
}