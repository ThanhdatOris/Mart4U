package com.ctut.mart4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

//import com.ctut.mart4u.admin.CategoryActivity;
import com.ctut.mart4u.customer.CategoryActivity;
import com.ctut.mart4u.customer.AccountActivity;
import com.ctut.mart4u.customer.DeliveryActivity;

public abstract class BaseActivity extends AppCompatActivity {

    // Phương thức trừu tượng để các Activity con cung cấp layout
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_container);

        // Lấy container và thêm layout của activity con vào
        FrameLayout contentContainer = findViewById(R.id.content_container);
        View activityContent = getLayoutInflater().inflate(getLayoutId(), contentContainer, false);
        contentContainer.addView(activityContent);

        // Khởi tạo thanh điều hướng
        initNavigationBars();

        // Highlight tab hiện tại
        highlightCurrentTab();
    }

    private void initNavigationBars() {
        // Khởi tạo Bottom Navigation Bar
        LinearLayout tabCategory = findViewById(R.id.tab_category);
        LinearLayout tabDelivery = findViewById(R.id.tab_delivery);
        LinearLayout tabLotteMart = findViewById(R.id.tab_lotte_mart);
        LinearLayout tabAccount = findViewById(R.id.tab_account);
        LinearLayout tabQuickBuy = findViewById(R.id.tab_quick_buy);

        // Xử lý sự kiện cho các tab với cải tiến
        tabCategory.setOnClickListener(v -> safeNavigateTo(CategoryActivity.class));
        tabDelivery.setOnClickListener(v -> safeNavigateTo(DeliveryActivity.class));
        tabLotteMart.setOnClickListener(v -> safeNavigateTo(MainActivity.class));
        tabAccount.setOnClickListener(v -> safeNavigateTo(AccountActivity.class));
        tabQuickBuy.setOnClickListener(v -> {
            // Handle the case when QuickBuyActivity is not yet implemented
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
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
        // Tránh tạo lại activity hiện tại
        if (this.getClass().equals(activityClass)) {
            return;
        }

        // Chuyển đến activity được chọn
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ngăn chặn việc xếp chồng activity
        startActivity(intent);
        overridePendingTransition(0, 0); // Tắt hiệu ứng chuyển đổi
    }

    private void highlightCurrentTab() {
        // Reset tất cả tab về trạng thái mặc định
        resetTabsAppearance();

        // Highlight tab hiện tại dựa trên loại activity
        if (this.getClass().equals(CategoryActivity.class)) {
            highlightTab(R.id.tab_category);
        } else if (this.getClass().equals(DeliveryActivity.class)) {
            highlightTab(R.id.tab_delivery);
        } else if (this.getClass().equals(MainActivity.class)) {
            highlightTab(R.id.tab_lotte_mart);
        } else if (this.getClass().equals(AccountActivity.class)) {
            highlightTab(R.id.tab_account);
        }
    }

    private void resetTabsAppearance() {
        int defaultColor = getResources().getColor(android.R.color.white);
        setTabColor(R.id.tab_category, defaultColor);
        setTabColor(R.id.tab_delivery, defaultColor);
        setTabColor(R.id.tab_lotte_mart, defaultColor);
        setTabColor(R.id.tab_account, defaultColor);
        setTabColor(R.id.tab_quick_buy, defaultColor);
    }

    private void highlightTab(int tabId) {
        int highlightColor = getResources().getColor(android.R.color.holo_orange_light);
        setTabColor(tabId, highlightColor);
    }

    private void setTabColor(int tabId, int color) {
        LinearLayout tab = findViewById(tabId);
        if (tab != null) {
            // Đổi màu TextView (văn bản)
            TextView textView = (TextView) tab.getChildAt(1);
            if (textView != null) {
                textView.setTextColor(color);
            }
            // Đổi màu ImageView (icon)
            ImageView imageView = (ImageView) tab.getChildAt(0);
            if (imageView != null) {
                imageView.setColorFilter(color);
            }
        }
    }
}