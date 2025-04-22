package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.customer.ProductDetailActivity;
import com.ctut.mart4u.db.DatabaseHelper;
import com.ctut.mart4u.model.CartDetail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CartEntryAdapter extends RecyclerView.Adapter<CartEntryAdapter.CartEntryViewHolder> {
    private Context context;
    private List<CartDetail> cartList;
    private OnItemRemovedListener onItemRemovedListener;

    // Callback interface để thông báo khi một sản phẩm bị xóa
    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    public CartEntryAdapter(Context context, List<CartDetail> cartList, OnItemRemovedListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.onItemRemovedListener = listener;
    }

    @NonNull
    @Override
    public CartEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.customer_item_cart, null);
        return new CartEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartEntryAdapter.CartEntryViewHolder holder, int position) {
        CartDetail cartDetail = cartList.get(position);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        String productName = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getName();
        String productPrice = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getPrice() + " VND";
        String productImagePath = databaseHelper.getProductDao().getProductById(cartDetail.getProductId()).getImagePath();

        holder.textViewItemName.setText(productName);
        holder.textViewPrice.setText(productPrice);
        holder.textViewQuantity.setText("Số lượng: " + cartDetail.getQuantity());

        // Hiển thị hình ảnh với bitmap từ imagePath
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(productImagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.imageViewProduct.setImageBitmap(bitmap);
        } catch (IOException e) {
            holder.imageViewProduct.setImageResource(R.drawable.ic_launcher_foreground); // Hình ảnh mặc định nếu lỗi
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Xử lý sự kiện onClick khi nhấn vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", cartDetail.getProductId());
            context.startActivity(intent);
        });

        // Sự kiện xóa một sản phẩm
        holder.btnDeleteItem.setOnClickListener(v -> {
            databaseHelper.getCartDetailDao().delete(cartDetail);
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();

            // Thông báo cho CartActivity để tính lại tổng giá
            if (onItemRemovedListener != null) {
                onItemRemovedListener.onItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartEntryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewPrice;
        TextView textViewQuantity;
        TextView textViewAddedDate;
        Button btnDeleteItem;
        ImageView imageViewProduct;

        public CartEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewAddedDate = itemView.findViewById(R.id.textViewAddedDate);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}