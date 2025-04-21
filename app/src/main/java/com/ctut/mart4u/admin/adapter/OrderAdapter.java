package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.model.Purchase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Purchase> orderList;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Purchase purchase);
    }

    public OrderAdapter(Context context, List<Purchase> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Purchase purchase = orderList.get(position);
        holder.tvOrderId.setText(String.valueOf(purchase.getId()));
        holder.tvUserId.setText(String.valueOf(purchase.getUserId()));
        holder.tvPurchaseDate.setText(purchase.getPurchaseDate());
        holder.tvTotalAmount.setText(String.format("%.2f", purchase.getTotalAmount()));
        holder.tvStatus.setText(purchase.getStatus());

        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderClick(purchase));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserId, tvPurchaseDate, tvTotalAmount, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvPurchaseDate = itemView.findViewById(R.id.tvPurchaseDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}