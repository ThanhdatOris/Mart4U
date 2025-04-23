package com.ctut.mart4u.admin;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductEditActivity extends AdminBaseActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText etProductName, etProductPrice, etProductDescription;
    private Spinner spinnerCategory;
    private Button btnSaveProduct, btnCancelEdit, btnDeleteProduct, btnChangeImage;
    private ImageView productImage;
    private TextView titleTextView;
    private int productId = -1;
    private Uri currentPhotoUri; // Lưu đường dẫn ảnh dưới dạng Uri tạm thời
    private List<Category> categoryList; // Danh sách danh mục
    private List<String> categoryNames; // Tên danh mục để hiển thị trong Spinner
    private List<Integer> categoryIds; // ID danh mục tương ứng

    // Launcher để chọn ảnh từ thư viện
    private ActivityResultLauncher<Intent> pickImageLauncher;
    // Launcher để chụp ảnh từ camera
    private ActivityResultLauncher<Intent> takePictureLauncher;

    private static final int REQUEST_PERMISSIONS_CODE = 100;
    private boolean hasShownPermissionDeniedMessage = false; // Tránh lặp lại thông báo

    @Override
    protected int getLayoutId() {
        return R.layout.admin_product_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Cập nhật imagePath cũ trong cơ sở dữ liệu
        migrateOldImagePaths();

        // Yêu cầu quyền CAMERA và READ_EXTERNAL_STORAGE
        requestPermissions();

        // Ánh xạ view
        titleTextView = findViewById(R.id.titleTextView);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        productImage = findViewById(R.id.productImage);

        // Đổi màu btnChangeImage
        btnChangeImage.setBackgroundColor(getResources().getColor(R.color.blue));
        btnSaveProduct.setBackgroundColor(getResources().getColor(R.color.blue));
        btnCancelEdit.setBackgroundColor(getResources().getColor(R.color.red));

        // Khởi tạo danh sách danh mục
        categoryList = databaseHelper.getCategoryDao().getAllCategories();
        Log.d("ProductEditActivity", "Số lượng danh mục: " + categoryList.size());
        categoryNames = new ArrayList<>();
        categoryIds = new ArrayList<>();
        categoryNames.add("Chọn danh mục");
        categoryIds.add(-1); // Giá trị mặc định khi không chọn danh mục
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
            categoryIds.add(category.getId());
            Log.d("ProductEditActivity", "Danh mục: " + category.getName() + ", ID: " + category.getId());
        }

        // Thiết lập adapter cho Spinner
        if (spinnerCategory != null) {
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);

            // Thêm OnItemSelectedListener để debug
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCategory = categoryNames.get(position);
                    int selectedCategoryId = categoryIds.get(position);
                    Log.d("ProductEditActivity", "Đã chọn danh mục: " + selectedCategory + ", ID: " + selectedCategoryId);
                    Toast.makeText(ProductEditActivity.this, "Đã chọn: " + selectedCategory, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("ProductEditActivity", "Không có danh mục nào được chọn");
                }
            });
        } else {
            Toast.makeText(this, "Không tìm thấy Spinner danh mục", Toast.LENGTH_SHORT).show();
        }

        // Khởi tạo launcher chọn ảnh từ thư viện
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                currentPhotoUri = result.getData().getData();
                Log.d("ProductEditActivity", "Ảnh được chọn từ thư viện: " + currentPhotoUri);
                if (currentPhotoUri != null) {
                    // Sao chép ảnh vào bộ nhớ nội bộ
                    String imagePath = copyImageToInternalStorage(currentPhotoUri);
                    if (imagePath != null) {
                        currentPhotoUri = Uri.fromFile(new File(imagePath));
                        // Sử dụng Glide để hiển thị ảnh
                        Glide.with(this)
                                .load(imagePath)
                                .placeholder(R.drawable.ic_flag)
                                .error(R.drawable.ic_flag)
                                .into(productImage);
                    } else {
                        Toast.makeText(this, "Không thể sao chép ảnh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("ProductEditActivity", "currentPhotoUri là null sau khi chọn ảnh từ thư viện");
                    Toast.makeText(this, "Không thể chọn ảnh từ thư viện", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("ProductEditActivity", "Không thể chọn ảnh từ thư viện: resultCode=" + result.getResultCode());
            }
        });

        // Khởi tạo launcher chụp ảnh từ camera
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Log.d("ProductEditActivity", "Ảnh chụp từ camera: " + currentPhotoUri);
                if (currentPhotoUri != null) {
                    // Sao chép ảnh vào bộ nhớ nội bộ
                    String imagePath = copyImageToInternalStorage(currentPhotoUri);
                    if (imagePath != null) {
                        currentPhotoUri = Uri.fromFile(new File(imagePath));
                        // Sử dụng Glide để hiển thị ảnh
                        Glide.with(this)
                                .load(imagePath)
                                .placeholder(R.drawable.ic_flag)
                                .error(R.drawable.ic_flag)
                                .into(productImage);
                    } else {
                        Toast.makeText(this, "Không thể sao chép ảnh chụp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("ProductEditActivity", "currentPhotoUri là null sau khi chụp ảnh");
                    Toast.makeText(this, "Không thể chụp ảnh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("ProductEditActivity", "Không thể chụp ảnh: resultCode=" + result.getResultCode());
            }
        });

        // Xử lý sự kiện nút thay đổi ảnh
        btnChangeImage.setOnClickListener(v -> showImageSourceDialog());

        // Lấy productId từ Intent
        productId = getIntent().getIntExtra("productId", -1);

        if (productId != -1) {
            // Chỉnh sửa sản phẩm hiện có
            Product product = databaseHelper.getProductDao().getProductById(productId);
            if (product != null) {
                etProductName.setText(product.getName());
                etProductPrice.setText(String.valueOf(product.getPrice()));
                etProductDescription.setText(product.getDescription());
                titleTextView.setText("Chỉnh sửa sản phẩm");

                // Thiết lập danh mục được chọn
                if (spinnerCategory != null) {
                    int categoryPosition = categoryIds.indexOf(product.getCategoryId());
                    if (categoryPosition != -1) {
                        spinnerCategory.setSelection(categoryPosition);
                    } else {
                        Log.d("ProductEditActivity", "Không tìm thấy categoryId: " + product.getCategoryId() + " trong danh sách categoryIds");
                        spinnerCategory.setSelection(0); // Chọn "Chọn danh mục" nếu không tìm thấy
                    }
                }

                // Hiển thị ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
                if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
                    Log.d("ProductEditActivity", "Tải ảnh từ imagePath: " + product.getImagePath());
                    // Sử dụng Glide để hiển thị ảnh
                    Glide.with(this)
                            .load(product.getImagePath())
                            .placeholder(R.drawable.ic_flag)
                            .error(R.drawable.ic_flag)
                            .into(productImage);
                } else {
                    Log.d("ProductEditActivity", "imagePath là null hoặc rỗng cho productId: " + productId);
                    productImage.setImageResource(R.drawable.ic_flag); // Ảnh mặc định
                }
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Thêm sản phẩm mới
            titleTextView.setText("Thêm sản phẩm mới");
            btnDeleteProduct.setVisibility(View.GONE);
            btnChangeImage.setText("Thêm ảnh sản phẩm");
            productImage.setImageResource(R.drawable.ic_flag);
        }

        // Xử lý sự kiện nút Hủy
        btnCancelEdit.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Lưu
        btnSaveProduct.setOnClickListener(v -> {
            String name = etProductName.getText().toString().trim();
            String priceStr = etProductPrice.getText().toString().trim();
            String description = etProductDescription.getText().toString().trim();
            int selectedCategoryPosition = spinnerCategory != null ? spinnerCategory.getSelectedItemPosition() : -1;
            int categoryId = selectedCategoryPosition != -1 ? categoryIds.get(selectedCategoryPosition) : -1;
            Log.d("ProductEditActivity", "Vị trí danh mục được chọn: " + selectedCategoryPosition + ", CategoryId: " + categoryId);

            // Kiểm tra thông tin bắt buộc
            if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (categoryId == -1) {
                Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String imagePath = currentPhotoUri != null ? currentPhotoUri.getPath() : null; // Lưu đường dẫn file thực tế
            Log.d("ProductEditActivity", "Lưu imagePath: " + imagePath);

            if (productId != -1) {
                // Cập nhật sản phẩm
                Product product = databaseHelper.getProductDao().getProductById(productId);
                product.setName(name);
                product.setPrice(price);
                product.setDescription(description);
                product.setCategoryId(categoryId);
                product.setImagePath(imagePath);
                databaseHelper.getProductDao().update(product);
                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm sản phẩm mới
                Product newProduct = new Product(name, description, price, categoryId, 0, imagePath, false);
                databaseHelper.getProductDao().insert(newProduct);
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }

            // Quay lại ProductActivity
            Intent intent = new Intent(this, ProductActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nút Xóa (chỉ khi chỉnh sửa)
        if (productId != -1) {
            btnDeleteProduct.setOnClickListener(v -> {
                Product product = databaseHelper.getProductDao().getProductById(productId);
                product.setDeleted(true);
                databaseHelper.getProductDao().update(product);
                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    // Hàm sao chép ảnh từ Uri vào bộ nhớ nội bộ
    private String copyImageToInternalStorage(Uri uri) {
        try {
            File directory = new File(getFilesDir(), "product_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "product_" + System.currentTimeMillis() + ".jpg");
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            Log.e("ProductEditActivity", "Lỗi khi sao chép ảnh: " + e.getMessage());
            return null;
        }
    }

    // Cập nhật imagePath cũ trong cơ sở dữ liệu
    private void migrateOldImagePaths() {
        List<Product> products = databaseHelper.getProductDao().getAllProducts();
        for (Product product : products) {
            String imagePath = product.getImagePath();
            if (imagePath != null && imagePath.startsWith("content://")) {
                try {
                    Uri uri = Uri.parse(imagePath);
                    String newImagePath = copyImageToInternalStorage(uri);
                    if (newImagePath != null) {
                        product.setImagePath(newImagePath);
                        databaseHelper.getProductDao().update(product);
                        Log.d("ProductEditActivity", "Đã cập nhật imagePath cho productId=" + product.getId() + ": " + newImagePath);
                    } else {
                        Log.d("ProductEditActivity", "Không thể sao chép ảnh cho productId=" + product.getId());
                    }
                } catch (Exception e) {
                    Log.e("ProductEditActivity", "Lỗi khi cập nhật imagePath cho productId=" + product.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    private void requestPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        // Yêu cầu quyền CAMERA
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }

        // Yêu cầu quyền READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("ProductEditActivity", "Quyền bị từ chối: " + permissions[i]);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        // Quyền bị từ chối vĩnh viễn
                        if (!hasShownPermissionDeniedMessage) {
                            hasShownPermissionDeniedMessage = true;
                            new AlertDialog.Builder(this)
                                    .setTitle("Quyền bị từ chối")
                                    .setMessage("Ứng dụng cần quyền " + permissions[i] + " để chọn/chụp ảnh. Vui lòng cấp quyền trong cài đặt.")
                                    .setPositiveButton("Đi tới cài đặt", (dialog, which) -> {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    })
                                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    } else {
                        // Quyền bị từ chối tạm thời
                        if (!hasShownPermissionDeniedMessage) {
                            hasShownPermissionDeniedMessage = true;
                            Toast.makeText(this, "Ứng dụng cần quyền " + permissions[i] + " để chọn/chụp ảnh", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    private void showImageSourceDialog() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasStoragePermission) {
            if (!hasShownPermissionDeniedMessage) {
                hasShownPermissionDeniedMessage = true;
                Toast.makeText(this, "Vui lòng cấp quyền truy cập bộ nhớ để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
            requestPermissions();
            return;
        }

        if (!hasCameraPermission) {
            if (!hasShownPermissionDeniedMessage) {
                hasShownPermissionDeniedMessage = true;
                Toast.makeText(this, "Vui lòng cấp quyền truy cập camera để chụp ảnh", Toast.LENGTH_SHORT).show();
            }
            requestPermissions();
            return;
        }

        // Đặt lại flag để có thể hiển thị thông báo trong lần yêu cầu quyền tiếp theo
        hasShownPermissionDeniedMessage = false;

        boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if (hasCamera && hasCameraPermission) {
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
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "product_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Mart4U");
        }

        currentPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.d("ProductEditActivity", "URI trước khi chụp ảnh: " + currentPhotoUri);
        if (currentPhotoUri != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Log.d("ProductEditActivity", "Không thể tạo URI để lưu ảnh chụp");
            Toast.makeText(this, "Không thể tạo URI để lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }
}