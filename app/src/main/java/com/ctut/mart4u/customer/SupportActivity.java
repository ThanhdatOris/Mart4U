package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.ImageButton;
import android.util.Log;

import androidx.activity.EdgeToEdge;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;

public class SupportActivity extends BaseActivity {
    private static final String TAG = "SupportActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.customer_support; // Layout mới
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Khởi tạo nút Back
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack == null) {
            Log.e(TAG, "Back button not found in layout");
            finish();
            return;
        }

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại trang trước (AccountActivity)
        });
    }
}