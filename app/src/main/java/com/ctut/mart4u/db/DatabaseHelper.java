package com.ctut.mart4u.db;

import android.content.Context;
import androidx.room.Room;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.model.DeliverySchedule;;

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
                .addMigrations(AppDatabase.MIGRATION_1_2)
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
        if (database.userDao().getAllCustomers().isEmpty()) {
            database.userDao().insert(new User("customer1", "pass123", "customer1@example.com", "customer"));
            database.userDao().insert(new User("admin1", "admin123", "admin1@example.com", "admin"));
        }

        // Thêm dữ liệu mẫu cho bảng categories nếu bảng rỗng
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insert(new Category("Thực phẩm", "Các loại thực phẩm tươi sống và đóng gói"));
            database.categoryDao().insert(new Category("Đồ uống", "Nước giải khát và đồ uống khác"));
            database.categoryDao().insert(new Category("Đồ gia dụng", "Đồ dùng trong gia đình"));
        }

        // Thêm dữ liệu mẫu cho bảng products nếu bảng rỗng
        if (database.productDao().getAllProducts().isEmpty()) {
            database.productDao().insert(new Product("Gạo ST25", "Gạo thơm ngon từ Việt Nam", 30000, 1, 100));
            database.productDao().insert(new Product("Sữa tươi Vinamilk", "Sữa tươi 100% nguyên chất", 25000, 2, 200));
            database.productDao().insert(new Product("Nồi inox", "Nồi inox cao cấp", 150000, 3, 50));
        }

        // Thêm dữ liệu mẫu cho bảng addresses nếu bảng rỗng
        if (database.addressDao().getAddressesByUser(1).isEmpty()) {
            // Thêm địa chỉ cho customer1 (userId = 1)
            database.addressDao().insert(new Address(1, "123 Đường Láng, Hà Nội, Hà Nội, 100000, Vietnam", true));
            database.addressDao().insert(new Address(1, "456 Nguyễn Trãi, Hà Nội, Hà Nội, 100000, Vietnam", false));
            // admin1 (userId = 2) không có địa chỉ
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