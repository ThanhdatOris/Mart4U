package com.ctut.mart4u.admin;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.adapter.CategoryAdapter;
import com.ctut.mart4u.db.CategoryDao;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity {

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private CategoryDao categoryDao;

    private boolean isSampleData = true; // Chuyển thành false để dùng dữ liệu thật

    @Override
    protected int getLayoutId() {
        return R.layout.admin_category; // Trả về layout nội dung của CategoryActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_category);

        // Khởi tạo RecyclerView
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory); // Correct ID
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo DatabaseHelper và lấy CategoryDao
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        categoryDao = databaseHelper.getCategoryDao();

        // Khởi tạo danh sách và adapter
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, databaseHelper);
        recyclerViewCategory.setAdapter(categoryAdapter);

        // Load categories (sample or real)
        loadCategories();
    }

//    private void loadCategories() {
//        if (isSampleData) {
//            // Dữ liệu mẫu để test
//            categoryList.clear();
//            categoryList.add(new Category("Thịt"));
//            categoryList.add(new Category("Rau củ"));
//            categoryList.add(new Category("Trái cây"));
//        } else {
//            // Dữ liệu thực từ database
//            categoryList.clear();
//            categoryList.addAll(categoryDao.getAllCategories());
//        }
//
//        categoryAdapter.notifyDataSetChanged();
//    }
    private void loadCategories() {
        if (isSampleData) {
            // Dữ liệu mẫu để test
            categoryList.clear();
            categoryList.add(new Category("Thịt"));
            categoryList.add(new Category("Rau củ"));
            categoryList.add(new Category("Trái cây"));
        } else {
            // Dữ liệu thực từ database
            categoryList.clear();
            categoryList.addAll(categoryDao.getAllCategories());
        }
        categoryList.addAll(categoryDao.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
    }
}
