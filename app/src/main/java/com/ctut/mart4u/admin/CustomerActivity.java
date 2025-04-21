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
import com.ctut.mart4u.admin.adapter.CustomerAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.User;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewCustomers;
    private CustomerAdapter customerAdapter;
    private List<User> customerList;
    private EditText searchBar;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ view
        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
        searchBar = findViewById(R.id.searchBar);

        // Khởi tạo danh sách khách hàng
        customerList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(this, customerList, user -> {
            Intent intent = new Intent(CustomerActivity.this, CustomerDetailActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });

        recyclerViewCustomers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCustomers.setAdapter(customerAdapter);

        // Tải danh sách khách hàng
        loadCustomers();

        // Xử lý tìm kiếm
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCustomers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(databaseHelper.getUserDao().getAllCustomers());
        customerAdapter.notifyDataSetChanged();
    }

    private void filterCustomers(String query) {
        customerList.clear();
        List<User> allCustomers = databaseHelper.getUserDao().getAllCustomers();
        if (query.isEmpty()) {
            customerList.addAll(allCustomers);
        } else {
            String queryLower = query.toLowerCase();
            for (User customer : allCustomers) {
                if (customer.getUsername().toLowerCase().contains(queryLower) ||
                        customer.getEmail().toLowerCase().contains(queryLower) ||
                        (customer.getPhoneNumber() != null && customer.getPhoneNumber().contains(queryLower))) {
                    customerList.add(customer);
                }
            }
        }
        customerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers(); // Tải lại danh sách khi quay lại activity
    }
}