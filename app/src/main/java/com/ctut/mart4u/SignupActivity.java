package com.ctut.mart4u;


import android.content.Intent;
import android.os.Bundle;
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
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        logo = findViewById(R.id.logo);

        logo.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            password = BCrypt.hashpw(password, BCrypt.gensalt());

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.getUserDao().getUserByUsername(username) != null) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.getUserDao().getUserByEmail(email) != null) {
                Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (databaseHelper.getUserDao().getUserByPhoneNumber(phoneNumber) != null) {
                Toast.makeText(this, "Số điện thoại đã được sử dụng", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(username, password, email, "customer", phoneNumber);
            long userId = databaseHelper.getUserDao().insert(newUser);
            if (userId != -1) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}