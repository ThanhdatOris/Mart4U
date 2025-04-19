package com.ctut.mart4u.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

public class DeleteAccountActivity extends BaseActivity {
    private static final String TAG = "DeleteAccountActivity";
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private CheckBox cbAgree;
    private Button btnConfirmDelete, btnCancel;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_delete_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);
        int userId = getIntent().getIntExtra("userId", -1);
        currentUser = databaseHelper.getUserDao().getUserById(userId);
        if (currentUser == null) {
            finish();
            return;
        }

        // Khởi tạo nút Back
        ImageButton btnBack = findViewById(R.id.btn_delete_account_back);
        if (btnBack == null) {
            Log.e(TAG, "Back button not found in layout");
            finish();
            return;
        }

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại trang trước (AccountActivity)
        });

        // Khởi tạo view
        cbAgree = findViewById(R.id.cb_agree);
        btnConfirmDelete = findViewById(R.id.btn_confirm_delete);
        btnCancel = findViewById(R.id.btn_cancel);

        // Kích hoạt nút xác nhận khi đồng ý
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnConfirmDelete.setEnabled(isChecked);
        });

        // Xử lý xóa tài khoản
       btnConfirmDelete.setOnClickListener(v -> {
           try {
               // TODO: Xóa dữ liệu liên quan (giỏ hàng, đơn hàng, địa chỉ, v.v.)
               databaseHelper.getUserDao().delete(currentUser); // Room không có delete, cần thêm @Delete
               Toast.makeText(this, "Tài khoản đã được xóa", Toast.LENGTH_SHORT).show();
            //    Intent intent = new Intent(DeleteAccountActivity.this, LoginActivity.class);
            //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //    startActivity(intent);
               finishAffinity();
           } catch (Exception e) {
               Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
           }
       });

        // Hủy và thoát
        btnCancel.setOnClickListener(v -> finish());
    }
}