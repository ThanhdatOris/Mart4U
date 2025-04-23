package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.adapter.CategoryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;

import java.util.List;

public class CategoryActivity extends AdminBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.admin_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

        RecyclerView recyclerViewCategory = findViewById(R.id.categoryRecyclerView);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));

        // Lấy thông tin danh mục từ database
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Category> categoryList = databaseHelper.getCategoryDao().getAllCategories();

        Toast.makeText(this, "Số lượng danh mục: " + categoryList.size(), Toast.LENGTH_SHORT).show();

        // Khởi tạo adapter ban đầu
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
        recyclerViewCategory.setAdapter(adapter);

        // Xử lý sự kiện thêm danh mục
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryEditActivity.class));
        });

        // Xử lý tìm kiếm
        EditText searchEditText = findViewById(R.id.searchEditText);
        ImageView searchIcon = findViewById(R.id.searchIcon);

        // Tìm kiếm khi nhấn Enter
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText, databaseHelper, recyclerViewCategory, categoryList);
                return true;
            }
            return false;
        });

        // Tìm kiếm khi nhấn icon
        searchIcon.setOnClickListener(v -> performSearch(searchEditText, databaseHelper, recyclerViewCategory, categoryList));
    }

    private void performSearch(EditText searchEditText, DatabaseHelper databaseHelper, RecyclerView recyclerView, List<Category> categoryList) {
        String query = searchEditText.getText().toString().trim();
        if (query.isEmpty()) {
            CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
            recyclerView.setAdapter(adapter);
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
        } else {
            List<Category> searchResults = databaseHelper.getCategoryDao().searchCategories(query);
            if (searchResults.isEmpty()) {
                searchResults = databaseHelper.getCategoryDao().getAllCategories();
                Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            }
            CategoryAdapter adapter = new CategoryAdapter(this, searchResults);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật danh sách khi quay lại
        RecyclerView recyclerViewCategory = findViewById(R.id.categoryRecyclerView);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Category> updatedCategoryList = databaseHelper.getCategoryDao().getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, updatedCategoryList);
        recyclerViewCategory.setAdapter(adapter);
    }
}