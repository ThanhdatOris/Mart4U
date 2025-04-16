package com.ctut.mart4u.customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CategoryAdapter;
import com.ctut.mart4u.model.Category;

import java.util.ArrayList;
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

// Dữ liệu mẫu
        List<Category> sampleCategoryList = new ArrayList<>();
        sampleCategoryList.add(new Category("Rau củ", "Rau củ tươi ngon"));
        sampleCategoryList.add(new Category("Thịt", "Thịt tươi ngon"));
        sampleCategoryList.add(new Category("Trứng", "Trứng tươi ngon"));

// Set adapter
        CategoryAdapter adapter = new CategoryAdapter(this, sampleCategoryList);
        recyclerViewCategory.setAdapter(adapter);

    }


}
