package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.model.Purchase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<Purchase> purchaseList;

    public HistoryAdapter(Context context, List<Purchase> purchaseList) {
        this.context = context;
        this.purchaseList = purchaseList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_order_item_history, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Purchase purchase = purchaseList.get(position);

        try {
            long millis = Long.parseLong(purchase.getPurchaseDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDateTime = sdf.format(new Date(millis));
            holder.tvOrderDate.setText("Ngày đặt: " + formattedDateTime);
        } catch (Exception e) {
            holder.tvOrderDate.setText("Ngày đặt: " + purchase.getPurchaseDate());
        }


        holder.tvOrderStatus.setText("Trạng thái: " + purchase.getStatus());
        holder.tvOrderTotal.setText("Tổng tiền: " + purchase.getTotalAmount() + " VNĐ");

    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate;
        TextView tvOrderStatus;
        TextView tvOrderTotal;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);

        }
    }

    //    private Context context;
}
