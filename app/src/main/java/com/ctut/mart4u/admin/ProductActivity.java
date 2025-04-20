package com.ctut.mart4u.admin;

import android.os.Bundle;
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
        EdgeToEdge.enable(this);
        RecyclerView recyclerViewProduct = findViewById(R.id.productRecyclerView);
        recyclerViewProduct.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // lay thong tin product tu database
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Product> productList = databaseHelper.getProductDao().getAllProducts();

        Toast.makeText(this, "Số lượng sản phẩm: " + productList.size(), Toast.LENGTH_SHORT).show();

        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerViewProduct.setAdapter(adapter);

    }
}