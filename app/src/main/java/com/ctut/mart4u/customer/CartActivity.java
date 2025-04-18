package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CartEntryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;

import java.util.List;

public class CartActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userId = getIntent().getIntExtra("user_id", -1);
        Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();

        RecyclerView recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        //lay du lieu tu database
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<CartDetail> cartList = databaseHelper.getCartDetailDao().getCartDetailsByUser(userId);

//        Toast.makeText(this, "Cart List Size: " + cartList.size(), Toast.LENGTH_SHORT).show();

        // su kien click vao nut xoa tat ca
        Button btnDeleteAll = findViewById(R.id.btnDeleteAllCart);
        btnDeleteAll.setOnClickListener(v -> {
            databaseHelper.getCartDetailDao().deleteAll(userId);
            cartList.clear();
            recyclerViewCart.getAdapter().notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa tất cả sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
        });


        //set adapter
        CartEntryAdapter adapter = new CartEntryAdapter(this, cartList);
        recyclerViewCart.setAdapter(adapter);

        EdgeToEdge.enable(this);

    }
}