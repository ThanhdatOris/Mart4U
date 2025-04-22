package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.ProductDetailActivity;
import com.ctut.mart4u.model.Product;

import java.io.IOException;
import java.io.InputStream;
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

        // Hien thi img voi bitmap tu imagePath
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = ((AssetManager) assetManager).open(product.getImagePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        holder.ivProductImage.setImageBitmap(bitmap);

        // xu ly event onClick khi nhấn vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
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