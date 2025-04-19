package com.ctut.mart4u.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.LoginActivity;
import com.ctut.mart4u.MainActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.utils.UserSession;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";
    private SharedPreferences sharedPreferences;


    @Override
    protected int getLayoutId() {
        return R.layout.customer_settings; // Trả về đúng layout
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Gọi sau khi layout được thiết lập bởi BaseActivity

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);


        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Log.e(TAG, "Invalid userId");
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Khởi tạo nút Back
        ImageButton btnBack = findViewById(R.id.btn_setting_back);
        if (btnBack == null) {
            Log.e(TAG, "Back button not found in layout");
            finish();
            return;
        }

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại trang trước (AccountActivity)
        });

        // Khởi tạo các view
        View llProfile = findViewById(R.id.ll_profile);
        View llChangePassword = findViewById(R.id.ll_change_password);
        View llDeleteAccount = findViewById(R.id.ll_delete_account);
        View llLogout = findViewById(R.id.ll_logout);

        // Kiểm tra null để tránh lỗi
        if (llProfile == null || llChangePassword == null || llDeleteAccount == null || llLogout == null) {
            Log.e(TAG, "One or more views not found in layout");
            Toast.makeText(this, "Lỗi giao diện, vui lòng thử lại", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Thông tin của tôi
        llProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileEditActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Đổi mật khẩu
        llChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Xóa tài khoản
        llDeleteAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, DeleteAccountActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // Đăng xuất
       llLogout.setOnClickListener(v -> {
            UserSession.getInstance().clearSession();
            sharedPreferences.edit().clear().apply();
            // updateUI();

           Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
           finishAffinity();
       });
    }
}