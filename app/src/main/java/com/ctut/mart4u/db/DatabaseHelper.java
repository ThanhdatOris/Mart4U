package com.ctut.mart4u.db;

import android.content.Context;

import androidx.room.Room;

import com.ctut.mart4u.model.Category;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private final AppDatabase database;

    // Private constructor để ngăn việc tạo instance trực tiếp
    private DatabaseHelper(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "mart4u_database")
                .allowMainThreadQueries() // Chỉ dùng cho mục đích học tập, không khuyến khích trong production
                .fallbackToDestructiveMigration() // Xóa và tạo lại cơ sở dữ liệu khi version thay đổi
                .build();

        // Thêm dữ liệu mẫu cho bảng categories nếu bảng rỗng
        initializeSampleCategories();
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

    // Phương thức để lấy CategoryDao
    public CategoryDao getCategoryDao() {
        return database.categoryDao();
    }

    // Phương thức để thêm dữ liệu mẫu cho bảng categories
    private void initializeSampleCategories() {
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insert(new Category("Thực phẩm"));
            database.categoryDao().insert(new Category("Đồ uống"));
            database.categoryDao().insert(new Category("Đồ gia dụng"));
        }
    }
}