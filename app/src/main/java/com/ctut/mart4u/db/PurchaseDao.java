package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.ctut.mart4u.model.Purchase;
import java.util.List;

@Dao
public interface PurchaseDao {
    @Insert
    long insert(Purchase purchase);

    @Update
    void update(Purchase purchase);

    @Delete
    void delete(Purchase purchase);

    @Query("SELECT * FROM purchases WHERE userId = :userId")
    List<Purchase> getPurchasesByUser(int userId);

    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    Purchase getPurchaseById(int purchaseId);

    @Query("SELECT * FROM purchases")
    List<Purchase> getAllPurchases();

    @Query("SELECT * FROM purchases WHERE userId LIKE '%' || :query || '%' OR purchaseDate LIKE '%' || :query || '%' OR status LIKE '%' || :query || '%'")
    List<Purchase> searchPurchases(String query);
}