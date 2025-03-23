package com.ctut.mart4u;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.ShoppingItem;

import java.util.List;

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

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            checkBoxBought = itemView.findViewById(R.id.checkBoxBought);
        }
    }
}