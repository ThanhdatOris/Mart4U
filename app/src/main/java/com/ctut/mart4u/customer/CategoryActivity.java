package com.ctut.mart4u.customer;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.GridLayoutManager;
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

        // Khởi tạo RecyclerView
        RecyclerView recyclerViewCategory = findViewById(R.id.recyclerViewCategory);

        // Sử dụng GridLayoutManager (dạng lưới với 3 cột)
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, 3));

        // Lấy dữ liệu từ cơ sở dữ liệu
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Category> sampleCategoryList = databaseHelper.getCategoryDao().getAllCategories();

        // Set adapter
        CategoryAdapter adapter = new CategoryAdapter(this, sampleCategoryList);
        recyclerViewCategory.setAdapter(adapter);
    }
}