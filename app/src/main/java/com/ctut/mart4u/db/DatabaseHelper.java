package com.ctut.mart4u.db;

import android.content.Context;

import androidx.room.Room;

import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.model.DeliverySchedule;
import org.mindrot.jbcrypt.BCrypt;
import com.ctut.mart4u.R;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private final AppDatabase database;

    //Cách cũ
    // Private constructor để ngăn việc tạo instance trực tiếp
//    private DatabaseHelper(Context context) {
//        database = Room.databaseBuilder(context.getApplicationContext(),
//                        AppDatabase.class, "mart4u_database")
//                .allowMainThreadQueries() // Chỉ dùng cho mục đích học tập, không khuyến khích trong production
//                .fallbackToDestructiveMigration() // Xóa và tạo lại cơ sở dữ liệu khi version thay đổi
//                .build();
//
//        // Thêm dữ liệu mẫu nếu cơ sở dữ liệu rỗng
//        initializeSampleData();
//    }

    //Cách mới
    // Tham chiếu đến migrate tại appdatabase
    private DatabaseHelper(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "mart4u_database")
                .allowMainThreadQueries()
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5) // Thêm các migration
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_5_6)
                .build();

        initializeSampleData();
    }

    // Phương thức Singleton để lấy instance duy nhất
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    // Phương thức để lấy UserDao
    public UserDao getUserDao() {
        return database.userDao();
    }

    // Phương thức để lấy CategoryDao
    public CategoryDao getCategoryDao() {
        return database.categoryDao();
    }

    // Phương thức để lấy ProductDao
    public ProductDao getProductDao() {
        return database.productDao();
    }

    // Phương thức để lấy CartDetailDao
    public CartDetailDao getCartDetailDao() {
        return database.cartDetailDao();
    }

    // Phương thức để lấy PurchaseDao
    public PurchaseDao getPurchaseDao() {
        return database.purchaseDao();
    }

    // Phương thức để lấy PurchaseDetailDao
    public PurchaseDetailDao getPurchaseDetailDao() {
        return database.purchaseDetailDao();
    }

    // Phương thức để lấy AddressDao
    public AddressDao getAddressDao() {
        return database.addressDao();
    }

    // Phương thức để lấy DeliveryScheduleDao
    public DeliveryScheduleDao getDeliveryScheduleDao() {
        return database.deliveryScheduleDao();
    }

    // Phương thức để thêm dữ liệu mẫu cho các bảng
    private void initializeSampleData() {
        // Thêm dữ liệu mẫu cho bảng users nếu bảng rỗng
        if (database.userDao().getAllUsers().isEmpty()) { // Sửa từ getAllCustomers() thành getAllUsers()
            database.userDao().insert(new User(
                    "customer",
                    BCrypt.hashpw("123456", BCrypt.gensalt()),
                    "customer@gmail.com",
                    "customer",
                    "0816396742"
            ));
            database.userDao().insert(new User(
                    "sinoo",
                    BCrypt.hashpw("123456", BCrypt.gensalt()),
                    "anhkhoa@gmail.com",
                    "customer",
                    "0345517311"
            ));
            database.userDao().insert(new User(
                    "admin",
                    BCrypt.hashpw("123456", BCrypt.gensalt()),
                    "admin1@example.com",
                    "admin",
                    "0345517311"
            ));
        }

        if (database.addressDao().getAddressesByUser(1).isEmpty()) {
            database.addressDao().insert(new Address(1, "Thành Đạt", "0345517311", "123 Đường Láng, Hà Nội, Hà Nội, 100000, Vietnam", true, "COD"));
            database.addressDao().insert(new Address(1, "Thành Đạt", "0345517311", "456 Nguyễn Trãi, Hà Nội, Hà Nội, 100000, Vietnam", false, "Store Pickup"));
        }
        // Thêm dữ liệu mẫu cho bảng categories nếu bảng rỗng
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insert(new Category("Trái cây", "Trái cây tươi sống nhập khẩu Nhật, Mỹ, Hàn", R.drawable.ic_category_fruit));
            database.categoryDao().insert(new Category("Rau củ", "Rau củ các loại", R.drawable.ic_category_vegetable));
            database.categoryDao().insert(new Category("Trứng", "Trứng công nghiệp", R.drawable.ic_category_egg));
            database.categoryDao().insert(new Category("Thịt", "Các loại thịt tươi sống và đóng gói", R.drawable.ic_category_meat));
        }

        // Thêm dữ liệu mẫu cho bảng products nếu bảng rỗng
        if (database.productDao().getAllProducts().isEmpty()) {
            database.productDao().insert(new Product("Gạo ST25", "Gạo thơm ngon từ Việt Nam", 30000, 1, 100, "images/product_gaongucoc1.jpg"));
            database.productDao().insert(new Product("Gạo ABC", "Gạo thơm ngon từ Việt Nam", 30000, 1, 100, "images/product_gaongucoc2.jpg"));
            database.productDao().insert(new Product("Sữa tươi Vinamilk", "Sữa tươi 100% nguyên chất", 25000, 2, 200, "images/product_trungsua1.jpg"));
            database.productDao().insert(new Product("Nồi inox", "Nồi inox cao cấp", 150000, 3, 50, "images/product_trungsua2.jpg"));
        }

        // Thêm dữ liệu mẫu cho bảng delivery_schedule nếu bảng rỗng
        if (database.deliveryScheduleDao().getAllSchedules().isEmpty()) {
            database.deliveryScheduleDao().insert(new DeliverySchedule("9:00 - 11:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("11:00 - 13:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("13:00 - 15:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("15:00 - 18:00", "Mở", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("18:00 - 20:00", "Mở", "Mở", "Mở"));
        }
    }
}