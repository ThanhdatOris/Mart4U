package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctut.mart4u.customer.ProductDetailActivity;
import com.ctut.mart4u.model.Product;

import com.ctut.mart4u.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ShoppingListAdapter  extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private Context context;
    private List<Product> productList;

    public ShoppingListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout của item (customer_item_shopping)
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_shopping, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        // Lấy sản phẩm tại vị trí position
        Product product = productList.get(position);
        // Gán tên sản phẩm vào TextView
        holder.productName.setText(product.getName());
        // Gán số lượng sản phẩm vào TextView
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        // Gán hình ảnh sản phẩm vào ImageView

        // Lấy hình ảnh từ assets

//        AssetManager assetManager = context.getAssets();
//        InputStream is = null;
//        try {
//            is = ((AssetManager) assetManager).open(product.getImagePath());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        holder.productImage.setImageBitmap(bitmap);

        // Hiển thị hình ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            Glide.with(context)
                    .load(product.getImagePath()) // Glide tự động xử lý cả assets và URL
                    .placeholder(R.drawable.ic_flag) // Ảnh mặc định khi đang tải
                    .error(R.drawable.ic_flag) // Ảnh mặc định nếu lỗi
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_flag); // Ảnh mặc định
        }

        //xử lý sự kiện xem chị tiết sản phẩm => day qua trang chi tiet san pham
        holder.addToCartIcon.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng sản phẩm trong danh sách
        return productList != null ? productList.size() : 0;
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các view trong item layout
//        TextView productName; // Ví dụ: TextView để hiển thị tên sản phẩm
//        TextView quantity;
//        Button viewProductDetail;

        TextView productName;
        TextView productPrice;
        ImageView addToCartIcon;
        ImageView productImage;
        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout customer_item_shopping
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartIcon = itemView.findViewById(R.id.addToCartIcon);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
