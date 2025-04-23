package com.ctut.mart4u.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.db.AddressDao;
import com.ctut.mart4u.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addresses;
    private AddressDao addressDao;
    private OnEditAddressListener editAddressListener;

    // Constructor nhận danh sách địa chỉ, AddressDao, và listener để xử lý sự kiện sửa
    public AddressAdapter(List<Address> addresses, AddressDao addressDao, OnEditAddressListener editAddressListener) {
        this.addresses = addresses;
        this.addressDao = addressDao;
        this.editAddressListener = editAddressListener;
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

        // Xử lý sự kiện checkbox đặt làm mặc định
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

        // Xử lý sự kiện nút Sửa
        holder.btnEditAddress.setOnClickListener(v -> {
            if (editAddressListener != null) {
                editAddressListener.onEditAddress(address, position);
            }
        });

        // Xử lý sự kiện nút Xóa
        holder.btnDeleteAddress.setOnClickListener(v -> {
            // Không cho phép xóa nếu chỉ còn 1 địa chỉ và nó là địa chỉ mặc định
            if (addresses.size() == 1 && address.isDefault()) {
                Toast.makeText(v.getContext(), "Không thể xóa địa chỉ mặc định cuối cùng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Xóa địa chỉ khỏi cơ sở dữ liệu
            addressDao.delete(address);
            addresses.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, addresses.size());

            // Nếu địa chỉ bị xóa là địa chỉ mặc định, đặt địa chỉ đầu tiên trong danh sách làm mặc định
            if (address.isDefault() && !addresses.isEmpty()) {
                Address newDefaultAddress = addresses.get(0);
                newDefaultAddress.setDefault(true);
                addressDao.update(newDefaultAddress);
                notifyDataSetChanged();
            }

            Toast.makeText(v.getContext(), "Đã xóa địa chỉ", Toast.LENGTH_SHORT).show();
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
        TextView tvDeliveryMethod;
        CheckBox checkboxDefault;
        Button btnEditAddress;
        Button btnDeleteAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiverInfo = itemView.findViewById(R.id.tvReceiverInfo);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDeliveryMethod = itemView.findViewById(R.id.tvDeliveryMethod);
            checkboxDefault = itemView.findViewById(R.id.checkboxDefault);
            btnEditAddress = itemView.findViewById(R.id.btnEditAddress);
            btnDeleteAddress = itemView.findViewById(R.id.btnDeleteAddress);
        }
    }

    // Interface để xử lý sự kiện sửa địa chỉ
    public interface OnEditAddressListener {
        void onEditAddress(Address address, int position);
    }
}