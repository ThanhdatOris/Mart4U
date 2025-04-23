package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PurchaseDetailAdapter extends RecyclerView.Adapter<PurchaseDetailAdapter.PurchaseDetailViewHolder> {
    private Context context;
    private List<PurchaseDetail> purchaseDetailList;
    //contructor
    public PurchaseDetailAdapter(Context context, List<PurchaseDetail> purchaseDetailList) {
        this.context = context;
        this.purchaseDetailList = purchaseDetailList;
    }
    @NonNull
    @Override
    public PurchaseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail_product, parent, false);
        return new PurchaseDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseDetailAdapter.PurchaseDetailViewHolder holder, int position) {
        PurchaseDetail purchaseDetail = purchaseDetailList.get(position);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        Product product = databaseHelper.getProductDao().getProductById(purchaseDetail.getProductId());
        holder.tvProductName.setText("Tên sản phẩm: " + product.getName());
        holder.tvProductQuantity.setText("Số lượng: " + String.valueOf(purchaseDetail.getQuantity()));
        holder.tvProductPrice.setText("Tổng giá: " + String.valueOf(purchaseDetail.getUnitPrice() * purchaseDetail.getQuantity()) + " VNĐ");

//        AssetManager assetManager = context.getAssets();
//        InputStream is = null;
//        try {
//            is = ((AssetManager) assetManager).open(product.getImagePath());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        holder.ivProductImage.setImageBitmap(bitmap);

        // Hiển thị hình ảnh từ imagePath nếu có, nếu không thì dùng ảnh mặc định
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            Glide.with(context)
                    .load(product.getImagePath()) // Glide tự động xử lý cả assets và URL
                    .placeholder(R.drawable.ic_flag) // Ảnh mặc định khi đang tải
                    .error(R.drawable.ic_flag) // Ảnh mặc định nếu lỗi
                    .into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_flag); // Ảnh mặc định
        }

    }

    @Override
    public int getItemCount() {
        return purchaseDetailList != null ? purchaseDetailList.size() : 0;
    }

    public class PurchaseDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductQuantity;
        TextView tvProductPrice;

        ImageView ivProductImage;

        public PurchaseDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);

        }
    }
}
