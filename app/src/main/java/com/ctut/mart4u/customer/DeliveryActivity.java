package com.ctut.mart4u.customer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.customer.adapter.AddressAdapter;
import com.ctut.mart4u.db.AddressDao;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DeliveryScheduleDao;
import com.ctut.mart4u.db.UserDao;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.DeliverySchedule;
import com.ctut.mart4u.model.User;

import java.util.List;

public class DeliveryActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;
    private TextView tvAccountInfo;
    private TextView tvDeliveryAddress;
    private TextView tvStore;
    private Button btnUpdateAddress;
    private Address currentAddress;
    private User currentUser;
    private int userId = 1; // Giả sử userId = 1, thay đổi dựa trên đăng nhập

    @Override
    protected int getLayoutId() {
        return R.layout.customer_delivery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ các thành phần giao diện
        tvAccountInfo = findViewById(R.id.tvAccountInfo);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvStore = findViewById(R.id.tvStore);
        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);

        // Tải thông tin khách hàng, địa chỉ và cửa hàng
        loadUserInfo();
        loadAddressInfo();
        loadStoreInfo();

        // Sự kiện nút cập nhật địa chỉ
        btnUpdateAddress.setOnClickListener(v -> showManageAddressesDialog());

        // Tải lịch giao hàng
        loadDeliverySchedule();
    }

    private void loadUserInfo() {
        UserDao userDao = databaseHelper.getUserDao();
        currentUser = userDao.getUserById(userId); // Lấy thông tin user
        if (currentUser != null) {
            tvAccountInfo.setText("Tên: " + currentUser.getUsername() + "\nSố điện thoại: " + currentUser.getPhoneNumber());
        } else {
            tvAccountInfo.setText("Tên: Chưa có thông tin\nSố điện thoại: Chưa có thông tin");
        }
    }

    private void loadAddressInfo() {
        AddressDao addressDao = databaseHelper.getAddressDao();
        List<Address> addresses = addressDao.getAddressesByUser(userId);
        for (Address address : addresses) {
            if (address.isDefault()) {
                currentAddress = address;
                break;
            }
        }

        if (currentAddress == null && !addresses.isEmpty()) {
            currentAddress = addresses.get(0);
        }

        if (currentAddress != null) {
            tvDeliveryAddress.setText(currentAddress.getAddress());
        } else {
            tvDeliveryAddress.setText("Chưa có địa chỉ");
        }
    }

    private void loadStoreInfo() {
        // Giả lập thông tin cửa hàng (bạn có thể lấy từ cơ sở dữ liệu hoặc API)
        tvStore.setText("Cửa hàng: Căn Thơ");
    }

    private void showManageAddressesDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_manage_addresses);

        // Ánh xạ các thành phần trong dialog
        RecyclerView rvAddresses = dialog.findViewById(R.id.rvAddresses);
        Button btnAddNewAddress = dialog.findViewById(R.id.btnAddNewAddress);
        LinearLayout layoutNewAddress = dialog.findViewById(R.id.layoutNewAddress);
        EditText etReceiverName = dialog.findViewById(R.id.etReceiverName);
        EditText etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);
        EditText etAddress = dialog.findViewById(R.id.etAddress);
        Button btnSaveNewAddress = dialog.findViewById(R.id.btnSaveNewAddress);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Thiết lập RecyclerView với AddressAdapter
        AddressDao addressDao = databaseHelper.getAddressDao();
        AddressAdapter addressAdapter = new AddressAdapter(addressDao.getAddressesByUser(userId), addressDao);
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvAddresses.setAdapter(addressAdapter);

        // Sự kiện nút thêm địa chỉ mới
        btnAddNewAddress.setOnClickListener(v -> {
            layoutNewAddress.setVisibility(View.VISIBLE);
            btnAddNewAddress.setVisibility(View.GONE);
        });

        // Sự kiện nút lưu địa chỉ mới
        btnSaveNewAddress.setOnClickListener(v -> {
            String receiverName = etReceiverName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (receiverName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                return; // Có thể thêm thông báo lỗi
            }

            Address newAddress = new Address(userId, receiverName, phoneNumber, address, false);
            addressDao.insert(newAddress);
            addressAdapter.updateAddresses(addressDao.getAddressesByUser(userId));
            layoutNewAddress.setVisibility(View.GONE);
            btnAddNewAddress.setVisibility(View.VISIBLE);
        });

        // Sự kiện nút đóng
        btnClose.setOnClickListener(v -> {
            loadAddressInfo(); // Cập nhật lại địa chỉ mặc định
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadDeliverySchedule() {
        List<DeliverySchedule> schedules = databaseHelper.getInstance(this)
                .getDeliveryScheduleDao()
                .getAllSchedules();

        TableLayout tableLayout = findViewById(R.id.table_schedule);
        if (tableLayout == null) {
            return;
        }

        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        for (DeliverySchedule schedule : schedules) {
            TableRow row = new TableRow(this);

            TextView timeSlot = new TextView(this);
            timeSlot.setText(schedule.timeSlot);
            timeSlot.setPadding(8, 8, 8, 8);
            row.addView(timeSlot);

            TextView todayStatus = new TextView(this);
            todayStatus.setText(schedule.todayStatus);
            todayStatus.setGravity(android.view.Gravity.CENTER);
            todayStatus.setTextColor(schedule.todayStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            todayStatus.setPadding(8, 8, 8, 8);
            row.addView(todayStatus);

            TextView tomorrowStatus = new TextView(this);
            tomorrowStatus.setText(schedule.tomorrowStatus);
            tomorrowStatus.setGravity(android.view.Gravity.CENTER);
            tomorrowStatus.setTextColor(schedule.tomorrowStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            tomorrowStatus.setPadding(8, 8, 8, 8);
            row.addView(tomorrowStatus);

            TextView dayAfterTomorrowStatus = new TextView(this);
            dayAfterTomorrowStatus.setText(schedule.dayAfterTomorrowStatus);
            dayAfterTomorrowStatus.setGravity(android.view.Gravity.CENTER);
            dayAfterTomorrowStatus.setTextColor(schedule.dayAfterTomorrowStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            dayAfterTomorrowStatus.setPadding(8, 8, 8, 8);
            row.addView(dayAfterTomorrowStatus);

            tableLayout.addView(row);
        }
    }
}