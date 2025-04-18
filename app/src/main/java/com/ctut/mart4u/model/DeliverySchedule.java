package com.ctut.mart4u.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "delivery_schedule")
public class DeliverySchedule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String timeSlot; // Ví dụ: "9:00 - 11:00"
    public String todayStatus; // Trạng thái hôm nay: "Mở" hoặc "Đóng"
    public String tomorrowStatus; // Trạng thái ngày mai
    public String dayAfterTomorrowStatus; // Trạng thái ngày kia

    public DeliverySchedule(String timeSlot, String todayStatus, String tomorrowStatus, String dayAfterTomorrowStatus) {
        this.timeSlot = timeSlot;
        this.todayStatus = todayStatus;
        this.tomorrowStatus = tomorrowStatus;
        this.dayAfterTomorrowStatus = dayAfterTomorrowStatus;
    }
}