package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

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
        holder.textViewCategoryName.setText(category.getName());

        // set click vao nut button
//        holder.btnViewProductCategory.setOnClickListener(v -> {
////            Toast.makeText(context, "Bạn đã chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
//            // TODO: Sau này mở activity product_list ở đây
//             Intent intent = new Intent(context, ShoppingListActivity.class);
//             intent.putExtra("categoryId", category.getId());
//             context.startActivity(intent);
//        });

        // set click vao chu
        holder.textViewCategoryName.setOnClickListener(v -> {
            //            Toast.makeText(context, "Bạn đã chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Sau này mở activity product_list ở đây
             Intent intent = new Intent(context, ShoppingListActivity.class);
             intent.putExtra("categoryId", category.getId());
             context.startActivity(intent);
        });
        // set click vao hinh
        holder.imageViewCategory.setOnClickListener(v -> {
            //            Toast.makeText(context, "Bạn đã chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Sau này mở activity product_list ở đây
             Intent intent = new Intent(context, ShoppingListActivity.class);
             intent.putExtra("categoryId", category.getId());
             context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends ViewHolder {
        TextView textViewCategoryName;
        Button btnViewProductCategory;
        ImageView imageViewCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
//            btnViewProductCategory = itemView.findViewById(R.id.btnViewProductCategory);
        }
    }
}
