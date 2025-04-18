package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.ctut.mart4u.model.DeliverySchedule;

import java.util.List;

@Dao
public interface DeliveryScheduleDao {
    @Insert
    void insert(DeliverySchedule schedule);

    @Query("SELECT * FROM delivery_schedule")
    List<DeliverySchedule> getAllSchedules();
}