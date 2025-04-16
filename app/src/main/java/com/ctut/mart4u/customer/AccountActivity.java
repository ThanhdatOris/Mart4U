package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.AppDatabase;
import com.ctut.mart4u.db.CategoryDao;
import com.ctut.mart4u.db.ProductDao;
import com.ctut.mart4u.model.Category;
import com.ctut.mart4u.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends BaseActivity {
    private RecyclerView productRecyclerView;
    private SearchView searchView;
    private Spinner categorySpinner;
    private ProductAdapter productAdapter;
    private AppDatabase db;
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private List<Product> productList;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_shopping_list);

        // Initialize views
        productRecyclerView = findViewById(R.id.productRecyclerView);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categorySpinner);

        // Initialize database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mart4u-db")
                .allowMainThreadQueries() // For simplicity, in production use AsyncTask or Coroutines
                .build();
        productDao = db.productDao();
        categoryDao = db.categoryDao();

        // Setup RecyclerView
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        // Load categories into spinner
        categoryList = categoryDao.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All Categories");
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Load all products initially
        loadProducts(null, -1);

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadProducts(query, getSelectedCategoryId());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadProducts(newText, getSelectedCategoryId());
                return true;
            }
        });

        // Category filter
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadProducts(searchView.getQuery().toString(), getSelectedCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadProducts(String keyword, int categoryId) {
        productList.clear();
        if (keyword != null && !keyword.isEmpty()) {
            productList.addAll(productDao.searchProducts(keyword));
        } else if (categoryId != -1) {
            productList.addAll(productDao.getProductsByCategory(categoryId));
        } else {
            productList.addAll(productDao.getAllProducts());
        }
        productAdapter.notifyDataSetChanged();
    }

    private int getSelectedCategoryId() {
        int position = categorySpinner.getSelectedItemPosition();
        if (position == 0) { // "All Categories"
            return -1;
        }
        return categoryList.get(position - 1).getId();
    }
}