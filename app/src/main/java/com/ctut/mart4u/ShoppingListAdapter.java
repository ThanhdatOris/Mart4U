package com.ctut.mart4u;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartItem;
import com.ctut.mart4u.model.HistoryItem;
import com.ctut.mart4u.model.ShoppingItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder> {
    private List<ShoppingItem> shoppingList;
    private final DatabaseHelper databaseHelper;

    // Constructor
    public ShoppingListAdapter(List<ShoppingItem> shoppingList, DatabaseHelper databaseHelper) {
        this.shoppingList = shoppingList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);
        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem item = shoppingList.get(position);
        holder.textViewItemName.setText(item.getName());
        holder.textViewItemQuantity.setText("Qty: " + item.getQuantity());
        holder.checkBoxBought.setChecked(item.isBought());

        // Xử lý sự kiện khi CheckBox được thay đổi
        holder.checkBoxBought.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setBought(isChecked);
            databaseHelper.getShoppingDao().update(item); // Cập nhật trạng thái vào cơ sở dữ liệu

            // Nếu món đồ được đánh dấu là "đã mua", thêm vào lịch sử
            if (isChecked) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                HistoryItem historyItem = new HistoryItem(item.getName(), item.getQuantity(), currentDate);
                databaseHelper.getHistoryDao().insert(historyItem);
                Toast.makeText(holder.itemView.getContext(), item.getName() + " đã được thêm vào lịch sử mua sắm", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Thêm vào giỏ hàng"
        holder.btnAddToCart.setOnClickListener(v -> {
            // Lấy ngày hiện tại
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Tạo CartItem từ ShoppingItem
            CartItem cartItem = new CartItem(item.getName(), item.getQuantity(), currentDate);

            // Thêm vào bảng cart_items
            databaseHelper.getCartDao().insert(cartItem);

            // Thông báo cho người dùng
            Toast.makeText(holder.itemView.getContext(), item.getName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    // Cập nhật danh sách và làm mới RecyclerView
    public void updateList(List<ShoppingItem> newList) {
        this.shoppingList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder để giữ các thành phần giao diện
    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewItemQuantity;
        CheckBox checkBoxBought;
        Button btnAddToCart;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            checkBoxBought = itemView.findViewById(R.id.checkBoxBought);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}