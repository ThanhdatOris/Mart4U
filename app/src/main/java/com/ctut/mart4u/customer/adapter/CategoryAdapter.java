package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.ShoppingListActivity;
import com.ctut.mart4u.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Hiển thị tên danh mục
        holder.textViewCategoryName.setText(category.getName());

        // Hiển thị mô tả danh mục
        holder.textViewCategoryDescription.setText(category.getDescription());

        // Hiển thị hình ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
        if (category.getImagePath() != null && !category.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(category.getImagePath());
            holder.imageViewCategory.setImageURI(imageUri);
        } else {
            holder.imageViewCategory.setImageResource(R.drawable.ic_launcher_foreground); // Hình ảnh mặc định
        }

        // Gán sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShoppingListActivity.class);
            intent.putExtra("categoryId", category.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName;
        TextView textViewCategoryDescription;
        ImageView imageViewCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            textViewCategoryDescription = itemView.findViewById(R.id.textViewCategoryDescription);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
        }
    }
}