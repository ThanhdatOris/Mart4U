package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {
    private TextView textViewAppInfo;
    private Button btnClearAllData;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        textViewAppInfo = findViewById(R.id.textViewAppInfo);
        btnClearAllData = findViewById(R.id.btnClearAllData);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Xử lý sự kiện khi nhấn nút "Xóa toàn bộ dữ liệu"
        btnClearAllData.setOnClickListener(v -> {
            // Xóa tất cả dữ liệu trong các bảng
            databaseHelper.getShoppingDao().deleteAllItems();
            databaseHelper.getCategoryDao().deleteAllCategories();
            databaseHelper.getCartDao().deleteAllCartItems();
            databaseHelper.getHistoryDao().deleteAllHistoryItems();

            // Thông báo cho người dùng
            Toast.makeText(this, "Đã xóa toàn bộ dữ liệu", Toast.LENGTH_SHORT).show();
        });
    }
}