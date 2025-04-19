package com.ctut.mart4u;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.utils.UserSession;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = DatabaseHelper.getInstance(this);
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Kiểm tra trạng thái đăng nhập
        if (sharedPreferences.getInt("userId", -1) != -1) {
            User user = databaseHelper.getUserDao().getUserByUsername(
                    sharedPreferences.getString("username", ""));
            if (user != null) {
                UserSession.getInstance().setCurrentUser(user);
                if ("customer".equals(user.getRole())) {
                    startActivity(new Intent(this, MainActivity.class));
                }
//                else {
//                    startActivity(new Intent(this, AdminActivity.class));
//                }
                finish();
                return;
            }
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = databaseHelper.getUserDao().login(username, password);
            if (user != null) {
                // Lưu vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userId", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("role", user.getRole());
                editor.apply();

                // Lưu vào UserSession
                UserSession.getInstance().setCurrentUser(user);

                if ("customer".equals(user.getRole())) {
                    startActivity(new Intent(this, MainActivity.class));
                }
//                else {
//                    startActivity(new Intent(this, AdminActivity.class));
//                }
                finish();
            } else {
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }
}