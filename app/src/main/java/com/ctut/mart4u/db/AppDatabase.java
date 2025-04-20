package com.ctut.mart4u.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
}