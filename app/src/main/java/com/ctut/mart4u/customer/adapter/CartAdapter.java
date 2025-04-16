package com.ctut.mart4u.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private final DatabaseHelper databaseHelper;
    private final boolean showDeleteButton; // Biến để kiểm soát hiển thị nút xóa

    // Constructor
    public CartAdapter(List<CartItem> cartItemList, DatabaseHelper databaseHelper) {
        this(cartItemList, databaseHelper, true); // Mặc định hiển thị nút xóa
    }

    public CartAdapter(List<CartItem> cartItemList, DatabaseHelper databaseHelper, boolean showDeleteButton) {
        this.cartItemList = cartItemList;
        this.databaseHelper = databaseHelper;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = showDeleteButton ? R.layout.customer_item_cart : R.layout.customer_item_payment_product;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new CartViewHolder(view, showDeleteButton);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.textViewItemName.setText(cartItem.getName());
        holder.textViewQuantity.setText("Số lượng: " + cartItem.getQuantity());
        holder.textViewPrice.setText(String.format("%,.0fđ", cartItem.getPrice() * cartItem.getQuantity()));

        // Gán hình ảnh dựa trên tên sản phẩm
        String itemName = cartItem.getName().toLowerCase();
        if (itemName.contains("redbull")) {
            holder.imageViewProduct.setImageResource(R.drawable.img_product_redbull);
        } else if (itemName.contains("táo")) {
            holder.imageViewProduct.setImageResource(R.drawable.img_product_apple);
        } else {
            holder.imageViewProduct.setImageResource(R.drawable.img_product_apple); // Mặc định
        }

        // Nếu hiển thị nút xóa (cho CartActivity)
        if (showDeleteButton) {
            holder.textViewAddedDate.setText("Thêm vào: " + cartItem.getAddedDate());
            holder.btnDeleteItem.setOnClickListener(v -> {
                databaseHelper.getCartDao().delete(cartItem); // Xóa món đồ khỏi cơ sở dữ liệu
                cartItemList.remove(position); // Xóa món đồ khỏi danh sách
                notifyItemRemoved(position); // Cập nhật RecyclerView
                notifyItemRangeChanged(position, cartItemList.size()); // Cập nhật các vị trí còn lại
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    // Cập nhật danh sách và làm mới RecyclerView
    public void updateList(List<CartItem> newList) {
        this.cartItemList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder để giữ các thành phần giao diện
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewQuantity;
        TextView textViewPrice;
        TextView textViewAddedDate;
        ImageView imageViewProduct;
        Button btnDeleteItem;

        public CartViewHolder(@NonNull View itemView, boolean showDeleteButton) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);

            if (showDeleteButton) {
                textViewAddedDate = itemView.findViewById(R.id.textViewAddedDate);
                btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            }
        }
    }
}