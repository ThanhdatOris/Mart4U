package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.ctut.mart4u.db.DatabaseHelper;

public class MainActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Thêm sự kiện cho các sản phẩm
        LinearLayout product1 = findViewById(R.id.product1);
        LinearLayout product2 = findViewById(R.id.product2);
        LinearLayout product3 = findViewById(R.id.product3);

        product1.setOnClickListener(v -> addToCart("Táo Envy Mỹ Túi 1kg", 1, 179000));
        product2.setOnClickListener(v -> addToCart("Táo Juliet Pháp 1kg", 1, 109000));
        product3.setOnClickListener(v -> addToCart("Táo", 1, 8));
    }

    // Phương thức để thêm sản phẩm vào giỏ hàng
    private void addToCart(String name, int quantity, double price) {
        CartItem cartItem = new CartItem(name, quantity, price);
        databaseHelper.getCartDao().insert(cartItem);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}