package com.ctut.mart4u.db;

import android.content.Context;
import android.net.Uri;

import androidx.room.Room;

import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;
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
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6)
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
        if (database.userDao().getAllUsers().isEmpty()) {
            database.userDao().insert(new User("customer", BCrypt.hashpw("123456", BCrypt.gensalt()), "customer@gmail.com", "customer", "0816396742"));
            database.userDao().insert(new User("sinoo", BCrypt.hashpw("123456", BCrypt.gensalt()), "anhkhoa@gmail.com", "customer", "0345517311"));
            database.userDao().insert(new User("admin", BCrypt.hashpw("123456", BCrypt.gensalt()), "admin1@example.com", "admin", "0345517311"));
            database.userDao().insert(new User("user1", BCrypt.hashpw("123456", BCrypt.gensalt()), "user1@example.com", "customer", "0909123456"));
            database.userDao().insert(new User("user2", BCrypt.hashpw("123456", BCrypt.gensalt()), "user2@example.com", "customer", "0918234567"));
        }

        // Thêm dữ liệu mẫu cho bảng addresses nếu bảng rỗng
        if (database.addressDao().getAddressesByUser(1).isEmpty()) {
            database.addressDao().insert(new Address(1, "Thành Đạt", "0345517311", "123 Đường Láng, Hà Nội, Hà Nội, 100000, Vietnam", true, "COD"));
            database.addressDao().insert(new Address(1, "Thành Đạt", "0345517311", "456 Nguyễn Trãi, Hà Nội, Hà Nội, 100000, Vietnam", false, "Store Pickup"));
            database.addressDao().insert(new Address(2, "Anh Khoa", "0345517311", "789 Lê Lợi, TP.HCM, TP.HCM, 700000, Vietnam", true, "COD"));
            database.addressDao().insert(new Address(3, "Admin", "0345517311", "101 Trần Phú, Đà Nẵng, Đà Nẵng, 550000, Vietnam", true, "Store Pickup"));
            database.addressDao().insert(new Address(4, "User 1", "0909123456", "202 Phạm Văn Đồng, Hà Nội, Hà Nội, 100000, Vietnam", true, "COD"));
        }

        // Thêm dữ liệu mẫu cho bảng categories nếu bảng rỗng
        if (database.categoryDao().getAllCategories().isEmpty()) {
            database.categoryDao().insert(new Category(
                    "Trái cây",
                    "Trái cây tươi sống nhập khẩu Nhật, Mỹ, Hàn",
                    Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_fruit).toString()
            ));
            database.categoryDao().insert(new Category(
                    "Rau củ",
                    "Rau củ các loại",
                    Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_vegetable).toString()
            ));
            database.categoryDao().insert(new Category(
                    "Trứng",
                    "Trứng công nghiệp",
                    Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_egg).toString()
            ));
            database.categoryDao().insert(new Category(
                    "Thịt",
                    "Các loại thịt tươi sống và đóng gói",
                    Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_meat).toString()
            ));
            database.categoryDao().insert(new Category("Đồ uống", "Nước giải khát và sữa", Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_drink).toString()));
            database.categoryDao().insert(new Category("Đồ gia dụng", "Dụng cụ gia đình", Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_category_household).toString()));
            database.categoryDao().insert(new Category("Gạo", "Gạo các loại", Uri.parse("android.resource://com.ctut.mart4u/" + R.drawable.ic_flag).toString()));
        }


        // Thêm dữ liệu mẫu cho bảng products nếu bảng rỗng
        if (database.productDao().getAllProducts().isEmpty()) {
            database.productDao().insert(new Product("Gạo ST25", "Gạo thơm ngon từ Việt Nam", 30000, 7, 100, "images/product_gaoST25.jpg", false));
            database.productDao().insert(new Product("Gạo ABC", "Gạo thơm ngon từ Việt Nam", 30000, 7, 100, "images/product_gaongucoc2.jpg", false));
            database.productDao().insert(new Product("Rau củ ", "Rau củ các loại ", 25000, 2, 200, "images/product_raucu1.jpg", false));
            database.productDao().insert(new Product("Trứng vịt ", "Trứng vịt đồng", 150000, 3, 50, "images/product_trungvit.jpg", false));
            database.productDao().insert(new Product("Táo Mỹ", "Táo nhập khẩu từ Mỹ", 60000, 1, 80, "images/product_tao2.jpg", false));
            database.productDao().insert(new Product("Cải xanh", "Cải xanh tươi", 15000, 2, 150, "images/product_caixanh.jpg", false));
            database.productDao().insert(new Product("Trứng gà", "Trứng gà công nghiệp", 3000, 3, 500, "images/product_trungga.jpg", false));
            database.productDao().insert(new Product("Thịt bò Wagyu", "Thịt bò Wagyu Nhât Bản", 200000, 4, 60, "images/product_thit2.jpg", false));
            database.productDao().insert(new Product("Sữa Vinamilk", "Sữa Vinamilk", 10000, 5, 120, "images/product_Vinamilk.jpg", false));
            database.productDao().insert(new Product("Thịt bò ", "Thịt bò tươi", 200000, 4, 60, "images/product_thitbo.jpg", false));
            database.productDao().insert(new Product("Nước cam", "Nước cam ép 100%", 35000, 5, 120, "images/product_nuoccam.jpg", false));
            database.productDao().insert(new Product("Chảo chống dính", "Chảo chống dính cao cấp", 120000, 6, 40, "images/product_chaochongdinh.jpg", false));
        }

        // Thêm dữ liệu mẫu cho bảng delivery_schedule nếu bảng rỗng
        if (database.deliveryScheduleDao().getAllSchedules().isEmpty()) {
            database.deliveryScheduleDao().insert(new DeliverySchedule("9:00 - 11:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("11:00 - 13:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("13:00 - 15:00", "Đóng", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("15:00 - 18:00", "Mở", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("18:00 - 20:00", "Mở", "Mở", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("8:00 - 9:00", "Mở", "Đóng", "Mở"));
            database.deliveryScheduleDao().insert(new DeliverySchedule("20:00 - 22:00", "Mở", "Mở", "Đóng"));
        }

//        // Thêm dữ liệu mẫu cho bảng cart_details nếu bảng rỗng
//        if (database.cartDetailDao().getCartDetailsByUser(1).isEmpty() &&
//                database.cartDetailDao().getCartDetailsByUser(2).isEmpty() &&
//                database.cartDetailDao().getCartDetailsByUser(3).isEmpty() &&
//                database.cartDetailDao().getCartDetailsByUser(4).isEmpty() &&
//                database.cartDetailDao().getCartDetailsByUser(5).isEmpty()) {
//            database.cartDetailDao().insert(new CartDetail(1, 1, 2)); // User 1, Product 1 (Gạo ST25), 2 units
//            database.cartDetailDao().insert(new CartDetail(1, 3, 1)); // User 1, Product 3 (Sữa Vinamilk), 1 unit
//            database.cartDetailDao().insert(new CartDetail(2, 2, 3)); // User 2, Product 2 (Gạo ABC), 3 units
//            database.cartDetailDao().insert(new CartDetail(3, 4, 1)); // User 3, Product 4 (Nồi inox), 1 unit
//            database.cartDetailDao().insert(new CartDetail(4, 5, 2)); // User 4, Product 5 (Táo Mỹ), 2 units
//            database.cartDetailDao().insert(new CartDetail(4, 7, 5)); // User 4, Product 7 (Trứng gà), 5 units
//            database.cartDetailDao().insert(new CartDetail(5, 8, 1)); // User 5, Product 8 (Thịt bò), 1 unit
//            database.cartDetailDao().insert(new CartDetail(5, 9, 3)); // User 5, Product 9 (Nước cam), 3 units
//        }
//        // Thêm dữ liệu mẫu cho bảng purchases nếu bảng rỗng
        if (database.purchaseDao().getAllPurchases().isEmpty()) {
            database.purchaseDao().insert(new Purchase(1, "2025-04-20", 85000, "completed"));  // User 1
            database.purchaseDao().insert(new Purchase(1, "2025-04-21", 30000, "pending"));    // User 1
            database.purchaseDao().insert(new Purchase(2, "2025-04-20", 90000, "completed"));  // User 2
            database.purchaseDao().insert(new Purchase(3, "2025-04-22", 150000, "completed")); // User 3
            database.purchaseDao().insert(new Purchase(4, "2025-04-22", 195000, "pending"));   // User 4
            database.purchaseDao().insert(new Purchase(5, "2025-04-23", 305000, "completed")); // User 5
        }
//        // Thêm dữ liệu mẫu cho bảng purchase_details nếu bảng rỗng
        if (database.purchaseDetailDao().getPurchaseDetailsByPurchase(1).isEmpty() &&
                database.purchaseDetailDao().getPurchaseDetailsByPurchase(2).isEmpty() &&
                database.purchaseDetailDao().getPurchaseDetailsByPurchase(3).isEmpty() &&
                database.purchaseDetailDao().getPurchaseDetailsByPurchase(4).isEmpty() &&
                database.purchaseDetailDao().getPurchaseDetailsByPurchase(5).isEmpty() ) {
            // Purchase 1 (User 1)
            database.purchaseDetailDao().insert(new PurchaseDetail(1, 1, 2, 30000)); // Product 1 (Gạo ST25), 2 units
            database.purchaseDetailDao().insert(new PurchaseDetail(1, 3, 1, 25000)); // Product 3 (Sữa Vinamilk), 1 unit
            // Purchase 2 (User 1)
            database.purchaseDetailDao().insert(new PurchaseDetail(2, 1, 1, 30000)); // Product 1 (Gạo ST25), 1 unit
            // Purchase 3 (User 2)
            database.purchaseDetailDao().insert(new PurchaseDetail(3, 2, 3, 30000)); // Product 2 (Gạo ABC), 3 units
            // Purchase 4 (User 3)
            database.purchaseDetailDao().insert(new PurchaseDetail(4, 4, 1, 150000)); // Product 4 (Nồi inox), 1 unit
            // Purchase 5 (User 4)
            database.purchaseDetailDao().insert(new PurchaseDetail(5, 5, 2, 60000)); // Product 5 (Táo Mỹ), 2 units
            database.purchaseDetailDao().insert(new PurchaseDetail(5, 7, 5, 3000));  // Product 7 (Trứng gà), 5 units
            // Purchase 6 (User 5)
            database.purchaseDetailDao().insert(new PurchaseDetail(6, 8, 1, 200000)); // Product 8 (Thịt bò), 1 unit
            database.purchaseDetailDao().insert(new PurchaseDetail(6, 9, 3, 35000));  // Product 9 (Nước cam), 3 units
            database.purchaseDetailDao().insert(new PurchaseDetail(6, 10, 1, 120000)); // Product 10 (Chảo chống dính), 1 unit
        }
    }
}