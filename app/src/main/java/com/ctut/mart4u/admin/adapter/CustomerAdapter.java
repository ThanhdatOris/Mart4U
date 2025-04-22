package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.model.User;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<User> customerList;
    private OnCustomerClickListener onCustomerClickListener;

    public interface OnCustomerClickListener {
        void onCustomerClick(User user);
    }

    public CustomerAdapter(Context context, List<User> customerList, OnCustomerClickListener listener) {
        this.context = context;
        this.customerList = customerList;
        this.onCustomerClickListener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User customer = customerList.get(position);
        holder.tvUsername.setText(customer.getUsername());
        holder.tvEmail.setText(customer.getEmail());
        holder.tvPhoneNumber.setText(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "Chưa có số điện thoại");

        holder.itemView.setOnClickListener(v -> onCustomerClickListener.onCustomerClick(customer));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail, tvPhoneNumber;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}