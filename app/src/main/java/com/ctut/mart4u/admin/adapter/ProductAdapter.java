package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.ProductEditActivity;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Product;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_product_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf((int) product.getPrice()));
//        holder.productStock.setText(String.valueOf(product.getStockQuantity()));
//        // Hiển thị hình ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
//        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
//            Log.d("ProductAdapter", "Tải hình ảnh cho productId=" + product.getId() + ", imagePath=" + product.getImagePath());
//            try {
//                // imagePath giờ là đường dẫn file thực tế trong bộ nhớ nội bộ
//                Uri imageUri = Uri.fromFile(new File(product.getImagePath()));
//                holder.productImage.setImageURI(imageUri);
//            } catch (Exception e) {
//                Log.e("ProductAdapter", "Lỗi khi hiển thị Uri cho productId=" + product.getId() + ": " + e.getMessage());
//                holder.productImage.setImageResource(R.drawable.ic_flag);
//            }
//        } else {
//            Log.d("ProductAdapter", "imagePath là null hoặc rỗng cho productId=" + product.getId());
//            holder.productImage.setImageResource(R.drawable.ic_flag); // Ảnh mặc định
//        }

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

        // Sự kiện click vào edit
        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductEditActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

        // Sự kiện xóa
        holder.btnDeleteProduct.setOnClickListener(v -> {
            // Create an alert dialog to confirm deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?");

            // Add the positive button (Yes)
            builder.setPositiveButton("Có", (dialog, which) -> {
                // Execute deletion when confirmed
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
                product.setDeleted(true);
                databaseHelper.getProductDao().update(product);
                productList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productList.size());
                Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
            });

            // Add the negative button (No)
            builder.setNegativeButton("Không", (dialog, which) -> {
                // User cancelled the deletion, just dismiss the dialog
                dialog.dismiss();
            });

            // Show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
//        TextView productStock;
        ImageButton btnEditProduct;
        ImageButton btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
//            productStock = itemView.findViewById(R.id.etProductStock);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}