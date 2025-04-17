package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.customer.ProductDetailActivity;
import com.ctut.mart4u.model.Product;

import com.ctut.mart4u.R;
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
        holder.quantity.setText(String.valueOf(product.getStockQuantity()));

        //xử lý sự kiện xem chị tiết sản phẩm => day qua trang chi tiet san pham
        holder.viewProductDetail.setOnClickListener(v -> {
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
        TextView productName; // Ví dụ: TextView để hiển thị tên sản phẩm
        TextView quantity;
        Button viewProductDetail;
        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout customer_item_shopping
            productName = itemView.findViewById(R.id.textViewItemName); // Đảm bảo ID này tồn tại trong customer_item_shopping.xml
//            quantity = itemView.findViewById(R.id.textViewItemQuantity); // Đảm bảo ID này tồn tại trong customer_item_shopping.xml
//            viewProductDetail = itemView.findViewById(R.id.btnViewProductDetail); // Đảm bảo ID này tồn tại trong customer_item_shopping.xml
        }
    }
}
