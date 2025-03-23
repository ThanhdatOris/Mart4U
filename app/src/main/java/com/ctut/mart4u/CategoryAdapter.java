package com.ctut.mart4u;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private final DatabaseHelper databaseHelper;

    // Constructor
    public CategoryAdapter(List<Category> categoryList, DatabaseHelper databaseHelper) {
        this.categoryList = categoryList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textViewCategoryName.setText(category.getName());

        // Xử lý sự kiện khi nhấn nút xóa
        holder.btnDeleteCategory.setOnClickListener(v -> {
            databaseHelper.getCategoryDao().delete(category); // Xóa danh mục khỏi cơ sở dữ liệu
            categoryList.remove(position); // Xóa danh mục khỏi danh sách
            notifyItemRemoved(position); // Cập nhật RecyclerView
            notifyItemRangeChanged(position, categoryList.size()); // Cập nhật các vị trí còn lại
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // Cập nhật danh sách và làm mới RecyclerView
    public void updateList(List<Category> newList) {
        this.categoryList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder để giữ các thành phần giao diện
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName;
        Button btnDeleteCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            btnDeleteCategory = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }
}