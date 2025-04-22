package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

import org.mindrot.jbcrypt.BCrypt;

public class ChangePasswordActivity extends BaseActivity {
    private static final String TAG = "ChangePasswordActivity";

    private DatabaseHelper databaseHelper;
    private User currentUser;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnSavePassword;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);
        int userId = getIntent().getIntExtra("userId", -1);
        currentUser = databaseHelper.getUserDao().getUserById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo nút Back
        ImageButton btnBack = findViewById(R.id.btn_change_password_back);
        if (btnBack == null) {
            Log.e(TAG, "Back button not found in layout");
            finish();
            return;
        }

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo view
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSavePassword = findViewById(R.id.btn_save_password);

        // Xử lý lưu mật khẩu
        btnSavePassword.setOnClickListener(v -> {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Kiểm tra các trường nhập liệu
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu hiện tại
            boolean isCurrentPasswordCorrect = false;
            String storedPassword = currentUser.getPassword();
            try {
                if (storedPassword != null && storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
                    // Mật khẩu đã được mã hóa bằng jBCrypt
                    isCurrentPasswordCorrect = BCrypt.checkpw(currentPassword, storedPassword);
                } else {
                    // Mật khẩu chưa được mã hóa (văn bản thô), so sánh trực tiếp
                    isCurrentPasswordCorrect = currentPassword.equals(storedPassword);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking password: " + e.getMessage());
                Toast.makeText(this, "Lỗi khi kiểm tra mật khẩu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            if (!isCurrentPasswordCorrect) {
                Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu xác nhận
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra độ dài mật khẩu mới
            if (newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mã hóa mật khẩu mới bằng jBCrypt
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            currentUser.setPassword(hashedNewPassword);

            // Cập nhật mật khẩu vào database
            try {
                databaseHelper.getUserDao().update(currentUser);
                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu mật khẩu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}