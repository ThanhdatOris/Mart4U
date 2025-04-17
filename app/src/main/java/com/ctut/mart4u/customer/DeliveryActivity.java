package com.ctut.mart4u.customer;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.R;
import com.ctut.mart4u.model.DeliverySchedule;

import java.util.List;

public class DeliveryActivity extends BaseActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.customer_delivery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(this);

        // Lấy dữ liệu từ database và hiển thị
        loadDeliverySchedule();
    }

    private void loadDeliverySchedule() {
        // Lấy instance của database
        List<DeliverySchedule> schedules = databaseHelper.getInstance(this)
                .getDeliveryScheduleDao()
                .getAllSchedules();

        // Tìm TableLayout
        TableLayout tableLayout = findViewById(R.id.table_schedule);
        if (tableLayout == null) {
            return; // Nếu không tìm thấy TableLayout, thoát
        }

        // Xóa các dòng hiện có (trừ tiêu đề)
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        // Thêm dữ liệu từ database vào TableLayout
        for (DeliverySchedule schedule : schedules) {
            TableRow row = new TableRow(this);

            // Cột 1: Time Slot
            TextView timeSlot = new TextView(this);
            timeSlot.setText(schedule.timeSlot);
            timeSlot.setPadding(8, 8, 8, 8);
            row.addView(timeSlot);

            // Cột 2: Today Status
            TextView todayStatus = new TextView(this);
            todayStatus.setText(schedule.todayStatus);
            todayStatus.setGravity(android.view.Gravity.CENTER);
            todayStatus.setTextColor(schedule.todayStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            todayStatus.setPadding(8, 8, 8, 8);
            row.addView(todayStatus);

            // Cột 3: Tomorrow Status
            TextView tomorrowStatus = new TextView(this);
            tomorrowStatus.setText(schedule.tomorrowStatus);
            tomorrowStatus.setGravity(android.view.Gravity.CENTER);
            tomorrowStatus.setTextColor(schedule.tomorrowStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            tomorrowStatus.setPadding(8, 8, 8, 8);
            row.addView(tomorrowStatus);

            // Cột 4: Day After Tomorrow Status
            TextView dayAfterTomorrowStatus = new TextView(this);
            dayAfterTomorrowStatus.setText(schedule.dayAfterTomorrowStatus);
            dayAfterTomorrowStatus.setGravity(android.view.Gravity.CENTER);
            dayAfterTomorrowStatus.setTextColor(schedule.dayAfterTomorrowStatus.equals("Mở") ? 0xFF00FF00 : 0xFFFF0000);
            dayAfterTomorrowStatus.setPadding(8, 8, 8, 8);
            row.addView(dayAfterTomorrowStatus);

            // Thêm dòng vào TableLayout
            tableLayout.addView(row);
        }
    }
}