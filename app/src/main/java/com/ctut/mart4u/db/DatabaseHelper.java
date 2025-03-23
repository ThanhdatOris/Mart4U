package com.ctut.mart4u.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private final AppDatabase database;

    // Private constructor để ngăn việc tạo instance trực tiếp
    private DatabaseHelper(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "mart4u_database")
                .allowMainThreadQueries() // Chỉ dùng cho mục đích học tập, không khuyến khích trong production
                .build();
    }

    // Phương thức Singleton để lấy instance duy nhất
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    // Phương thức để lấy ShoppingDao
    public ShoppingDao getShoppingDao() {
        return database.shoppingDao();
    }
}