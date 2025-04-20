package com.ctut.mart4u;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

import org.mindrot.jbcrypt.BCrypt;

public class SignupActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText etUsername, etPassword, etEmail, etPhoneNumber;
    private Button btnSignup;
    private TextView tvLogin;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseHelper = DatabaseHelper.getInstance(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
        logo = findViewById(R.id.logo);

        logo.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            // Kiểm tra đầu vào
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra độ dài và độ phức tạp của mật khẩu
            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng số điện thoại (ví dụ: 10 số)
            if (!phoneNumber.matches("\\d{10}")) {
                Toast.makeText(this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hash mật khẩu
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Thực hiện kiểm tra và đăng ký trên thread riêng
            new Thread(() -> {
                // Kiểm tra trùng lặp username
                if (databaseHelper.getUserDao().getUserByUsername(username) != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Kiểm tra trùng lặp email
                if (databaseHelper.getUserDao().getUserByEmail(email) != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Kiểm tra trùng lặp số điện thoại
                if (databaseHelper.getUserDao().getUserByPhoneNumber(phoneNumber) != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Số điện thoại đã được sử dụng", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Tạo user mới
                User newUser = new User(username, hashedPassword, email, "customer", phoneNumber);
                long userId = databaseHelper.getUserDao().insert(newUser);

                // Cập nhật UI trên main thread
                runOnUiThread(() -> {
                    if (userId != -1) {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}