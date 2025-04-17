package com.ctut.mart4u.customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CategoryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;

import java.util.List;

public class CategoryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_activity_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo EdgeToEdge
        EdgeToEdge.enable(this);

        // Thêm ở trong phương thức onCreate()
        RecyclerView recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));

// Lấy dữ liệu từ cơ sở dữ liệu
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Category> sampleCategoryList = databaseHelper.getCategoryDao().getAllCategories();

// Set adapter
        CategoryAdapter adapter = new CategoryAdapter(this, sampleCategoryList);
        recyclerViewCategory.setAdapter(adapter);

    }


}
