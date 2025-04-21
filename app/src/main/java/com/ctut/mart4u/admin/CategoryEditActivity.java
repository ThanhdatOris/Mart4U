package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;
import com.google.android.material.textfield.TextInputEditText;

public class CategoryEditActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText etCategoryName, etCategoryDescription;
    private Button btnSaveCategory, btnCancel, btnDeleteCategory;
    private TextView titleTextView;
    private int categoryId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_category_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ view
        titleTextView = findViewById(R.id.titleTextView);
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        btnCancel = findViewById(R.id.btnCancel);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);

        // Lấy categoryId từ Intent
        categoryId = getIntent().getIntExtra("categoryId", -1);

        if (categoryId != -1) {
            // Chỉnh sửa danh mục hiện có
            Category category = databaseHelper.getCategoryDao().getCategoryById(categoryId);
            if (category != null) {
                etCategoryName.setText(category.getName());
                etCategoryDescription.setText(category.getDescription());
                titleTextView.setText("Chỉnh sửa danh mục");
            } else {
                Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Thêm danh mục mới
            titleTextView.setText("Thêm danh mục mới");
            btnDeleteCategory.setVisibility(View.GONE);
        }

        // Xử lý sự kiện nút Hủy
        btnCancel.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Lưu
        btnSaveCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString().trim();
            String description = etCategoryDescription.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (categoryId != -1) {
                // Cập nhật danh mục
                Category category = databaseHelper.getCategoryDao().getCategoryById(categoryId);
                category.setName(name);
                category.setDescription(description);
                databaseHelper.getCategoryDao().update(category);
                Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm danh mục mới
                Category newCategory = new Category(name, description, 0); // imageResourceId mặc định là 0
                databaseHelper.getCategoryDao().insert(newCategory);
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            }

            // Quay lại CategoryActivity
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nút Xóa (chỉ khi chỉnh sửa)
        if (categoryId != -1) {
            btnDeleteCategory.setOnClickListener(v -> {
                // Kiểm tra xem danh mục có sản phẩm liên kết không
                int productCount = databaseHelper.getProductDao().countProductsByCategoryId(categoryId);
                if (productCount > 0) {
                    Toast.makeText(this, "Không thể xóa danh mục vì có " + productCount + " sản phẩm liên kết.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Nếu không có sản phẩm liên kết, tiến hành xóa
                Category category = databaseHelper.getCategoryDao().getCategoryById(categoryId);
                databaseHelper.getCategoryDao().delete(category);
                Toast.makeText(this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CategoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}