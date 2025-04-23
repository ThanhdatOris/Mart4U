package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.PurchaseDetailAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PurchaseDetailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_order_detail_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        RecyclerView recyclerViewPurchaseDetail = findViewById(R.id.rvOrderDetailItems);
        recyclerViewPurchaseDetail.setLayoutManager(new LinearLayoutManager(this));

        int purchaseId = getIntent().getIntExtra("purchaseId", -1);
        if (purchaseId == -1) {
            Toast.makeText(this, "Lỗi hệ thống, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);

            TextView tvOrderId = findViewById(R.id.tvOrderId);
            TextView tvOrderDate = findViewById(R.id.tvOrderDate);
            TextView tvOrderStatus = findViewById(R.id.tvOrderStatus);
            TextView tvOrderTotal = findViewById(R.id.tvOrderTotal);
            //lay thong tin tu co so du lieu
            Purchase purchase =  databaseHelper.getPurchaseDao().getPurchaseById(purchaseId);
            //set thong tin len giao dien
            tvOrderId.setText("Mã đơn hàng: " + purchaseId);
            try {
                long millis = Long.parseLong(purchase.getPurchaseDate());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String formattedDateTime = sdf.format(new Date(millis));
                tvOrderDate.setText("Ngày đặt: " + formattedDateTime);
            } catch (Exception e) {
                tvOrderDate.setText("Ngày đặt: " + purchase.getPurchaseDate());
            }
//            tvOrderDate.setText("Ngày đặt hàng: " + databaseHelper.getPurchaseDao().getPurchaseById(purchaseId).getPurchaseDate());
            //        // trạng thái
            if (databaseHelper.getPurchaseDao().getPurchaseById(purchaseId).getStatus().equalsIgnoreCase("completed")) {
                tvOrderStatus.setText("Trạng thái: Hoàn thành");
            } else {
                tvOrderStatus.setText("Trạng thái: Đang xử lý");
            }

//            tvOrderStatus.setText("Trạng thái đơn hàng: " + databaseHelper.getPurchaseDao().getPurchaseById(purchaseId).getStatus());
            tvOrderTotal.setText("Tổng tiền: " + databaseHelper.getPurchaseDao().getPurchaseById(purchaseId).getTotalAmount() + " VNĐ");



            // Lấy thông tin chi tiết đơn hàng từ cơ sở dữ liệu
            List<PurchaseDetail> purchaseDetails = databaseHelper.getPurchaseDetailDao().getPurchaseDetailsByPurchase(purchaseId);
//            Toast.makeText(this, "Lấy thông tin chi tiết đơn hàng thành công! Số lượng: " + purchaseDetails.size(), Toast.LENGTH_SHORT).show();
            // Tạo adapter và gán dữ liệu cho RecyclerView
            PurchaseDetailAdapter purchaseDetailAdapter = new PurchaseDetailAdapter(this, purchaseDetails);
            recyclerViewPurchaseDetail.setAdapter(purchaseDetailAdapter);
        }

    }
}