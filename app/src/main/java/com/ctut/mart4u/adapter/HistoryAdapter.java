package com.ctut.mart4u.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryItem> historyItemList;
    private final DatabaseHelper databaseHelper;

    // Constructor
    public HistoryAdapter(List<HistoryItem> historyItemList, DatabaseHelper databaseHelper) {
        this.historyItemList = historyItemList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem historyItem = historyItemList.get(position);
        holder.textViewItemName.setText(historyItem.getName());
        holder.textViewQuantity.setText("Số lượng: " + historyItem.getQuantity());
        holder.textViewPurchaseDate.setText("Mua vào: " + historyItem.getPurchaseDate());

        // Xử lý sự kiện khi nhấn nút xóa
        holder.btnDeleteItem.setOnClickListener(v -> {
            databaseHelper.getHistoryDao().delete(historyItem); // Xóa món đồ khỏi cơ sở dữ liệu
            historyItemList.remove(position); // Xóa món đồ khỏi danh sách
            notifyItemRemoved(position); // Cập nhật RecyclerView
            notifyItemRangeChanged(position, historyItemList.size()); // Cập nhật các vị trí còn lại
        });
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    // Cập nhật danh sách và làm mới RecyclerView
    public void updateList(List<HistoryItem> newList) {
        this.historyItemList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder để giữ các thành phần giao diện
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewQuantity;
        TextView textViewPurchaseDate;
        Button btnDeleteItem;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewPurchaseDate = itemView.findViewById(R.id.textViewPurchaseDate);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}