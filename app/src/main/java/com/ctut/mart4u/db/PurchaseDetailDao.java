package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.ctut.mart4u.model.PurchaseDetail;
import java.util.List;

@Dao
public interface PurchaseDetailDao {
    @Insert
    void insert(PurchaseDetail purchaseDetail);

    @Query("SELECT * FROM purchase_details WHERE purchaseId = :purchaseId")
    List<PurchaseDetail> getPurchaseDetailsByPurchase(int purchaseId);
}