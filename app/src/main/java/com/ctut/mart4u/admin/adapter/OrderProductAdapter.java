package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctut.mart4u.R;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.model.PurchaseDetail;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    private Context context;
    private List<PurchaseDetail> productList;
    private DatabaseHelper databaseHelper;

    public OrderProductAdapter(Context context, List<PurchaseDetail> productList) {
        this.context = context;
        this.productList = productList;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_order_product_item, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        PurchaseDetail detail = productList.get(position);

        // Lấy thông tin sản phẩm từ database
        Product product = databaseHelper.getProductDao().getProductById(detail.getProductId());
        if (product != null) {
            // Hiển thị tên sản phẩm
            holder.tvProductName.setText("Tên sản phẩm: " + product.getName());

            // Hiển thị hình ảnh sản phẩm từ thư mục assets
            if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
                // Tạo đường dẫn đầy đủ cho assets
                String assetPath = "file:///android_asset/" + product.getImagePath();
                Glide.with(context)
                        .load(assetPath)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.ivProductImage);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground); // Hình ảnh mặc định
            }
        } else {
            holder.tvProductName.setText("Tên sản phẩm: Không tìm thấy");
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Hiển thị số lượng và đơn giá
        holder.tvQuantity.setText("Số lượng: " + detail.getQuantity());
        holder.tvUnitPrice.setText("Đơn giá: " + detail.getUnitPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvQuantity, tvUnitPrice;

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
        }
    }
}