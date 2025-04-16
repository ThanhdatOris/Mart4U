package com.ctut.mart4u.customer;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;

public class DeliveryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_delivery; // Trả về layout nội dung của DeliveryActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display, but without trying to access non-existent views
        EdgeToEdge.enable(this);

        // Xóa mấy dòng dưới đây hết
    }
}