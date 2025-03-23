package com.ctut.mart4u.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ctut.mart4u.model.CartItem;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.HistoryItem;
import com.ctut.mart4u.model.ShoppingItem;

@Database(entities = {ShoppingItem.class, Category.class, CartItem.class, HistoryItem.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShoppingDao shoppingDao();
    public abstract CategoryDao categoryDao();
    public abstract CartDao cartDao();
    public abstract HistoryDao historyDao();
}