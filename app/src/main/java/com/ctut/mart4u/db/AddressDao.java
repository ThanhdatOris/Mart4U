package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.ctut.mart4u.model.Address;
import java.util.List;

@Dao
public interface AddressDao {
    @Insert
    void insert(Address address);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    List<Address> getAddressesByUser(int userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    Address getDefaultAddress(int userId);

}