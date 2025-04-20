package com.ctut.mart4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.admin.DashboardActivity;
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
    private ImageView logo;

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
                } else if ("admin".equals(user.getRole())) {
                    startActivity(new Intent(this, DashboardActivity.class));
                }
                finish();
                return;
            }
        }

        logo = findViewById(R.id.logo);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        logo.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy user từ database dựa trên username
            User user = databaseHelper.getUserDao().getUserByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                // Mật khẩu khớp, tiến hành đăng nhập
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userId", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("role", user.getRole());
                editor.apply();

                // Lưu vào UserSession
                UserSession.getInstance().setCurrentUser(user);

                if ("customer".equals(user.getRole())) {
                    startActivity(new Intent(this, MainActivity.class));
                } else if ("admin".equals(user.getRole())) {
                    startActivity(new Intent(this, DashboardActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }
}