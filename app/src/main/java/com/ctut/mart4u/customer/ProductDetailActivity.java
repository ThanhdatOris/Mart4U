package com.ctut.mart4u.customer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.ProductDetailAdapter;
import com.ctut.mart4u.customer.adapter.ShoppingListAdapter;
import com.ctut.mart4u.db.CartDetailDao;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class ProductDetailActivity extends BaseActivity {
    private DatabaseHelper databaseHelper; // Khai báo biến

    @Override
    protected int getLayoutId() {
        return R.layout.customer_activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(this); // Khởi tạo trong onCreate

        int productId = getIntent().getIntExtra("productId", -1);

        if (productId != -1) {

            Product product = databaseHelper.getProductDao().getProductById(productId);
            if (product == null) {
                Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Category category = databaseHelper.getCategoryDao().getCategoryById(product.getCategoryId());
            if (category == null) {
                Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Gán giá trị cho các view
            TextView productName = findViewById(R.id.textViewProductName);
            TextView productPrice = findViewById(R.id.textViewProductPrice);
            TextView productDescription = findViewById(R.id.textViewProductDescription);
            TextView stockQuantity = findViewById(R.id.textViewStockQuantity);
            TextView categoryName = findViewById(R.id.textViewCategory);
            ImageView productImage = findViewById(R.id.imageViewProductDetail);

            productName.setText(product.getName());
            productPrice.setText(product.getPrice() + "đ");
            productDescription.setText(product.getDescription());
            stockQuantity.setText("Số lượng: " + product.getStockQuantity());
            categoryName.setText(category.getName());
            // Hiển thị ảnh sản phẩm
            InputStream is = null;
            try {
                is = getAssets().open(product.getImagePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            productImage.setImageBitmap(bitmap);

            // Chức năng tăng/giảm số lượng
            Button btnIncreaseQuantity = findViewById(R.id.buttonIncreaseQuantity);
            Button btnDecreaseQuantity = findViewById(R.id.buttonDecreaseQuantity);
            EditText editTextQuantity = findViewById(R.id.editTextQuantity);

            btnIncreaseQuantity.setOnClickListener(v -> {
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                if (quantity < product.getStockQuantity()) {
                    quantity++;
                    editTextQuantity.setText(String.valueOf(quantity));
                } else {
                    Toast.makeText(this, "Đã đạt số lượng tồn kho tối đa", Toast.LENGTH_SHORT).show();
                }
            });

            btnDecreaseQuantity.setOnClickListener(v -> {
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    editTextQuantity.setText(String.valueOf(quantity));
                } else {
                    Toast.makeText(this, "Không thể giảm dưới 1", Toast.LENGTH_SHORT).show();
                }
            });

            // Thêm vào giỏ hàng
            Button buttonAddToCart = findViewById(R.id.buttonAddToCart);
            buttonAddToCart.setOnClickListener(v -> {
                int userId = 1; // Thay bằng userId từ phiên đăng nhập
                int productCartId = product.getId();
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                CartDetail cartDetail = new CartDetail(userId, productCartId, quantity);
                try {
                    long result = databaseHelper.getCartDetailDao().insert(cartDetail);
                    if (result != -1) {
                        Toast.makeText(this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        updateCartBadge(); // Tk Khoa cập nhật badge sau khi thêm
                    } else {
                        Toast.makeText(this, "Không thể thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }
        EdgeToEdge.enable(this);
    }
}