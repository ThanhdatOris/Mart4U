package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.ShoppingListAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewShoppingList;
    private EditText editTextItemName;
    private EditText editTextItemQuantity;
    private Button btnAddItem;
    private Button btnDeleteAll;
    private ShoppingListAdapter adapter;
    private List<ShoppingItem> shoppingList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // neu co du lieu ve cateogyID
        // nay lay id cua category tu intent => phuc vu viec get product theo category
        int categoryId = getIntent().getIntExtra("categoryId", -1);
        if (categoryId != -1) {
            // Có categoryId => lấy sản phẩm theo category
            Toast.makeText(this, "Category ID: " + categoryId, Toast.LENGTH_SHORT).show();
        } else {
            // Không có categoryId => lấy toàn bộ sản phẩm
            Toast.makeText(this, "Khong co category => lay tat ca product", Toast.LENGTH_SHORT).show();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_shopping_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        recyclerViewShoppingList = findViewById(R.id.recyclerViewShoppingList);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        shoppingList = new ArrayList<>();
        adapter = new ShoppingListAdapter(shoppingList, databaseHelper);
        recyclerViewShoppingList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewShoppingList.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadShoppingList();

        // Xử lý sự kiện khi nhấn nút Add
        btnAddItem.setOnClickListener(v -> {
            String itemName = editTextItemName.getText().toString().trim();
            String quantityStr = editTextItemQuantity.getText().toString().trim();

            if (itemName.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Please enter item name and quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo và thêm món đồ mới
            ShoppingItem newItem = new ShoppingItem(itemName, quantity, false);
            databaseHelper.getShoppingDao().insert(newItem);
            editTextItemName.setText("");
            editTextItemQuantity.setText("");
            loadShoppingList(); // Làm mới danh sách
        });

        // Xử lý sự kiện khi nhấn nút Delete All
        btnDeleteAll.setOnClickListener(v -> {
            databaseHelper.getShoppingDao().deleteAllItems();
            loadShoppingList(); // Làm mới danh sách
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu và cập nhật RecyclerView
    private void loadShoppingList() {
        shoppingList.clear();
        shoppingList.addAll(databaseHelper.getShoppingDao().getAllItems());
        adapter.updateList(shoppingList);
    }
//    private void loadCategories() {
//        if (isSampleData) {
//            // Dữ liệu mẫu để test
//            categoryList.clear();
//            categoryList.add(new Category("Thịt"));
//            categoryList.add(new Category("Rau củ"));
//            categoryList.add(new Category("Trái cây"));
//        } else {
//            // Dữ liệu thực từ database
//            categoryList.clear();
//            categoryList.addAll(categoryDao.getAllCategories());
//        }
//
//        categoryAdapter.notifyDataSetChanged();
//    }
}