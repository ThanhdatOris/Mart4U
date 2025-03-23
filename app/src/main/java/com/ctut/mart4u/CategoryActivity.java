package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCategories;
    private EditText editTextCategoryName;
    private Button btnAddCategory;
    private Button btnDeleteAllCategories;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnDeleteAllCategories = findViewById(R.id.btnDeleteAllCategories);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryList, databaseHelper);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadCategories();

        // Xử lý sự kiện khi nhấn nút Add
        btnAddCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString().trim();

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo và thêm danh mục mới
            Category newCategory = new Category(categoryName);
            databaseHelper.getCategoryDao().insert(newCategory);
            editTextCategoryName.setText("");
            loadCategories(); // Làm mới danh sách
        });

        // Xử lý sự kiện khi nhấn nút Delete All
        btnDeleteAllCategories.setOnClickListener(v -> {
            databaseHelper.getCategoryDao().deleteAllCategories();
            loadCategories(); // Làm mới danh sách
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu và cập nhật RecyclerView
    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(databaseHelper.getCategoryDao().getAllCategories());
        adapter.updateList(categoryList);
    }
}