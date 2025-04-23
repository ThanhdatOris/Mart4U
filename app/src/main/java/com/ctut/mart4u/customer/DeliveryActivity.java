package com.ctut.mart4u.customer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.LoginActivity;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.adapter.AddressAdapter;
import com.ctut.mart4u.db.AddressDao;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.db.DeliveryScheduleDao;
import com.ctut.mart4u.model.Address;
import com.ctut.mart4u.model.DeliverySchedule;
import com.ctut.mart4u.model.User;
import com.ctut.mart4u.utils.UserSession;

import java.util.List;

public class DeliveryActivity extends BaseActivity implements AddressAdapter.OnEditAddressListener {
    private DatabaseHelper databaseHelper;
    private LinearLayout layoutNotLoggedIn, layoutLoggedIn;
    private Button btnLogin, btnUpdateAddress;
    private TextView tvAccountInfo, tvDeliveryAddress, tvStore, tvDeliveryFee;
    private RadioGroup rgDeliveryMethod;
    private Address currentAddress;
    private User currentUser;
    private Address editingAddress; // Để lưu địa chỉ đang chỉnh sửa
    private int editingPosition; // Vị trí của địa chỉ đang chỉnh sửa

    @Override
    protected int getLayoutId() {
        return R.layout.customer_delivery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.red));
        databaseHelper = DatabaseHelper.getInstance(this);

        // Ánh xạ các thành phần giao diện
        layoutNotLoggedIn = findViewById(R.id.layoutNotLoggedIn);
        layoutLoggedIn = findViewById(R.id.layoutLoggedIn);
        btnLogin = findViewById(R.id.btnLogin);
        tvAccountInfo = findViewById(R.id.tvAccountInfo);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        tvStore = findViewById(R.id.tvStore);
        rgDeliveryMethod = findViewById(R.id.rgDeliveryMethod);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);

        // Cập nhật giao diện dựa trên trạng thái đăng nhập
        updateUI();

        // Xử lý sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(DeliveryActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Tải lịch giao hàng (hiển thị cho cả hai trạng thái)
        loadDeliverySchedule();

        // Nếu đã đăng nhập, thiết lập các sự kiện liên quan đến giao hàng
        if (UserSession.getInstance().isLoggedIn()) {
            // Tải thông tin khách hàng, địa chỉ và cửa hàng
            loadUserInfo();
            loadAddressInfo();
            loadStoreInfo();

            // Sự kiện thay đổi phương thức giao hàng
            rgDeliveryMethod.setOnCheckedChangeListener((group, checkedId) -> {
                if (currentAddress != null) {
                    if (checkedId == R.id.rbCOD) {
                        currentAddress.setDeliveryMethod("COD");
                        tvDeliveryFee.setText("Phí giao hàng: 30.000 VNĐ");
                    } else if (checkedId == R.id.rbStorePickup) {
                        currentAddress.setDeliveryMethod("Store Pickup");
                        tvDeliveryFee.setText("Phí giao hàng: 0 VNĐ");
                    }
                    // Cập nhật địa chỉ trong database
                    databaseHelper.getAddressDao().update(currentAddress);
                }
            });

            // Sự kiện nút cập nhật địa chỉ
            btnUpdateAddress.setOnClickListener(v -> showManageAddressesDialog());
        }
    }

    private void updateUI() {
        currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Đã đăng nhập
            layoutLoggedIn.setVisibility(View.VISIBLE);
            layoutNotLoggedIn.setVisibility(View.GONE);
        } else {
            // Chưa đăng nhập
            layoutLoggedIn.setVisibility(View.GONE);
            layoutNotLoggedIn.setVisibility(View.VISIBLE);
        }
    }

    private void loadUserInfo() {
        if (currentUser != null) {
            tvAccountInfo.setText("Tên: " + currentUser.getUsername() + "\nSố điện thoại: " + currentUser.getPhoneNumber());
        } else {
            tvAccountInfo.setText("Tên: Chưa có thông tin\nSố điện thoại: Chưa có thông tin");
        }
    }

    private void loadAddressInfo() {
        AddressDao addressDao = databaseHelper.getAddressDao();
        List<Address> addresses = addressDao.getAddressesByUser(currentUser.getId());
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
            if ("COD".equals(currentAddress.getDeliveryMethod())) {
                rgDeliveryMethod.check(R.id.rbCOD);
                tvDeliveryFee.setText("Phí giao hàng: 30.000 VNĐ");
            } else {
                rgDeliveryMethod.check(R.id.rbStorePickup);
                tvDeliveryFee.setText("Phí giao hàng: 0 VNĐ");
            }
        } else {
            tvDeliveryAddress.setText("Chưa có địa chỉ");
            rgDeliveryMethod.clearCheck();
            tvDeliveryFee.setText("Phí giao hàng: 0 VNĐ");
        }
    }

    private void loadStoreInfo() {
        tvStore.setText("Cửa hàng: Cần Thơ");
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
        Spinner spinnerDeliveryMethod = dialog.findViewById(R.id.spinnerDeliveryMethod);
        TextView tvDeliveryFeeDialog = dialog.findViewById(R.id.tvDeliveryFee);
        Button btnSaveNewAddress = dialog.findViewById(R.id.btnSaveNewAddress);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Thiết lập RecyclerView với AddressAdapter
        AddressDao addressDao = databaseHelper.getAddressDao();
        List<Address> addressList = addressDao.getAddressesByUser(currentUser.getId());
        AddressAdapter addressAdapter = new AddressAdapter(addressList, addressDao, this);
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvAddresses.setAdapter(addressAdapter);

        // Sự kiện nút thêm địa chỉ mới
        btnAddNewAddress.setOnClickListener(v -> {
            layoutNewAddress.setVisibility(View.VISIBLE);
            btnAddNewAddress.setVisibility(View.GONE);
            // Xóa dữ liệu trong form
            etReceiverName.setText("");
            etPhoneNumber.setText("");
            etAddress.setText("");
            spinnerDeliveryMethod.setSelection(0);
            tvDeliveryFeeDialog.setText("Phí giao hàng: 0 VNĐ");
            editingAddress = null; // Đặt lại trạng thái chỉnh sửa
        });

        // Sự kiện nút lưu địa chỉ
        btnSaveNewAddress.setOnClickListener(v -> {
            // Khai báo các biến cục bộ và đảm bảo chúng không thay đổi sau khi khởi tạo
            final String receiverName = etReceiverName.getText().toString().trim();
            final String phoneNumber = etPhoneNumber.getText().toString().trim();
            final String addressText = etAddress.getText().toString().trim();
            final String deliveryMethod = spinnerDeliveryMethod.getSelectedItem().toString();

            // Kiểm tra dữ liệu nhập vào
            if (receiverName.isEmpty() || phoneNumber.isEmpty() || addressText.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phoneNumber.matches("\\d{10,11}")) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật phí giao hàng dựa trên phương thức giao hàng
            final int deliveryFee = deliveryMethod.equals("COD") ? 30000 : 0;
            tvDeliveryFeeDialog.setText("Phí giao hàng: " + deliveryFee + " VNĐ");

            if (editingAddress == null) {
                // Thêm địa chỉ mới
                Address newAddress = new Address(currentUser.getId(), receiverName, phoneNumber, addressText, false, deliveryMethod);
                addressDao.insert(newAddress);
                addressList.add(newAddress);
                Toast.makeText(this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật địa chỉ hiện có
                editingAddress.setReceiverName(receiverName);
                editingAddress.setPhoneNumber(phoneNumber);
                editingAddress.setAddress(addressText);
                editingAddress.setDeliveryMethod(deliveryMethod);
                addressDao.update(editingAddress);
                addressList.set(editingPosition, editingAddress);
                Toast.makeText(this, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
                editingAddress = null; // Đặt lại trạng thái chỉnh sửa
            }

            // Cập nhật giao diện
            addressAdapter.updateAddresses(addressList);
            layoutNewAddress.setVisibility(View.GONE);
            btnAddNewAddress.setVisibility(View.VISIBLE);
        });

        // Sự kiện nút đóng
        btnClose.setOnClickListener(v -> {
            loadAddressInfo(); // Cập nhật lại địa chỉ mặc định
            dialog.dismiss();
        });

        // Thiết lập kích thước dialog
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.9); // 90% chiều rộng màn hình
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onEditAddress(Address address, int position) {
        // Hiển thị dialog với thông tin địa chỉ cần chỉnh sửa
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_manage_addresses);

        RecyclerView rvAddresses = dialog.findViewById(R.id.rvAddresses);
        Button btnAddNewAddress = dialog.findViewById(R.id.btnAddNewAddress);
        LinearLayout layoutNewAddress = dialog.findViewById(R.id.layoutNewAddress);
        EditText etReceiverName = dialog.findViewById(R.id.etReceiverName);
        EditText etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);
        EditText etAddress = dialog.findViewById(R.id.etAddress);
        Spinner spinnerDeliveryMethod = dialog.findViewById(R.id.spinnerDeliveryMethod);
        TextView tvDeliveryFeeDialog = dialog.findViewById(R.id.tvDeliveryFee);
        Button btnSaveNewAddress = dialog.findViewById(R.id.btnSaveNewAddress);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        AddressDao addressDao = databaseHelper.getAddressDao();
        List<Address> addressList = addressDao.getAddressesByUser(currentUser.getId());
        AddressAdapter addressAdapter = new AddressAdapter(addressList, addressDao, this);
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvAddresses.setAdapter(addressAdapter);

        // Hiển thị form chỉnh sửa với thông tin địa chỉ hiện tại
        layoutNewAddress.setVisibility(View.VISIBLE);
        btnAddNewAddress.setVisibility(View.GONE);

        // Điền thông tin địa chỉ vào form
        etReceiverName.setText(address.getReceiverName());
        etPhoneNumber.setText(address.getPhoneNumber());
        etAddress.setText(address.getAddress());

        // Thiết lập spinnerDeliveryMethod
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerDeliveryMethod.getAdapter();
        int spinnerPosition = adapter.getPosition(address.getDeliveryMethod());
        spinnerDeliveryMethod.setSelection(spinnerPosition);

        // Cập nhật phí giao hàng
        int deliveryFee = address.getDeliveryMethod().equals("COD") ? 30000 : 0;
        tvDeliveryFeeDialog.setText("Phí giao hàng: " + deliveryFee + " VNĐ");

        // Lưu địa chỉ đang chỉnh sửa và vị trí của nó
        editingAddress = address;
        editingPosition = position;

        // Sự kiện nút lưu địa chỉ
        btnSaveNewAddress.setOnClickListener(v -> {
            // Khai báo các biến cục bộ và đảm bảo chúng không thay đổi sau khi khởi tạo
            final String receiverName = etReceiverName.getText().toString().trim();
            final String phoneNumber = etPhoneNumber.getText().toString().trim();
            final String addressText = etAddress.getText().toString().trim();
            final String deliveryMethod = spinnerDeliveryMethod.getSelectedItem().toString();

            if (receiverName.isEmpty() || phoneNumber.isEmpty() || addressText.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phoneNumber.matches("\\d{10,11}")) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }


            // Cập nhật địa chỉ
            editingAddress.setReceiverName(receiverName);
            editingAddress.setPhoneNumber(phoneNumber);
            editingAddress.setAddress(addressText);
            editingAddress.setDeliveryMethod(deliveryMethod);
            addressDao.update(editingAddress);
            addressList.set(editingPosition, editingAddress);
            Toast.makeText(this, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();

            addressAdapter.updateAddresses(addressList);
            layoutNewAddress.setVisibility(View.GONE);
            btnAddNewAddress.setVisibility(View.VISIBLE);
        });

        // Sự kiện nút đóng
        btnClose.setOnClickListener(v -> {
            loadAddressInfo(); // Cập nhật lại địa chỉ mặc định
            dialog.dismiss();
        });

        // Thiết lập kích thước dialog
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.95); // 90% chiều rộng màn hình
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        dialog.getWindow().setAttributes(params);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        if (UserSession.getInstance().isLoggedIn()) {
            loadUserInfo();
            loadAddressInfo();
            loadStoreInfo();
        }
    }
}