package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.ctut.mart4u.model.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE role = 'customer'")
    List<User> getAllCustomers();

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
}