package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.ShoppingListAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;

import java.util.List;

public class ShoppingListActivity extends BaseActivity {

    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerViewShoppingList = findViewById(R.id.recyclerViewShoppingList);
        recyclerViewShoppingList.setLayoutManager(new LinearLayoutManager(this));
        int categoryId = getIntent().getIntExtra("categoryId", -1);
        if (categoryId != -1) {
            // Có categoryId => lấy sản phẩm theo category
//            Toast.makeText(this, "Category ID: " + categoryId, Toast.LENGTH_SHORT).show();

            List<Product> productList = databaseHelper.getProductDao().getProductsByCategory(categoryId);
            ShoppingListAdapter adapter = new ShoppingListAdapter(this, productList);
            recyclerViewShoppingList.setAdapter(adapter);

        }
//        else {
//            // Không có categoryId => lấy toàn bộ sản phẩm
//            List<Product> productList = databaseHelper.getProductDao().getAllProducts();
//            ShoppingListAdapter adapter = new ShoppingListAdapter(this, productList);
//            recyclerViewShoppingList.setAdapter(adapter);
//            Toast.makeText(this, "Khong co category => lay tat ca product", Toast.LENGTH_SHORT).show();
//        }

        EdgeToEdge.enable(this);

    }
}