package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.adapter.OrderAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Purchase;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Purchase> orderList;
    private EditText searchBar;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ view
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        searchBar = findViewById(R.id.searchBar);

        // Khởi tạo danh sách đơn hàng
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList, purchase -> {
            Intent intent = new Intent(OrderActivity.this, OrderEditActivity.class);
            intent.putExtra("purchaseId", purchase.getId());
            startActivity(intent);
        });

        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(orderAdapter);

        // Tải danh sách đơn hàng
        loadOrders();

        // Xử lý tìm kiếm
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadOrders() {
        orderList.clear();
        orderList.addAll(databaseHelper.getPurchaseDao().getAllPurchases());
        orderAdapter.notifyDataSetChanged();
    }

    private void filterOrders(String query) {
        orderList.clear();
        if (query.isEmpty()) {
            orderList.addAll(databaseHelper.getPurchaseDao().getAllPurchases());
        } else {
            orderList.addAll(databaseHelper.getPurchaseDao().searchPurchases(query));
        }
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders(); // Tải lại danh sách khi quay lại activity
    }
}