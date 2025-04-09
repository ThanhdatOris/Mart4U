package com.ctut.mart4u;

import android.os.Bundle;
import android.widget.Button;

public class DeliveryActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_delivery; // Trả về layout nội dung của CategoryActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Khởi tạo các nút
        Button btnClose = findViewById(R.id.btnClose);
        Button btnReadyToOrder = findViewById(R.id.btnReadyToOrder);
        Button btnOpen = findViewById(R.id.btnOpen);

        // Xử lý sự kiện cho các nút (để rỗng theo yêu cầu)
        btnClose.setOnClickListener(v -> {
            // Để rỗng
        });

        btnReadyToOrder.setOnClickListener(v -> {
            // Để rỗng
        });

        btnOpen.setOnClickListener(v -> {
            // Để rỗng
        });
    }

}