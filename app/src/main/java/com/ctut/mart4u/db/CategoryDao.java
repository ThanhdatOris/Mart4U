package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ctut.mart4u.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Query("DELETE FROM categories")
    void deleteAllCategories();
}