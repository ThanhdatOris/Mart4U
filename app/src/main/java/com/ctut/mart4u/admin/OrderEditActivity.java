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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.adapter.OrderProductAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;
import com.ctut.mart4u.model.User;

import java.util.List;

public class OrderEditActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvOrderId, tvCustomerName, tvPhoneNumber, tvPurchaseDate, tvTotalAmount, tvStatus, tvCustomerAddress;
    private Button btnConfirm, btnCancelOrder, btnDelete;
    private TextView tvTitle, tvPurchaseDetailsTitle;
    private RecyclerView recyclerViewProducts;
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
        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPurchaseDate = findViewById(R.id.tvPurchaseDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvStatus = findViewById(R.id.tvStatus);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnDelete = findViewById(R.id.btnDelete);
        tvPurchaseDetailsTitle = findViewById(R.id.tvPurchaseDetailsTitle);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

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
            tvOrderId.setText("Mã đơn hàng: " + purchase.getId());
            tvPurchaseDate.setText("Ngày mua: " + purchase.getPurchaseDate());
            tvTotalAmount.setText("Tổng tiền: " + purchase.getTotalAmount());
            tvStatus.setText("Trạng thái: " + purchase.getStatus());

            // Hiển thị tên khách hàng và số điện thoại
            User user = databaseHelper.getUserDao().getUserById(purchase.getUserId());
            if (user != null) {
                tvCustomerName.setText("Tên khách hàng: " + user.getUsername());
                tvPhoneNumber.setText("Số điện thoại: " + user.getPhoneNumber());

                // Hiển thị địa chỉ khách hàng
                Address address = databaseHelper.getAddressDao().getDefaultAddress(user.getId());
                if (address != null) {
                    tvCustomerAddress.setText("Địa chỉ: " + address.getAddress());
                } else {
                    tvCustomerAddress.setText("Địa chỉ: Chưa có");
                }
            }

            // Hiển thị danh sách sản phẩm
            List<PurchaseDetail> details = databaseHelper.getPurchaseDetailDao().getPurchaseDetailsByPurchase(purchaseId);
            OrderProductAdapter adapter = new OrderProductAdapter(this, details);
            recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewProducts.setAdapter(adapter);

            tvPurchaseDetailsTitle.setVisibility(View.VISIBLE);
            recyclerViewProducts.setVisibility(View.VISIBLE);
        }

        // Xử lý nút Xác nhận đơn hàng
        btnConfirm.setOnClickListener(v -> {
            purchase.setStatus("completed");
            databaseHelper.getPurchaseDao().update(purchase);
            Toast.makeText(this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
            tvStatus.setText("Trạng thái: completed");
        });

        // Xử lý nút Hủy đơn hàng
        btnCancelOrder.setOnClickListener(v -> {
            purchase.setStatus("cancelled");
            databaseHelper.getPurchaseDao().update(purchase);
            Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
            tvStatus.setText("Trạng thái: cancelled");
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
}