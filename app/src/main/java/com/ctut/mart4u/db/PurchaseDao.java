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

    @Query("SELECT * FROM purchases WHERE userId = :userId  ORDER BY purchaseDate DESC")
    List<Purchase> getPurchasesByUser(int userId);

    @Query("SELECT * FROM purchases WHERE id = :purchaseId ORDER BY purchaseDate DESC")
    Purchase getPurchaseById(int purchaseId);

    @Query("SELECT * FROM purchases ORDER BY purchaseDate DESC")
    List<Purchase> getAllPurchases();

    @Query("SELECT * FROM purchases WHERE userId LIKE '%' || :query || '%' OR purchaseDate LIKE '%' || :query || '%' OR status LIKE '%' || :query || '%' ORDER BY purchaseDate DESC")
    List<Purchase> searchPurchases(String query);

    // Thêm phương thức để đếm đơn hàng theo trạng thái và userId
    @Query("SELECT COUNT(*) FROM purchases WHERE userId = :userId AND status = :status ORDER BY purchaseDate DESC")
    int getOrderCountByStatus(int userId, String status);
}