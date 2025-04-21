package com.ctut.mart4u;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.customer.AccountActivity;
import com.ctut.mart4u.customer.AdressActivity;
import com.ctut.mart4u.customer.CartActivity;
import com.ctut.mart4u.customer.CategoryActivity;
import com.ctut.mart4u.customer.HistoryActivity;
import com.ctut.mart4u.customer.adapter.ProductAdapter;
import com.ctut.mart4u.db.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change the color of menu icons
        ImageView accountIcon = findViewById(R.id.ic_account);
        ImageView locationIcon = findViewById(R.id.ic_location);
        ImageView quickBuyIcon = findViewById(R.id.ic_cart);
        ImageView orderIcon = findViewById(R.id.ic_order);
        EditText etSearch = findViewById(R.id.etSearch);

        // Điều hướng đến trang tài khoản
        accountIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        // Điều hướng đến trang chọn địa chỉ
        locationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdressActivity.class);
            startActivity(intent);
        });

        // Điều hướng đến giỏ hàng
        quickBuyIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Điều hướng đến danh sách đơn hàng
        orderIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Set color filter (e.g., red color)
        int color = Color.parseColor("#FFFFFF"); // Replace with your desired color
        accountIcon.setColorFilter(color);
        locationIcon.setColorFilter(color);
        quickBuyIcon.setColorFilter(color);
        orderIcon.setColorFilter(color);
        // Initialize the database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Fetch products from the database
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProducts.setLayoutManager(layoutManager);


        // Fetch products from the database and set the adapter
        ProductAdapter adapter = new ProductAdapter(this, databaseHelper.getProductDao().getAllProducts());
        rvProducts.setAdapter(adapter);

        ImageView bannerImage = findViewById(R.id.bannerImage);
        try {
            InputStream inputStream = getAssets().open("images/banner.png"); // Đường dẫn tới ảnh trong thư mục assets
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            bannerImage.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set OnClickListener to navigate to CustomerActivityCategory
        bannerImage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString()); // Gọi phương thức lọc
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}