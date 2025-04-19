package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;

import java.util.List;

public class CartEntryAdapter  extends RecyclerView.Adapter<CartEntryAdapter.CartEntryViewHolder> {
    private Context context;
    private List<CartDetail> cartList;

    public CartEntryAdapter(Context context, List<CartDetail> cartList) {
        this.context = context;
        this.cartList = cartList;
    }
    @NonNull
    @Override
    public CartEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.customer_item_cart, null);
        return new CartEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartEntryAdapter.CartEntryViewHolder holder, int position) {
        CartDetail cartDetail = cartList.get(position);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        String productName = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getName();
        String productPrice = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getPrice() + " VND";

        holder.textViewItemName.setText(productName);
        holder.textViewPrice.setText(productPrice);
        holder.textViewQuantity.setText("Số lượng: " + cartDetail.getQuantity());
        // =========================Lấy ảnh sản phẩm từ cơ sở dữ liệu
//        int productImage = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getImage();
//        holder.imageViewProduct.setImageResource(productImage);



//        holder.textViewAddedDate.setText("Ngày thêm: " + cartDetail.getAddedDate());

        // ===========================su kien xoa 1 san pham===========================
        holder.btnDeleteItem.setOnClickListener(v -> {
//            Toast.makeText(context, "Xóa sản phẩm khỏi giỏ hàng" + cartDetail.getProductId(), Toast.LENGTH_SHORT).show();
            // Xóa sản phẩm khỏi giỏ hàng
            databaseHelper.getCartDetailDao().delete(cartDetail);
            // Cập nhật lại danh sách giỏ hàng
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartEntryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewPrice;
        TextView textViewQuantity;
        TextView textViewAddedDate;
        Button btnDeleteItem;
        ImageView imageViewProduct;
        public CartEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewAddedDate = itemView.findViewById(R.id.textViewAddedDate);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);

        }
    }
}
