package com.ctut.mart4u.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ctut.mart4u.model.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);
    @Update
    void update(Product product);
    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%'")
    List<Product> searchProducts(String keyword);
    @Query("DELETE FROM products WHERE id = :productId")
    void deleteProductById(int productId);
    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);


    @Query("SELECT * FROM products WHERE id = :productId")
    Product getProductById(int productId);
    @Query("UPDATE products SET stockQuantity = stockQuantity - :quantity WHERE id = :productId AND stockQuantity >= :quantity")
    int reduceProductQuantity(int productId, int quantity);
}