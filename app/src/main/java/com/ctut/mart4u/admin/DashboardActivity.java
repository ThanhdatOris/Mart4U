package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;

public class DashboardActivity extends AdminBaseActivity {
@Override
    protected int getLayoutId() {
        return R.layout.activity_dashboard;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tvAdminName = findViewById(R.id.tvAdminName);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // Cập nhật tên admin (có thể lấy từ SharedPreferences hoặc UserSession)
        tvAdminName.setText("Admin " + getCurrentAdminName());

        // Xử lý sự kiện nút "Bắt đầu" (chuyển đến trang quản lý danh mục, ví dụ)
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dashboard);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private String getCurrentAdminName() {
        // Thay bằng logic lấy tên admin từ UserSession hoặc SharedPreferences
        return "John Doe";
    }
}