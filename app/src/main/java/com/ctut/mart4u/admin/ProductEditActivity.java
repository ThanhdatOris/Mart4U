package com.ctut.mart4u.admin;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Product;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;

public class ProductEditActivity extends AdminBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.admin_product_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        int productId = getIntent().getIntExtra("productId", -1);


        if(productId != -1){
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
            // Lấy thông tin sản phẩm từ database
            Product product = databaseHelper.getProductDao().getProductById(productId);
            Toast.makeText(this, "Sản phẩm ID: " + productId, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Sản phẩm : " + product.getName(), Toast.LENGTH_SHORT).show();

            ImageView productImage = findViewById(R.id.productImage);
            TextInputEditText productName = findViewById(R.id.etProductName);
            TextInputEditText etProductPrice = findViewById(R.id.etProductPrice);
            TextInputEditText etProductDescription = findViewById(R.id.etProductDescription);

            Toast.makeText(this, "Sản phẩm : " + product.getName(), Toast.LENGTH_SHORT).show();
            // Hiển thị thông tin sản phẩm lên giao diện
            productName.setText(product.getName());
            etProductPrice.setText(String.valueOf(product.getPrice()));
            etProductDescription.setText(product.getDescription());

            // anh
            InputStream is = null;
            try {
                is = getAssets().open(product.getImagePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            productImage.setImageBitmap(bitmap);

            // ==========================================su kien click vao nut huy
            Button btnCancelEdit = findViewById(R.id.btnCancelEdit);
            //quay lai trang truoc do
            btnCancelEdit.setOnClickListener(v -> {
                finish();
            });

            //============================su kien xoa
            Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
            btnDeleteProduct.setOnClickListener(v -> {
                // Xóa sản phẩm

                Product productToDelete = databaseHelper.getProductDao().getProductById(productId);
//                Toast.makeText(this, "Sản phẩm" + productId, Toast.LENGTH_SHORT).show();
                productToDelete.setDeleted(true);
                databaseHelper.getProductDao().update(productToDelete);
                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                // den trang ProductActivity
                Intent intent = new Intent(this, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            //========================================su kien luu
            Button btnSaveEdit = findViewById(R.id.btnSaveProduct);
            btnSaveEdit.setOnClickListener(v -> {
                // Lưu thông tin sản phẩm
                String name = productName.getText().toString();
                String price = etProductPrice.getText().toString();
                String description = etProductDescription.getText().toString();

                if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setName(name);
                product.setPrice(Double.parseDouble(price));
                product.setDescription(description);

                databaseHelper.getProductDao().update(product);
                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();

                // Quay lại trang ProductActivity
                Intent intent = new Intent(this, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            //========================================su kien chon luu anh
            Button btnChangeImage = findViewById(R.id.btnChangeImage);
            //duyet anh tu thiet bi, nguoi dung tu chon

        }
        else{
            TextView titleTextView = findViewById(R.id.titleTextView);
            titleTextView.setText("Thêm sản phẩm mới");
            // Ẩn các trường không cần thiết
            Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
            btnDeleteProduct.setVisibility(View.GONE);
            Button btnChangeImage = findViewById(R.id.btnChangeImage);
            btnChangeImage.setText("Thêm ảnh sản phẩm");
            Button btnCancelEdit = findViewById(R.id.btnCancelEdit);
            btnCancelEdit.setOnClickListener(v -> {
                finish();
            });
            Button btnSaveEdit = findViewById(R.id.btnSaveProduct);
            btnSaveEdit.setText("Tạo");


        }
    }
}