package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ctut.mart4u.model.HistoryItem;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insert(HistoryItem historyItem);

    @Delete
    void delete(HistoryItem historyItem);

    @Query("SELECT * FROM history_items")
    List<HistoryItem> getAllHistoryItems();

    @Query("DELETE FROM history_items")
    void deleteAllHistoryItems();
}