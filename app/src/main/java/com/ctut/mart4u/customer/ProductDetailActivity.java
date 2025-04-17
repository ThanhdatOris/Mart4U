package com.ctut.mart4u.customer;

import android.os.Bundle;
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

public class ProductDetailActivity extends BaseActivity {

    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
    @Override
    protected int getLayoutId() {
        return R.layout.customer_activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView recyclerProductDetail = findViewById(R.id.recyclerViewProductDetail);
////
////
//        recyclerProductDetail.setLayoutManager(new LinearLayoutManager(this));
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
//            // Có productId => lấy chi tiết sản phẩm
//            Toast.makeText(this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();
            Product product = databaseHelper.getProductDao().getProductById(productId);
            Category category = databaseHelper.getCategoryDao().getCategoryById(product.getCategoryId());
            // Directly set values to views
            TextView productName = findViewById(R.id.textViewProductName);
            TextView productPrice = findViewById(R.id.textViewProductPrice);
            TextView productDescription = findViewById(R.id.textViewProductDescription);
            TextView stockQuantity = findViewById(R.id.textViewStockQuantity);
            TextView categoryName = findViewById(R.id.textViewCategory);

            productName.setText(product.getName());
            productPrice.setText(product.getPrice() + "đ");
            productDescription.setText(product.getDescription());
            stockQuantity.setText("Số lượng: " + product.getStockQuantity());
            categoryName.setText(category.getName());

//            ================================chuc nang cong, tru so luong san pham dat hang=========================================
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

            editTextQuantity.setOnClickListener(v -> {
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                if (quantity < 1) {
                    editTextQuantity.setText("1");
                } else if (quantity > product.getStockQuantity()) {
                    editTextQuantity.setText(String.valueOf(product.getStockQuantity()));
                }
            });

            // ====================================them vao gio hang========================
            Button buttonAddToCart = findViewById(R.id.buttonAddToCart);
            buttonAddToCart.setOnClickListener(v -> {
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                int userId = 1; //======================> lay userId tu dang nhap
                int productCartId = product.getId();
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                //luu vao database
                CartDetail cartDetail = new CartDetail(
                        userId,
                        productCartId,
                        quantity
                );
                try {
                    long result = databaseHelper.getCartDetailDao().insert(cartDetail);
                    if (result != -1) {
                        Toast.makeText(this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Không thể thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });

            // Set up RecyclerView with ProductDetailAdapter
//            Toast.makeText(this, "Product Name: " + product.getName(), Toast.LENGTH_SHORT).show();

        }

        EdgeToEdge.enable(this);

    }
}