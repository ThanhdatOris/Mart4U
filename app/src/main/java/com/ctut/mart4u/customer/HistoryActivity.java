package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.HistoryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Purchase;

import java.util.List;

public class HistoryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_orders_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.red));
        //=======================id nguoi dung mac dinh la 1
        int userId = getCurrentUserId();
        //Lấy danh sách đơn hàng từ cơ sở dữ liệu
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        //Lấy danh sách đơn hàng từ cơ sở dữ liệu
        List<Purchase> purchaseList = databaseHelper.getPurchaseDao().getPurchasesByUser(userId);
//        Toast.makeText(this, "Số lượng đơn hàng: " + purchaseList.size(), Toast.LENGTH_SHORT).show();

        //Lấy RecyclerView từ layout
        RecyclerView recyclerViewHistory = findViewById(R.id.rvOrderHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        //Tạo adapter cho RecyclerView
        HistoryAdapter adapter = new HistoryAdapter(this, purchaseList);
        //Gán adapter cho RecyclerView
        recyclerViewHistory.setAdapter(adapter);


    }
    // lấy người dùng đăng nhập\
    // protected int getCurrentUserId() {
    //     return getSharedPreferences("login_prefs", MODE_PRIVATE).getInt("userId", -1);
    // }

}