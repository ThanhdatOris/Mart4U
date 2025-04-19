package com.ctut.mart4u.customer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.CartEntryAdapter;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.PurchaseDetail;

import java.util.List;

public class CartActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userId = getIntent().getIntExtra("user_id", -1);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<CartDetail> cartList = databaseHelper.getCartDetailDao().getCartDetailsByUser(userId);

        RecyclerView recyclerViewCart = findViewById(R.id.recyclerViewCart);
        TextView textViewTotalPrice = findViewById(R.id.tvTotalPrice);
        TextView tvProductListTitle = findViewById(R.id.tvProductListTitle);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAllCart);

        if (cartList == null || cartList.isEmpty()) {
            // Giỏ hàng trống
            tvProductListTitle.setText("Giỏ hàng trống");
            textViewTotalPrice.setText(""); // Không hiển thị tổng tiền
            recyclerViewCart.setVisibility(View.GONE); // Ẩn RecyclerView
            btnDeleteAll.setEnabled(false); // Vô hiệu hóa nút xóa tất cả
        } else {
            // Giỏ hàng có sản phẩm
            recyclerViewCart.setVisibility(View.VISIBLE);
            btnDeleteAll.setEnabled(true);

            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
            CartEntryAdapter adapter = new CartEntryAdapter(this, cartList);
            recyclerViewCart.setAdapter(adapter);

            // Tính tổng tiền
            int totalPrice = 0;
            for (CartDetail cartDetail : cartList) {
                totalPrice += cartDetail.getQuantity() * databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getPrice();
            }
            textViewTotalPrice.setText("Tổng tiền: " + totalPrice + " VND");
            tvProductListTitle.setText("Có " + cartList.size() + " sản phẩm trong giỏ hàng");

            // Xử lý sự kiện xóa tất cả
            btnDeleteAll.setOnClickListener(v -> {
                databaseHelper.getCartDetailDao().deleteAll(userId);
                cartList.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã xóa tất cả sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();

                // Cập nhật giao diện khi giỏ hàng trống
                tvProductListTitle.setText("Giỏ hàng trống");
                textViewTotalPrice.setText("");
                recyclerViewCart.setVisibility(View.GONE);
                btnDeleteAll.setEnabled(false);
            });

            // xử lý sự kiện thanh toán
            Button btnCheckout = findViewById(R.id.btnCheckout);
            btnCheckout.setOnClickListener(v -> {
                // Xử lý thanh toán
                Toast.makeText(this, "Thanh toán thành công" + cartList.size(), Toast.LENGTH_SHORT).show();
                // ===============tao don hang
                double totalAmount = 0;
                for (CartDetail cartDetail : cartList) {
                    totalAmount += cartDetail.getQuantity() * databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getPrice();
                }
                Purchase purchase = new Purchase(userId, String.valueOf(System.currentTimeMillis()) , totalAmount, "pending");
                //=======tao chi tiet don hang: tu cartDetail qua purchaseDetail
                databaseHelper.getPurchaseDao().insert(purchase);
                for(CartDetail cartDetail : cartList) {
//                    Toast.makeText(this, "Chi tiet don hang" + cartDetail.getProductId(), Toast.LENGTH_SHORT).show();
                    Product product = databaseHelper.getProductDao().getProductById(cartDetail.getProductId());
                    PurchaseDetail purchaseDetail = new PurchaseDetail(
                            //tao moi don hang chi tiet
                            purchase.getId(),
                            cartDetail.getProductId(),
                            cartDetail.getQuantity(),
                            product.getPrice()
                    );
                    //xoa chi tiet don hang
                    databaseHelper.getCartDetailDao().delete(cartDetail);
                }
                cartList.clear();
                //thong bao thanh toan thanh cong
                Toast.makeText(this, "Thanh toán thành công" + purchase.getId() + " " + purchase.getStatus() + " " + purchase.getPurchaseDate() + " " + purchase.getUserId(), Toast.LENGTH_SHORT).show();

            });
        }

        EdgeToEdge.enable(this);
    }
}