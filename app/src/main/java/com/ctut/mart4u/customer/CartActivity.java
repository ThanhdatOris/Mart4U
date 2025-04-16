package com.ctut.mart4u.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CartAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {
    private RecyclerView recyclerViewCart;
    private Button btnDeleteAllCart;
    private Button btnCheckout;
    private TextView tvTotalPrice;
    private CartAdapter adapter;
    private List<CartItem> cartItemList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo các thành phần giao diện
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        btnDeleteAllCart = findViewById(R.id.btnDeleteAllCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(cartItemList, databaseHelper);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadCartItems();

        // Thêm dữ liệu mẫu nếu giỏ hàng rỗng
        if (cartItemList.isEmpty()) {
            addSampleData();
            loadCartItems();
        }

        // Cập nhật tổng giá trị
        updateTotalPrice();

        // Xử lý sự kiện khi nhấn nút Delete All
        btnDeleteAllCart.setOnClickListener(v -> {
            databaseHelper.getCartDao().deleteAllCartItems();
            loadCartItems(); // Làm mới danh sách
            updateTotalPrice(); // Cập nhật tổng giá trị
        });

        // Xử lý sự kiện khi nhấn nút Thanh toán
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("totalPrice", calculateTotalPrice());
            startActivity(intent);
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu và cập nhật RecyclerView
    private void loadCartItems() {
        cartItemList.clear();
        cartItemList.addAll(databaseHelper.getCartDao().getAllCartItems());
        adapter.updateList(cartItemList);
    }

    // Phương thức để thêm dữ liệu mẫu
    private void addSampleData() {
        databaseHelper.getCartDao().insert(new CartItem("Nước Tăng Lực RedBull Lon 250ml", 2, 11500));
        databaseHelper.getCartDao().insert(new CartItem("Táo Envy Mỹ Túi 1kg", 1, 179000));
    }

    // Phương thức để tính tổng giá trị
    private double calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // Phương thức để cập nhật tổng giá trị
    private void updateTotalPrice() {
        double total = calculateTotalPrice();
        tvTotalPrice.setText(String.format("%,.0fđ", total));
        btnCheckout.setText(String.format("Thanh toán %,dđ", (int) total));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.customer_cart;
    }
}