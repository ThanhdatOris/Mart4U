package com.ctut.mart4u.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.LoginActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.utils.UserSession;

public class AccountActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;
    private LinearLayout layoutNotLoggedIn, layoutLoggedIn;
    private Button btnLogin;
    private TextView tvUsername, tvEmail;
    private ImageView avatar, icSettings;
    private View llSupport, profileContainer;
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Ánh xạ view
        layoutNotLoggedIn = findViewById(R.id.layoutNotLoggedIn);
        layoutLoggedIn = findViewById(R.id.layoutLoggedIn);
        btnLogin = findViewById(R.id.btnLogin);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        avatar = findViewById(R.id.avatar);
        icSettings = findViewById(R.id.ic_settings);
        llSupport = findViewById(R.id.ll_support);
        profileContainer = findViewById(R.id.profile_container);

        // Cập nhật giao diện dựa trên trạng thái đăng nhập
        updateUI();

        // Xử lý sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi đã đăng nhập
        if (UserSession.getInstance().isLoggedIn()) {
            // Chuyển đến trang chỉnh sửa thông tin khi nhấn vào tên
            profileContainer.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, ProfileEditActivity.class);
                intent.putExtra("userId", UserSession.getInstance().getCurrentUser().getId());
                startActivity(intent);
            });

            // Chuyển đến trang cài đặt khi nhấn bánh răng
            icSettings.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, SettingsActivity.class);
                intent.putExtra("userId", UserSession.getInstance().getCurrentUser().getId());
                startActivity(intent);
            });

            // Sự kiện nhấn Tổng đài/ email
            llSupport.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, SupportActivity.class);
                intent.putExtra("userId", UserSession.getInstance().getCurrentUser().getId());
                startActivity(intent);
            });
        }
    }

    private void updateUI() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Đã đăng nhập
            layoutLoggedIn.setVisibility(View.VISIBLE);
            layoutNotLoggedIn.setVisibility(View.GONE);
            tvUsername.setText(currentUser.getUsername());
            tvEmail.setText(currentUser.getEmail());
            // TODO: Load avatar nếu có
        } else {
            // Chưa đăng nhập
            layoutLoggedIn.setVisibility(View.GONE);
            layoutNotLoggedIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật giao diện khi quay lại Activity
        updateUI();
    }
}