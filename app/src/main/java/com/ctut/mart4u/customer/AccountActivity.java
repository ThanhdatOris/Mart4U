package com.ctut.mart4u.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

public class AccountActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private TextView tvUsername, tvEmail;
    private ImageView avatar, icSettings;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        databaseHelper = DatabaseHelper.getInstance(this);

        // Lấy thông tin người dùng (giả sử userId được truyền từ login)
        int userId = 1; // Thay bằng userId từ phiên đăng nhập
        currentUser = databaseHelper.getUserDao().getUserById(userId);
        if (currentUser == null) {
            finish();
            return;
        }

        // Khởi tạo view
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        avatar = findViewById(R.id.avatar);
        icSettings = findViewById(R.id.ic_settings);
        View profileContainer = findViewById(R.id.profile_container);

        // Hiển thị thông tin
        tvUsername.setText(currentUser.getUsername());
        tvEmail.setText(currentUser.getEmail());
        // TODO: Load avatar nếu có

        // Chuyển đến trang chỉnh sửa thông tin khi nhấn vào tên
        profileContainer.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ProfileEditActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Chuyển đến trang cài đặt khi nhấn bánh răng
        icSettings.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, SettingsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}