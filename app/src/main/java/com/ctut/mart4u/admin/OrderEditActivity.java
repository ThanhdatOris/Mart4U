package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderEditActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText etUserId, etPurchaseDate, etTotalAmount, etStatus;
    private Button btnSave, btnCancel, btnConfirm, btnCancelOrder, btnDelete;
    private TextView tvTitle, tvPurchaseDetailsTitle;
    private LinearLayout purchaseDetailsContainer;
    private Purchase purchase;
    private int purchaseId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_order_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ view
        tvTitle = findViewById(R.id.tvTitle);
        etUserId = findViewById(R.id.etUserId);
        etPurchaseDate = findViewById(R.id.etPurchaseDate);
        etTotalAmount = findViewById(R.id.etTotalAmount);
        etStatus = findViewById(R.id.etStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnDelete = findViewById(R.id.btnDelete);
        tvPurchaseDetailsTitle = findViewById(R.id.tvPurchaseDetailsTitle);
        purchaseDetailsContainer = findViewById(R.id.purchaseDetailsContainer);

        // Lấy purchaseId từ Intent
        purchaseId = getIntent().getIntExtra("purchaseId", -1);

        if (purchaseId != -1) {
            // Chỉnh sửa đơn hàng hiện có
            purchase = databaseHelper.getPurchaseDao().getPurchaseById(purchaseId);
            if (purchase == null) {
                Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            tvTitle.setText("Chi tiết đơn hàng");
            etUserId.setText(String.valueOf(purchase.getUserId()));
            etPurchaseDate.setText(purchase.getPurchaseDate());
            etTotalAmount.setText(String.valueOf(purchase.getTotalAmount()));
            etStatus.setText(purchase.getStatus());
            displayPurchaseDetails(purchaseId);
            tvPurchaseDetailsTitle.setVisibility(View.VISIBLE);
            purchaseDetailsContainer.setVisibility(View.VISIBLE);
        } else {
            // Thêm đơn hàng mới
            tvTitle.setText("Thêm đơn hàng mới");
            btnConfirm.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            tvPurchaseDetailsTitle.setVisibility(View.GONE);
            purchaseDetailsContainer.setVisibility(View.GONE);
            // Đặt giá trị mặc định
            etPurchaseDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            etStatus.setText("pending");
        }

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> saveOrder());

        // Xử lý nút Hủy (quay lại)
        btnCancel.setOnClickListener(v -> finish());

        // Xử lý nút Xác nhận đơn hàng
        btnConfirm.setOnClickListener(v -> {
            purchase.setStatus("completed");
            databaseHelper.getPurchaseDao().update(purchase);
            Toast.makeText(this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
            etStatus.setText("completed");
        });

        // Xử lý nút Hủy đơn hàng
        btnCancelOrder.setOnClickListener(v -> {
            purchase.setStatus("cancelled");
            databaseHelper.getPurchaseDao().update(purchase);
            Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
            etStatus.setText("cancelled");
        });

        // Xử lý nút Xóa đơn hàng
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa đơn hàng")
                    .setMessage("Bạn có chắc chắn muốn xóa đơn hàng này? Hành động này không thể hoàn tác.")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        databaseHelper.getPurchaseDao().delete(purchase);
                        Toast.makeText(this, "Xóa đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void saveOrder() {
        String userIdStr = etUserId.getText().toString().trim();
        String purchaseDate = etPurchaseDate.getText().toString().trim();
        String totalAmountStr = etTotalAmount.getText().toString().trim();
        String status = etStatus.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (userIdStr.isEmpty() || purchaseDate.isEmpty() || totalAmountStr.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId;
        double totalAmount;
        try {
            userId = Integer.parseInt(userIdStr);
            totalAmount = Double.parseDouble(totalAmountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "User ID và Tổng tiền phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (purchaseId != -1) {
            // Cập nhật đơn hàng
            purchase.setUserId(userId);
            purchase.setPurchaseDate(purchaseDate);
            purchase.setTotalAmount(totalAmount);
            purchase.setStatus(status);
            databaseHelper.getPurchaseDao().update(purchase);
            Toast.makeText(this, "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm đơn hàng mới
            Purchase newPurchase = new Purchase(userId, purchaseDate, totalAmount, status);
            databaseHelper.getPurchaseDao().insert(newPurchase);
            Toast.makeText(this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
        }

        // Quay lại OrderActivity
        Intent intent = new Intent(this, OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void displayPurchaseDetails(int purchaseId) {
        List<PurchaseDetail> details = databaseHelper.getPurchaseDetailDao().getPurchaseDetailsByPurchase(purchaseId);
        purchaseDetailsContainer.removeAllViews();

        if (details.isEmpty()) {
            TextView noDetailsText = new TextView(this);
            noDetailsText.setText("Không có chi tiết đơn hàng.");
            noDetailsText.setTextSize(16);
            noDetailsText.setTextColor(getResources().getColor(R.color.gray_600));
            noDetailsText.setPadding(0, 16, 0, 16);
            purchaseDetailsContainer.addView(noDetailsText);
            return;
        }

        for (PurchaseDetail detail : details) {
            TextView detailText = new TextView(this);
            String detailString = String.format(
                    "Sản phẩm ID: %d\nSố lượng: %d\nGiá: %.2f",
                    detail.getProductId(),
                    detail.getQuantity(),
                    detail.getUnitPrice()
            );
            detailText.setText(detailString);
            detailText.setTextSize(16);
            detailText.setTextColor(getResources().getColor(R.color.gray_800));
            detailText.setBackgroundResource(R.drawable.rounded_background);
            detailText.setPadding(16, 16, 16, 16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            detailText.setLayoutParams(params);
            purchaseDetailsContainer.addView(detailText);
        }
    }
}