package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctut.mart4u.R;
import com.ctut.mart4u.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private final Context context;
    private final List<Product> originalProductList;
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.originalProductList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("%,.2fđ", product.getPrice())); // Use %.2f for double/float

        // Handle item click (optional)
        holder.itemView.setOnClickListener(v -> {
            // Add your click handling logic here
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void filter(String keyword) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : originalProductList) { // Danh sách gốc chưa lọc
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productList.clear();
        productList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProductImage;
        private final TextView tvProductName;
        private final TextView tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}