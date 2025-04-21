package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.CategoryEditActivity;
import com.ctut.mart4u.db.DatabaseHelper;
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
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        // Hiển thị hình ảnh nếu có
        if (category.getImageResourceId() != 0) {
            holder.categoryImage.setImageResource(category.getImageResourceId());
        } else {
            holder.categoryImage.setImageResource(R.drawable.ic_flag);
        }

        // Sự kiện click vào edit
        holder.btnEditCategory.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryEditActivity.class);
            intent.putExtra("categoryId", category.getId());
            context.startActivity(intent);
        });

        // Sự kiện xóa
        holder.btnDeleteCategory.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
            // Kiểm tra xem danh mục có sản phẩm liên kết không
            int productCount = databaseHelper.getProductDao().countProductsByCategoryId(category.getId());
            if (productCount > 0) {
                Toast.makeText(context, "Không thể xóa danh mục vì có " + productCount + " sản phẩm liên kết.", Toast.LENGTH_LONG).show();
                return;
            }

            // Nếu không có sản phẩm liên kết, tiến hành xóa
            databaseHelper.getCategoryDao().delete(category);
            categoryList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, categoryList.size());
            Toast.makeText(context, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        ImageButton btnEditCategory;
        ImageButton btnDeleteCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            btnEditCategory = itemView.findViewById(R.id.btnEditCategory);
            btnDeleteCategory = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }
}