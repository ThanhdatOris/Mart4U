package com.ctut.mart4u.customer;

import android.os.Bundle;
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
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Product;

public class ProductDetailActivity extends BaseActivity {

    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
    @Override
    protected int getLayoutId() {
        return R.layout.customer_activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView recyclerProductDetail = findViewById(R.id.rec);
//        recyclerViewShoppingList.setLayoutManager(new LinearLayoutManager(this));
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
//            // Có productId => lấy chi tiết sản phẩm
//            Toast.makeText(this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();
            Product product = databaseHelper.getProductDao().getProductById(productId);

            Toast.makeText(this, "Product Name: " + product.getName(), Toast.LENGTH_SHORT).show();
        }

        EdgeToEdge.enable(this);

    }
}