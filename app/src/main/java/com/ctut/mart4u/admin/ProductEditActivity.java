package com.ctut.mart4u.admin;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

            ImageView productImage = findViewById(R.id.productImage);
            Button btnChangeImage = findViewById(R.id.btnChangeImage);
            TextInputEditText productName = findViewById(R.id.etProductName);
            TextInputEditText etProductPrice = findViewById(R.id.etProductPrice);
            TextInputEditText etProductDescription = findViewById(R.id.etProductDescription);

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

            //============================su kien nut xoa
//            Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
//            btnDeleteProduct.setOnClickListener(v -> {
//                // Xóa sản phẩm
//                databaseHelper.getProductDao().delete(productId);
//                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
//                finish();
//            });

        }
        else{
            // Không có productId => không thể chỉnh sửa sản phẩm
            Toast.makeText(this, "Không có thông tin sản phẩm để chỉnh sửa", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu không có thông tin sản phẩm
        }
    }
}