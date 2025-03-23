package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private Button btnDeleteAllCart;
    private CartAdapter adapter;
    private List<CartItem> cartItemList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        btnDeleteAllCart = findViewById(R.id.btnDeleteAllCart);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(cartItemList, databaseHelper);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadCartItems();

        // Xử lý sự kiện khi nhấn nút Delete All
        btnDeleteAllCart.setOnClickListener(v -> {
            databaseHelper.getCartDao().deleteAllCartItems();
            loadCartItems(); // Làm mới danh sách
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu và cập nhật RecyclerView
    private void loadCartItems() {
        cartItemList.clear();
        cartItemList.addAll(databaseHelper.getCartDao().getAllCartItems());
        adapter.updateList(cartItemList);
    }
}