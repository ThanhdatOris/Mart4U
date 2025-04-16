package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;
import com.ctut.mart4u.model.Product;

public class MainActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;
    private int userId = 1; // Giả định userId là 1, có thể lấy từ đăng nhập sau này

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Kiểm tra và thêm sản phẩm mẫu nếu chưa có
        initializeProducts();

        // Thêm sự kiện cho các sản phẩm
        LinearLayout product1 = findViewById(R.id.product1);
        LinearLayout product2 = findViewById(R.id.product2);
        LinearLayout product3 = findViewById(R.id.product3);

        product1.setOnClickListener(v -> addToCart(1)); // ID của Táo Envy Mỹ Túi 1kg
        product2.setOnClickListener(v -> addToCart(2)); // ID của Táo Juliet Pháp 1kg
        product3.setOnClickListener(v -> addToCart(3)); // ID của Táo
    }

    // Phương thức để kiểm tra và thêm sản phẩm mẫu
    private void initializeProducts() {
        // Kiểm tra xem sản phẩm đã tồn tại chưa, nếu chưa thì thêm
        if (databaseHelper.getProductDao().getProductById(1) == null) {
            databaseHelper.getProductDao().insert(new Product("Táo Envy Mỹ Túi 1kg", "Táo nhập khẩu từ Mỹ", 179000, 1, 50));
        }
        if (databaseHelper.getProductDao().getProductById(2) == null) {
            databaseHelper.getProductDao().insert(new Product("Táo Juliet Pháp 1kg", "Táo nhập khẩu từ Pháp", 109000, 1, 50));
        }
        if (databaseHelper.getProductDao().getProductById(3) == null) {
            databaseHelper.getProductDao().insert(new Product("Táo", "Táo không rõ nguồn gốc", 8000, 1, 100));
        }
    }

    // Phương thức để thêm sản phẩm vào giỏ hàng
    private void addToCart(int productId) {
        // Kiểm tra sản phẩm có tồn tại không
        Product product = databaseHelper.getProductDao().getProductById(productId);
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo CartDetail và thêm vào giỏ hàng
        CartDetail cartDetail = new CartDetail(userId, productId, 1); // Số lượng mặc định là 1
        databaseHelper.getCartDetailDao().insert(cartDetail);
        Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}