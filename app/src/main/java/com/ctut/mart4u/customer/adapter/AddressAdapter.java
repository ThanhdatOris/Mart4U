package com.ctut.mart4u.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.AddressDao;
import com.ctut.mart4u.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addresses;
    private AddressDao addressDao;

    // Constructor nhận danh sách địa chỉ và AddressDao
    public AddressAdapter(List<Address> addresses, AddressDao addressDao) {
        this.addresses = addresses;
        this.addressDao = addressDao;
    }

    // Phương thức cập nhật danh sách địa chỉ
    public void updateAddresses(List<Address> newAddresses) {
        this.addresses = newAddresses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addresses.get(position);
        holder.tvReceiverInfo.setText("Tên: " + address.getReceiverName() + " - SĐT: " + address.getPhoneNumber());
        holder.tvAddress.setText("Địa chỉ: " + address.getAddress());
        holder.tvDeliveryMethod.setText("Phương thức giao hàng: " + address.getDeliveryMethod());
        holder.checkboxDefault.setChecked(address.isDefault());

        holder.checkboxDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                for (Address addr : addresses) {
                    if (addr.getId() != address.getId() && addr.isDefault()) {
                        addr.setDefault(false);
                        addressDao.update(addr);
                    }
                }
                address.setDefault(true);
                addressDao.update(address);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    // ViewHolder bên trong AddressAdapter
    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiverInfo;
        TextView tvAddress;
        TextView tvDeliveryMethod; // Thêm để hiển thị phương thức giao hàng
        CheckBox checkboxDefault;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiverInfo = itemView.findViewById(R.id.tvReceiverInfo);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDeliveryMethod = itemView.findViewById(R.id.tvDeliveryMethod);
            checkboxDefault = itemView.findViewById(R.id.checkboxDefault);
        }
    }
}