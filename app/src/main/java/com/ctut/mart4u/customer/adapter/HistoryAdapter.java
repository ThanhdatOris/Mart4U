package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.PurchaseDetailActivity;
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

//        holder.tvCustomerName.setText("Tên khách hàng: " + purchase.get());
//        holder.tvPhoneNumber.setText("Số điện thoại: " + purchase.getPhoneNumber());
//        holder.tvShippingAddress.setText("Địa chỉ giao hàng: " + purchase.getShippingAddress());

        holder.tvOrderStatus.setText("Trạng thái: " + purchase.getStatus());
        holder.tvOrderTotal.setText("Tổng tiền: " + purchase.getTotalAmount() + " VNĐ");



        //xu ly su kien click xem chi tiet
        holder.btnViewDetails.setOnClickListener(v -> {
//            Toast.makeText(context, "Xem chi tiết đơn hàng" + purchase.getTotalAmount(), Toast.LENGTH_SHORT).show();
            //chuyen sang trang chi tiet don hang
            Context context = v.getContext();
            Intent intent = new Intent(context, PurchaseDetailActivity.class);
            intent.putExtra("purchaseId", purchase.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate;
        TextView tvOrderStatus;
        TextView tvOrderTotal;
        Button btnViewDetails;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);

        }
    }

    //    private Context context;
}
