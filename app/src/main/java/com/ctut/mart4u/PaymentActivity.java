package com.ctut.mart4u;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends BaseActivity {
    private TextView tvTotalPrice;
    private TextView tvFinalPrice;
    private TextView tvDeliveryFee;
    private TextView tvTotalTitle;
    private TextView tvProductTitle;
    private Button btnPlaceOrder;
    private RecyclerView recyclerViewProducts;
    private CartAdapter adapter;
    private List<CartItem> cartItemList;
    private DatabaseHelper databaseHelper;
    private double deliveryFee = 30000; // Phí giao hàng cố định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo các thành phần giao diện
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvFinalPrice = findViewById(R.id.tvFinalPrice);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
            String label = "Phí vận chuyển: ";
            String fee = "15.000đ"; // Ví dụ
            SpannableString spannable = new SpannableString(label + fee);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // tvDeliveryFee.setText(spannable);
        tvTotalTitle = findViewById(R.id.tvTotalTitle);
        tvProductTitle = findViewById(R.id.tvProductTitle);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        // Khởi tạo DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Khởi tạo danh sách và adapter
        cartItemList = new ArrayList<>();
        adapter = new CartAdapter(cartItemList, databaseHelper);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(adapter);

        // Load dữ liệu từ cơ sở dữ liệu
        loadCartItems();

        // Cập nhật tổng giá trị
        updateTotalPrice();

        // Xử lý sự kiện đặt hàng
        btnPlaceOrder.setOnClickListener(v -> {
            // Chức năng đặt hàng để rỗng theo yêu cầu
        });
    }

    // Phương thức để load dữ liệu từ cơ sở dữ liệu
    private void loadCartItems() {
        cartItemList.clear();
        cartItemList.addAll(databaseHelper.getCartDao().getAllCartItems());
        adapter.updateList(cartItemList);
    }

    // Phương thức để cập nhật tổng giá trị
    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }

        // Cập nhật tiêu đề
        tvProductTitle.setText(String.format("Sản phẩm (%d)", cartItemList.size()));
        tvTotalTitle.setText(String.format("Tổng giá trị (%d sản phẩm)", cartItemList.size()));

        // Cập nhật giá trị
        tvTotalPrice.setText(String.format("%,.0fđ", total));
        tvDeliveryFee.setText(String.format("%,.0fđ", deliveryFee));
        double finalPrice = total + deliveryFee;
        tvFinalPrice.setText(String.format("%,.0fđ", finalPrice));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment;
    }
}