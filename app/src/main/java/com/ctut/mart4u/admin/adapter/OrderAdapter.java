package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.Purchase;
import com.ctut.mart4u.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Purchase> orderList;
    private OnOrderClickListener onOrderClickListener;
    private DatabaseHelper databaseHelper;

    public interface OnOrderClickListener {
        void onOrderClick(Purchase purchase);
    }

    public OrderAdapter(Context context, List<Purchase> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = listener;
        this.databaseHelper = DatabaseHelper.getInstance(context);
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
        holder.tvOrderId.setText("Mã đơn hàng: " + purchase.getId());
//        holder.tvPurchaseDate.setText("Ngày mua: " + purchase.getPurchaseDate());
        // Xử lý ngày mua
        String purchaseDate = purchase.getPurchaseDate();
        String formattedDate;
        try {
            // Thử phân tích purchaseDate dưới dạng timestamp (dãy số)
            long timestamp = Long.parseLong(purchaseDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formattedDate = dateFormat.format(new Date(timestamp));
        } catch (NumberFormatException e) {
            // Nếu không phải timestamp, giả sử nó đã là chuỗi yyyy-MM-dd
            formattedDate = purchaseDate;
        }

        holder.tvPurchaseDate.setText("Ngày mua: " + formattedDate);

        holder.tvTotalAmount.setText("Tổng tiền: " + (int) purchase.getTotalAmount() + " VND");
        holder.tvStatus.setText("Trạng thái: " + purchase.getStatus());

        // Hiển thị tên khách hàng và số điện thoại
        User user = databaseHelper.getUserDao().getUserById(purchase.getUserId());
        if (user != null) {
            holder.tvCustomerName.setText("Tên khách hàng: " + user.getUsername());
            holder.tvPhoneNumber.setText("Số điện thoại: " + user.getPhoneNumber());

            // Hiển thị địa chỉ khách hàng
            Address address = databaseHelper.getAddressDao().getDefaultAddress(user.getId());
            if (address != null) {
                holder.tvCustomerAddress.setText("Địa chỉ: " + address.getAddress());
            } else {
                holder.tvCustomerAddress.setText("Địa chỉ: Chưa có");
            }
        }

        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderClick(purchase));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvPhoneNumber, tvPurchaseDate, tvTotalAmount, tvStatus, tvCustomerAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvPurchaseDate = itemView.findViewById(R.id.tvPurchaseDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCustomerAddress = itemView.findViewById(R.id.tvCustomerAddress);
        }
    }
}