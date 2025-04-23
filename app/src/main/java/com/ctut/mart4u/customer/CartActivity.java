package com.ctut.mart4u.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CartEntryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.CartDetail;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;
import com.ctut.mart4u.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity {

    private List<CartDetail> cartList;
    private TextView textViewTotalPrice;
    private TextView tvProductListTitle;
    private RecyclerView recyclerViewCart;
    private Button btnDeleteAll;
    private Button btnCheckout;
    private DatabaseHelper databaseHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userId = getIntent().getIntExtra("user_id", -1);
        databaseHelper = DatabaseHelper.getInstance(this);
        cartList = databaseHelper.getCartDetailDao().getCartDetailsByUser(userId);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice = findViewById(R.id.tvTotalPrice);
        tvProductListTitle = findViewById(R.id.tvProductListTitle);
        btnDeleteAll = findViewById(R.id.btnDeleteAllCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        TextView tvAccountInfo = findViewById(R.id.tvAccountInfo);
        TextView tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        TextView tvStore = findViewById(R.id.tvStore);

        // Lấy thông tin khách hàng (User)
        User user = databaseHelper.getUserDao().getUserById(userId);
        if (user != null) {
            // Hiển thị tên và số điện thoại từ User
            tvAccountInfo.setText(user.getUsername() + "  " + user.getPhoneNumber());

            // Lấy địa chỉ mặc định của khách hàng
            Address address = databaseHelper.getAddressDao().getDefaultAddress(userId);
            if (address != null) {
                tvDeliveryAddress.setText(address.getAddress());
                tvStore.setText("Cửa hàng: " + address.getDeliveryMethod());
            } else {
                tvDeliveryAddress.setText("Chưa có địa chỉ giao hàng");
                tvStore.setText("Cửa hàng: Chưa chọn");
            }
        } else {
            tvAccountInfo.setText("Không tìm thấy thông tin khách hàng");
            tvDeliveryAddress.setText("Chưa có địa chỉ giao hàng");
            tvStore.setText("Cửa hàng: Chưa chọn");
        }

        // Thiết lập giao diện ban đầu
        setupCartView();
        EdgeToEdge.enable(this);
    }

    // Phương thức tính tổng giá và cập nhật giao diện
    private void updateTotalPriceAndUI() {
        if (cartList == null || cartList.isEmpty()) {
            tvProductListTitle.setText("Giỏ hàng trống");
            textViewTotalPrice.setText("");
            recyclerViewCart.setVisibility(View.GONE);
            btnDeleteAll.setEnabled(false);
            btnCheckout.setEnabled(false); // Vô hiệu hóa nút thanh toán
        } else {
            int totalPrice = 0;
            for (CartDetail cartDetail : cartList) {
                Product product = databaseHelper.getProductDao().getProductById(cartDetail.getProductId());
                if (product != null) {
                    totalPrice += cartDetail.getQuantity() * product.getPrice();
                }
            }
            textViewTotalPrice.setText("Tổng tiền: " + totalPrice + " VND");
            tvProductListTitle.setText("Có " + cartList.size() + " sản phẩm trong giỏ hàng");
            recyclerViewCart.setVisibility(View.VISIBLE);
            btnDeleteAll.setEnabled(true);
            btnCheckout.setEnabled(totalPrice > 0); // Chỉ cho phép thanh toán nếu tổng giá lớn hơn 0
        }
    }

    // Thiết lập giao diện giỏ hàng
    private void setupCartView() {
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        CartEntryAdapter adapter = new CartEntryAdapter(this, cartList, () -> updateTotalPriceAndUI());
        recyclerViewCart.setAdapter(adapter);

        // Cập nhật tổng giá ban đầu
        updateTotalPriceAndUI();

        // Xử lý sự kiện xóa tất cả
        btnDeleteAll.setOnClickListener(v -> {
            databaseHelper.getCartDetailDao().deleteAll(getIntent().getIntExtra("user_id", -1));
            cartList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa tất cả sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            updateTotalPriceAndUI();
        });

        // Xử lý sự kiện thanh toán
        btnCheckout.setOnClickListener(v -> {
            if (cartList == null || cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống, không thể thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = getIntent().getIntExtra("user_id", -1);

            // Kỉiểm tra ếu người dùng đã đăng nhập và có thông tin địa chỉ
            User user = databaseHelper.getUserDao().getUserById(userId);
            if (user == null) {
                Toast.makeText(this, "Vui lòng đăng nhập để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            Address address = databaseHelper.getAddressDao().getDefaultAddress(userId);
            if (address == null) {
                Toast.makeText(this, "Vui lòng thêm địa chỉ giao hàng trước khi thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalAmount = 0;
            for (CartDetail cartDetail : cartList) {
                Product product = databaseHelper.getProductDao().getProductById(cartDetail.getProductId());
                if (product != null) {
                    totalAmount += cartDetail.getQuantity() * product.getPrice();
                }
            }

            if (totalAmount <= 0) {
                Toast.makeText(this, "Tổng tiền bằng 0, không thể thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            // Định dạng ngày hiện tại thành yyyy-MM-dd
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            // Tạo đơn hàng
            Purchase purchase = new Purchase(userId, String.valueOf(System.currentTimeMillis()), totalAmount, "pending");
            long purchaseId = databaseHelper.getPurchaseDao().insert(purchase);
            purchase.setId((int) purchaseId);

            // Tạo chi tiết đơn hàng
            for (CartDetail cartDetail : cartList) {
                Product product = databaseHelper.getProductDao().getProductById(cartDetail.getProductId());
                if (product != null) {
                    PurchaseDetail purchaseDetail = new PurchaseDetail(
                            purchase.getId(),
                            cartDetail.getProductId(),
                            cartDetail.getQuantity(),
                            product.getPrice()
                    );
                    databaseHelper.getPurchaseDetailDao().insert(purchaseDetail);
                    databaseHelper.getCartDetailDao().delete(cartDetail);
                }
            }

            cartList.clear();
            Toast.makeText(this, "Thanh toán thành công. Mã đơn hàng: " + purchase.getId(), Toast.LENGTH_SHORT).show();
            updateTotalPriceAndUI();

            // Chuyển qua HistoryActivity
            Intent intent = new Intent(CartActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}