package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
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
                .inflate(R.layout.customer_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textViewCategoryName.setText(category.getName());

        // Gán hình ảnh dựa trên tên danh mục
        String categoryName = category.getName().toLowerCase();
        if (categoryName.contains("rau củ")) {
            holder.imageViewCategory.setImageResource(R.drawable.img_category_vegetable);
        } else if (categoryName.contains("thịt")) {
            holder.imageViewCategory.setImageResource(R.drawable.img_category_meat);
        } else if (categoryName.contains("trứng")) {
            holder.imageViewCategory.setImageResource(R.drawable.img_category_egg);
        } else {
            holder.imageViewCategory.setImageResource(R.drawable.img_category_vegetable); // Mặc định
        }

//        // Xử lý sự kiện khi nhấn nút xóa
//        holder.btnDeleteCategory.setOnClickListener(v -> {
//            databaseHelper.getCategoryDao().delete(category); // Xóa danh mục khỏi cơ sở dữ liệu
//            categoryList.remove(position); // Xóa danh mục khỏi danh sách
//            notifyItemRemoved(position); // Cập nhật RecyclerView
//            notifyItemRangeChanged(position, categoryList.size()); // Cập nhật các vị trí còn lại
//        });
        // xem danh sach san pham cua 1 category
        holder.btnViewProductCategory.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ShoppingListActivity.class);
            intent.putExtra("categoryId", category.getId());
            context.startActivity(intent);
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
        ImageView imageViewCategory;
//        Button btnDeleteCategory;
        Button btnViewProductCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
//            btnDeleteCategory = itemView.findViewById(R.id.btnDeleteCategory);
            btnViewProductCategory = itemView.findViewById(R.id.btnViewProductCategory);
        }
    }
}