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

import com.ctut.mart4u.customer.CartActivity;
import com.ctut.mart4u.customer.CategoryActivity;
import com.ctut.mart4u.customer.AccountActivity;
import com.ctut.mart4u.customer.DeliveryActivity;
import com.ctut.mart4u.customer.HistoryActivity;
import com.ctut.mart4u.db.DatabaseHelper;

public abstract class BaseActivity extends AppCompatActivity {

    // Phương thức trừu tượng để các Activity con cung cấp layout
    protected abstract int getLayoutId();
    private TextView cartBadge;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_container);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Lấy container và thêm layout của activity con vào
        FrameLayout contentContainer = findViewById(R.id.content_container);
        View activityContent = getLayoutInflater().inflate(getLayoutId(), contentContainer, false);
        contentContainer.addView(activityContent);

        // Khởi tạo thanh điều hướng
        initNavigationBars();

        // Highlight tab hiện tại
        highlightCurrentTab();

        // Cập nhật badge giỏ hàng
        updateCartBadge();
    }

    private void initNavigationBars() {
        // Khởi tạo Bottom Navigation Bar
        LinearLayout tabCategory = findViewById(R.id.tab_category);
        LinearLayout tabDelivery = findViewById(R.id.tab_delivery);
        LinearLayout tabLotteMart = findViewById(R.id.tab_lotte_mart);
        LinearLayout tabAccount = findViewById(R.id.tab_account);
        LinearLayout tabQuickBuy = findViewById(R.id.tab_quick_buy);
        cartBadge = findViewById(R.id.cart_badge);

        if (tabCategory == null || tabDelivery == null || tabLotteMart == null ||
                tabAccount == null || tabQuickBuy == null || cartBadge == null) {
            android.util.Log.e("BaseActivity", "One or more tabs or badge not found in layout");
            Toast.makeText(this, "Lỗi: Không tìm thấy các tab hoặc badge trong layout", Toast.LENGTH_LONG).show();
            return;
        }

        // Xử lý sự kiện cho các tab với cải tiến
        tabCategory.setOnClickListener(v -> safeNavigateTo(CategoryActivity.class));
        tabDelivery.setOnClickListener(v -> safeNavigateTo(DeliveryActivity.class));
        tabLotteMart.setOnClickListener(v -> safeNavigateTo(MainActivity.class));
        tabAccount.setOnClickListener(v -> safeNavigateTo(AccountActivity.class));
        tabQuickBuy.setOnClickListener(v -> safeNavigateTo(HistoryActivity.class));

        // Xử lý sự kiện click cho cartBadge
        cartBadge.setOnClickListener(v -> {
            int userId = getCurrentUserId();
            if (userId == -1) {
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                // Chuyển đến màn hình đăng nhập
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return;
            }

            Intent intent = new Intent(BaseActivity.this, CartActivity.class);
            intent.putExtra("user_id", userId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
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
        } else if (this.getClass().equals(HistoryActivity.class)) {
            highlightTab(R.id.tab_quick_buy);
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

    // Phương thức cập nhật badge
    public void updateCartBadge() {
        if (cartBadge == null) return;

        int userId = getCurrentUserId();
        if (userId == -1) {
            // Nếu người dùng chưa đăng nhập, ẩn badge
            cartBadge.setVisibility(View.GONE);
            return;
        }

        int itemCount = databaseHelper.getCartDetailDao().getCartItemCount(userId);
        if (itemCount > 0) {
            cartBadge.setText(String.valueOf(itemCount));
            cartBadge.setVisibility(View.VISIBLE);
        } else {
            cartBadge.setVisibility(View.GONE);
        }
    }

    // Lấy userId từ SharedPreferences (sửa lại để khớp với LoginActivity)
    protected int getCurrentUserId() {
        return getSharedPreferences("login_prefs", MODE_PRIVATE).getInt("userId", -1);
    }
}