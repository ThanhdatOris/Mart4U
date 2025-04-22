package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.User;

import java.util.List;

public class CustomerDetailActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvUsername, tvEmail, tvPhoneNumber;
    private LinearLayout addressesContainer;
    private Button btnDeleteAccount;
    private User customer;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_customer_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ view
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        addressesContainer = findViewById(R.id.addressesContainer);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // Lấy userId từ Intent
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy khách hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin khách hàng
        customer = databaseHelper.getUserDao().getUserById(userId);
        if (customer == null) {
            Toast.makeText(this, "Không tìm thấy khách hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin khách hàng
        tvUsername.setText(customer.getUsername());
        tvEmail.setText(customer.getEmail());
        tvPhoneNumber.setText(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "Chưa có số điện thoại");

        // Hiển thị danh sách địa chỉ
        displayAddresses(userId);

        // Xử lý nút xóa tài khoản
        btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa tài khoản")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản của " + customer.getUsername() + "? Hành động này không thể hoàn tác.")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        deleteCustomerAccount();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void displayAddresses(int userId) {
        List<Address> addresses = databaseHelper.getAddressDao().getAddressesByUser(userId);
        addressesContainer.removeAllViews();

        if (addresses.isEmpty()) {
            TextView noAddressText = new TextView(this);
            noAddressText.setText("Khách hàng chưa có địa chỉ.");
            noAddressText.setTextSize(16);
            noAddressText.setTextColor(getResources().getColor(R.color.gray_600));
            noAddressText.setPadding(0, 16, 0, 16);
            addressesContainer.addView(noAddressText);
            return;
        }

        for (Address address : addresses) {
            TextView addressText = new TextView(this);
            String addressString = String.format(
                    "Người nhận: %s\nSố điện thoại: %s\nĐịa chỉ: %s\nPhương thức giao hàng: %s%s",
                    address.getReceiverName() != null ? address.getReceiverName() : "Không có",
                    address.getPhoneNumber() != null ? address.getPhoneNumber() : "Không có",
                    address.getAddress(),
                    address.getDeliveryMethod() != null ? address.getDeliveryMethod() : "Không có",
                    address.isDefault() ? "\n(Địa chỉ mặc định)" : ""
            );
            addressText.setText(addressString);
            addressText.setTextSize(16);
            addressText.setTextColor(getResources().getColor(R.color.gray_800));
            addressText.setBackgroundResource(R.drawable.rounded_background);
            addressText.setPadding(16, 16, 16, 16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            addressText.setLayoutParams(params);
            addressesContainer.addView(addressText);
        }
    }

    private void deleteCustomerAccount() {
        // Xóa khách hàng và các thông tin liên quan (Room sẽ tự động xóa các địa chỉ nhờ ForeignKey CASCADE)
        databaseHelper.getUserDao().delete(customer);
        Toast.makeText(this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();

        // Quay lại CustomerActivity
        Intent intent = new Intent(this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}