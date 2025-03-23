package com.ctut.mart4u;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private final DatabaseHelper databaseHelper;

    // Constructor
    public CartAdapter(List<CartItem> cartItemList, DatabaseHelper databaseHelper) {
        this.cartItemList = cartItemList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.textViewItemName.setText(cartItem.getName());
        holder.textViewQuantity.setText("Số lượng: " + cartItem.getQuantity());
        holder.textViewAddedDate.setText("Thêm vào: " + cartItem.getAddedDate());

        // Xử lý sự kiện khi nhấn nút xóa
        holder.btnDeleteItem.setOnClickListener(v -> {
            databaseHelper.getCartDao().delete(cartItem); // Xóa món đồ khỏi cơ sở dữ liệu
            cartItemList.remove(position); // Xóa món đồ khỏi danh sách
            notifyItemRemoved(position); // Cập nhật RecyclerView
            notifyItemRangeChanged(position, cartItemList.size()); // Cập nhật các vị trí còn lại
        });
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
        TextView textViewAddedDate;
        Button btnDeleteItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewAddedDate = itemView.findViewById(R.id.textViewAddedDate);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}