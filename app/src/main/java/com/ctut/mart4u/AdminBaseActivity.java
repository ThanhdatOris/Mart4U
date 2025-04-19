package com.ctut.mart4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.CategoryActivity;
import com.ctut.mart4u.admin.CustomerActivity;
import com.ctut.mart4u.admin.ProductActivity;
import com.ctut.mart4u.db.DatabaseHelper;

public abstract class AdminBaseActivity extends AppCompatActivity {

    protected abstract int getLayoutId();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout_nav_container);

        databaseHelper = DatabaseHelper.getInstance(this);



        // Lấy container và thêm layout của activity con vào
        FrameLayout contentContainer = findViewById(R.id.content_container);
        View activityContent = getLayoutInflater().inflate(getLayoutId(), contentContainer, false);
        contentContainer.addView(activityContent);

        // Khởi tạo thanh điều hướng
        initNavigationBars();

        // Highlight tab hiện tại
        highlightCurrentTab();

        // Xử lý sự kiện cho nút tìm kiếm và cài đặt trên top nav bar
        ImageView icSearch = findViewById(R.id.ic_search_admin);
        ImageView icSettings = findViewById(R.id.ic_settings_admin);
        icSearch.setOnClickListener(v -> Toast.makeText(this, "Tính năng tìm kiếm đang phát triển", Toast.LENGTH_SHORT).show());
        icSettings.setOnClickListener(v -> Toast.makeText(this, "Tính năng cài đặt đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void initNavigationBars() {
        // Khởi tạo Bottom Navigation Bar
        LinearLayout tabCategory = findViewById(R.id.tab_category);
        LinearLayout tabProduct = findViewById(R.id.tab_product);
        LinearLayout tabPurchase = findViewById(R.id.tab_purchase);
        LinearLayout tabUser = findViewById(R.id.tab_user);
        LinearLayout tabAddress = findViewById(R.id.tab_address);

        if (tabCategory == null || tabProduct == null || tabPurchase == null ||
                tabUser == null || tabAddress == null) {
            android.util.Log.e("AdminBaseActivity", "One or more tabs not found in layout");
            Toast.makeText(this, "Lỗi: Không tìm thấy các tab trong layout", Toast.LENGTH_LONG).show();
            return;
        }

        // Xử lý sự kiện cho các tab
        tabCategory.setOnClickListener(v -> safeNavigateTo(CategoryActivity.class));
        tabProduct.setOnClickListener(v -> safeNavigateTo(ProductActivity.class));
//        tabPurchase.setOnClickListener(v -> safeNavigateTo(PurchaseActivity.class));
//        tabUser.setOnClickListener(v -> safeNavigateTo(UserActivity.class));
//        tabAddress.setOnClickListener(v -> safeNavigateTo(AddressActivity.class));
    }

    private void safeNavigateTo(Class<?> activityClass) {
        try {
            navigateTo(activityClass);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở trang này", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateTo(Class<?> activityClass) {
        if (this.getClass().equals(activityClass)) {
            return;
        }

        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void highlightCurrentTab() {
        resetTabsAppearance();

        if (this.getClass().equals(CategoryActivity.class)) {
            highlightTab(R.id.tab_category);
        } else if (this.getClass().equals(ProductActivity.class)) {
            highlightTab(R.id.tab_product);
        }else if (this.getClass().equals(CustomerActivity.class)) {
            highlightTab(R.id.tab_user);
        }
//        else if (this.getClass().equals(AddressActivity.class)) {
//            highlightTab(R.id.tab_address);
//        } else if (this.getClass().equals(PurchaseActivity.class)) {
//            highlightTab(R.id.tab_purchase);
//        }
    }

    private void resetTabsAppearance() {
        int defaultColor = getResources().getColor(android.R.color.white);
        setTabColor(R.id.tab_category, defaultColor);
        setTabColor(R.id.tab_product, defaultColor);
        setTabColor(R.id.tab_purchase, defaultColor);
        setTabColor(R.id.tab_user, defaultColor);
        setTabColor(R.id.tab_address, defaultColor);
    }

    private void highlightTab(int tabId) {
        int highlightColor = getResources().getColor(android.R.color.holo_orange_light);
        setTabColor(tabId, highlightColor);
    }

    private void setTabColor(int tabId, int color) {
        LinearLayout tab = findViewById(tabId);
        if (tab != null) {
            TextView textView = (TextView) tab.getChildAt(1);
            if (textView != null) {
                textView.setTextColor(color);
            }
            ImageView imageView = (ImageView) tab.getChildAt(0);
            if (imageView != null) {
                imageView.setColorFilter(color);
            }
        }
    }
}