package com.ctut.mart4u.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.ShoppingItem;

@Database(entities = {ShoppingItem.class, Category.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShoppingDao shoppingDao();
    public abstract CategoryDao categoryDao();
}