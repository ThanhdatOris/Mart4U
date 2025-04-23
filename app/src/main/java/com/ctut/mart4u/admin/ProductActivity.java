package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.AdminBaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.adapter.ProductAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Product;

import java.util.List;

public class ProductActivity extends AdminBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.admin_product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        RecyclerView recyclerViewProduct = findViewById(R.id.productRecyclerView);
        recyclerViewProduct.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // lay thong tin product tu database
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Product> productList = databaseHelper.getProductDao().getAllProducts();

        Toast.makeText(this, "Số lượng sản phẩm: " + productList.size(), Toast.LENGTH_SHORT).show();

        // ===== click vao nut search
        ImageView searchIcon = findViewById(R.id.searchIcon);
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchIcon.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (query.isEmpty()) {
                ProductAdapter adapter = new ProductAdapter(this, productList);
                recyclerViewProduct.setAdapter(adapter);
                Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                List<Product> searchResults = databaseHelper.getProductDao().searchProducts(query);
                if (searchResults.isEmpty()) {
                    searchResults =databaseHelper.getProductDao().getAllProducts();
                } else {
                    ProductAdapter adapter = new ProductAdapter(this, searchResults);
                    recyclerViewProduct.setAdapter(adapter);
                }
            }
        });

        // xu ly su kien them san pham
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        //start ProductEditActivity
        btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, ProductEditActivity.class));
        });



        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerViewProduct.setAdapter(adapter);

    }
}