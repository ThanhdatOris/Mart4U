package com.ctut.mart4u.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.ctut.mart4u.R;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.CartDetail;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.DeliverySchedule;

@Database(entities = {User.class, Category.class, Product.class, CartDetail.class, Purchase.class, PurchaseDetail.class, Address.class, DeliverySchedule.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract CartDetailDao cartDetailDao();
    public abstract PurchaseDao purchaseDao();
    public abstract PurchaseDetailDao purchaseDetailDao();
    public abstract AddressDao addressDao();
    public abstract DeliveryScheduleDao deliveryScheduleDao();

    // Thêm migration
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Ví dụ: Thêm cột mới hoặc bảng mới
            // database.execSQL("ALTER TABLE delivery_schedule ADD COLUMN new_column TEXT");
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users ADD COLUMN phoneNumber TEXT");
            database.execSQL("ALTER TABLE addresses ADD COLUMN receiverName TEXT");
            database.execSQL("ALTER TABLE addresses ADD COLUMN phoneNumber TEXT");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE addresses ADD COLUMN deliveryMethod TEXT");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE products ADD COLUMN imagePath TEXT");
        }
    };
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE categories ADD COLUMN imageResourceId INTEGER NOT NULL DEFAULT 0");
        }
    };
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 1. Tạo bảng tạm thời với cột imagePath mới
            database.execSQL(
                    "CREATE TABLE categories_temp (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "imagePath TEXT)"
            );

            // 2. Chuyển dữ liệu từ bảng cũ sang bảng tạm, ánh xạ imageResourceId thành Uri
            database.execSQL(
                    "INSERT INTO categories_temp (id, name, description, imagePath) " +
                            "SELECT id, name, description, " +
                            "CASE " +
                            "WHEN imageResourceId = " + R.drawable.ic_category_fruit + " THEN 'android.resource://com.ctut.mart4u/" + R.drawable.ic_category_fruit + "' " +
                            "WHEN imageResourceId = " + R.drawable.ic_category_vegetable + " THEN 'android.resource://com.ctut.mart4u/" + R.drawable.ic_category_vegetable + "' " +
                            "WHEN imageResourceId = " + R.drawable.ic_category_egg + " THEN 'android.resource://com.ctut.mart4u/" + R.drawable.ic_category_egg + "' " +
                            "WHEN imageResourceId = " + R.drawable.ic_category_meat + " THEN 'android.resource://com.ctut.mart4u/" + R.drawable.ic_category_meat + "' " +
                            "ELSE NULL END " +
                            "FROM categories"
            );

            // 3. Xóa bảng cũ
            database.execSQL("DROP TABLE categories");

            // 4. Đổi tên bảng tạm thành bảng chính
            database.execSQL("ALTER TABLE categories_temp RENAME TO categories");
        }
    };
};