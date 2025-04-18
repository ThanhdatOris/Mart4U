package com.ctut.mart4u.customer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileEditActivity extends BaseActivity {
    private static final String TAG = "ProfileEditActivity";
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private ImageView avatar;
    private EditText etUsername, etEmail, etPhone, etDob;
    private Spinner spinnerNationality, spinnerGender;
    private Button btnSave, btnChangeAvatar;
    private Calendar calendar;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_profile_edit; // Sửa thành đúng layout
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);
        int userId = getIntent().getIntExtra("userId", -1);
        currentUser = databaseHelper.getUserDao().getUserById(userId);
        if (currentUser == null) {
            Log.e(TAG, "User not found for userId: " + userId);
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo view
        avatar = findViewById(R.id.avatar);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        spinnerNationality = findViewById(R.id.spinner_nationality);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnSave = findViewById(R.id.btn_save);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);

        // Kiểm tra null cho các view
        if (avatar == null || etUsername == null || etEmail == null || etPhone == null ||
                etDob == null || spinnerNationality == null || spinnerGender == null ||
                btnSave == null || btnChangeAvatar == null) {
            Log.e(TAG, "One or more views not found in layout");
            Toast.makeText(this, "Lỗi giao diện, vui lòng thử lại", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Hiển thị thông tin
        etUsername.setText(currentUser.getUsername());
        etEmail.setText(currentUser.getEmail());
        etPhone.setText(currentUser.getPhoneNumber());
        // TODO: Load avatar và các thông tin khác nếu có (ví dụ: quốc tịch, giới tính, ngày sinh)

        // Thiết lập DatePickerDialog cho ngày sinh
        calendar = Calendar.getInstance();
        etDob.setKeyListener(null); // Ngăn nhập tay
        etDob.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etDob.setText(sdf.format(calendar.getTime()));
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Thiết lập spinner quốc tịch
        ArrayAdapter<CharSequence> nationalityAdapter = ArrayAdapter.createFromResource(
                this, R.array.nationalities, android.R.layout.simple_spinner_item);
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setAdapter(nationalityAdapter);

        // Thiết lập spinner giới tính
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.genders, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Xử lý chọn ảnh
        btnChangeAvatar.setOnClickListener(v -> {
            // TODO: Mở gallery để chọn ảnh
            Toast.makeText(this, "Chức năng chọn ảnh đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý lưu thông tin
        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String dob = etDob.getText().toString().trim();
            String nationality = spinnerNationality.getSelectedItem().toString();
            String gender = spinnerGender.getSelectedItem().toString();

            // Kiểm tra dữ liệu đầu vào
            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("\\d{10}")) {
                Toast.makeText(this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!dob.matches("\\d{2}/\\d{2}/\\d{4}")) {
                Toast.makeText(this, "Ngày sinh không đúng định dạng DD/MM/YYYY", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật thông tin người dùng
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phone);
            // TODO: Cập nhật ngày sinh, quốc tịch, giới tính nếu model User hỗ trợ
            // currentUser.setDob(dob);
            // currentUser.setNationality(nationality);
            // currentUser.setGender(gender);

            try {
                databaseHelper.getUserDao().insert(currentUser); // Room sẽ tự động update nếu ID đã tồn tại
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Error saving user: " + e.getMessage());
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}