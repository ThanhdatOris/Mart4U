package com.ctut.mart4u.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.CartEntry;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;

@Database(entities = {User.class, Category.class, Product.class, CartEntry.class, Purchase.class, PurchaseDetail.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract CartEntryDao cartEntryDao();
    public abstract PurchaseDao purchaseDao();
    public abstract PurchaseDetailDao purchaseDetailDao();
}