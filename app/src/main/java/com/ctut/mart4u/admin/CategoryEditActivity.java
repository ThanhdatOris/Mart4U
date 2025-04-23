package com.ctut.mart4u.admin;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;
import com.google.android.material.textfield.TextInputEditText;

public class CategoryEditActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText etCategoryName, etCategoryDescription;
    private Button btnSaveCategory, btnCancel, btnDeleteCategory, btnSelectImage;
    private ImageView categoryImage;
    private TextView titleTextView;
    private int categoryId = -1;
    private Uri currentPhotoUri; // Chỉ sử dụng Uri để lưu ảnh

    // Launcher để chọn ảnh từ thư viện
    private ActivityResultLauncher<Intent> pickImageLauncher;
    // Launcher để chụp ảnh từ camera
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_category_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Yêu cầu quyền CAMERA
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }

        // Ánh xạ view
        titleTextView = findViewById(R.id.titleTextView);
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        btnCancel = findViewById(R.id.btnCancel);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        categoryImage = findViewById(R.id.categoryImage);

        // Khởi tạo launcher chọn ảnh từ thư viện
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                currentPhotoUri = result.getData().getData();
                categoryImage.setImageURI(currentPhotoUri);
            }
        });

        // Khởi tạo launcher chụp ảnh từ camera
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                categoryImage.setImageURI(currentPhotoUri);
            }
        });

        // Xử lý sự kiện nút chọn ảnh
        btnSelectImage.setOnClickListener(v -> showImageSourceDialog());

        // Lấy categoryId từ Intent
        categoryId = getIntent().getIntExtra("categoryId", -1);

        if (categoryId != -1) {
            // Chỉnh sửa danh mục hiện có
            Category category = databaseHelper.getCategoryDao().getCategoryById(categoryId);
            if (category != null) {
                etCategoryName.setText(category.getName());
                etCategoryDescription.setText(category.getDescription());
                titleTextView.setText("Chỉnh sửa danh mục");
                // Hiển thị ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
                if (category.getImagePath() != null && !category.getImagePath().isEmpty()) {
                    currentPhotoUri = Uri.parse(category.getImagePath());
                    categoryImage.setImageURI(currentPhotoUri);
                } else {
                    categoryImage.setImageResource(R.drawable.ic_flag);
                }
            } else {
                Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Thêm danh mục mới
            titleTextView.setText("Thêm danh mục mới");
            btnDeleteCategory.setVisibility(View.GONE);
            categoryImage.setImageResource(R.drawable.ic_flag);
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

            String imagePath = currentPhotoUri != null ? currentPhotoUri.toString() : null;

            if (categoryId != -1) {
                // Cập nhật danh mục
                Category category = databaseHelper.getCategoryDao().getCategoryById(categoryId);
                category.setName(name);
                category.setDescription(description);
                category.setImagePath(imagePath);
                databaseHelper.getCategoryDao().update(category);
                Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm danh mục mới
                Category newCategory = new Category(name, description, imagePath);
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
                int productCount = databaseHelper.getProductDao().countProductsByCategoryId(categoryId);
                if (productCount > 0) {
                    Toast.makeText(this, "Không thể xóa danh mục vì có " + productCount + " sản phẩm liên kết.", Toast.LENGTH_LONG).show();
                    return;
                }

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

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA
        };
        ActivityCompat.requestPermissions(this, permissions, 100);
    }

    private void showImageSourceDialog() {
        boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if (hasCamera) {
            String[] options = {"Chụp ảnh", "Chọn từ thư viện"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn nguồn ảnh")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            dispatchTakePictureIntent();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickImageLauncher.launch(intent);
                        }
                    })
                    .show();
        } else {
            String[] options = {"Chọn từ thư viện"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn nguồn ảnh")
                    .setItems(options, (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickImageLauncher.launch(intent);
                    })
                    .show();
        }
    }

    private void dispatchTakePictureIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "category_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Mart4U");
        }

        currentPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
        takePictureLauncher.launch(takePictureIntent);
    }
}