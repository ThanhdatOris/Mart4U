package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.adapter.HistoryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private Button btnDeleteAllHistory;
    private HistoryAdapter adapter;
    private List<HistoryItem> historyItemList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        btnDeleteAllHistory = findViewById(R.id.btnDeleteAllHistory);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        historyItemList = new ArrayList<>();
        adapter = new HistoryAdapter(historyItemList, databaseHelper);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadHistoryItems();

        // Xử lý sự kiện khi nhấn nút Delete All
        btnDeleteAllHistory.setOnClickListener(v -> {
            databaseHelper.getHistoryDao().deleteAllHistoryItems();
            loadHistoryItems(); // Làm mới danh sách
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu và cập nhật RecyclerView
    private void loadHistoryItems() {
        historyItemList.clear();
        historyItemList.addAll(databaseHelper.getHistoryDao().getAllHistoryItems());
        adapter.updateList(historyItemList);
    }
}