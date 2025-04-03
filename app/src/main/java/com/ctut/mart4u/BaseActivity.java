package com.ctut.mart4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Khởi tạo Bottom Navigation Bar
        LinearLayout tabCategory = findViewById(R.id.tab_category);
        LinearLayout tabDelivery = findViewById(R.id.tab_delivery);
        LinearLayout tabLotteMart = findViewById(R.id.tab_lotte_mart);
        LinearLayout tabAccount = findViewById(R.id.tab_account);
        LinearLayout tabQuickBuy = findViewById(R.id.tab_quick_buy);

        // Xử lý sự kiện cho các tab
        tabCategory.setOnClickListener(v -> startActivity(new Intent(this, CategoryActivity.class)));
        tabDelivery.setOnClickListener(v -> startActivity(new Intent(this, DeliveryActivity.class)));
        tabLotteMart.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        tabAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        tabQuickBuy.setOnClickListener(v -> { /* Để rỗng vì không triển khai Mua Nhanh */ });
    }

    // Phương thức trừu tượng để các Activity con cung cấp layout
    protected abstract int getLayoutId();
}