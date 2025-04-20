package com.ctut.mart4u.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.R;
import com.ctut.mart4u.admin.ProductEditActivity;
import com.ctut.mart4u.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    //contructor
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
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = ((AssetManager) assetManager).open(product.getImagePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        holder.productImage.setImageBitmap(bitmap);

        // sự kiện click vào edit
        holder.btnEditProduct.setOnClickListener(v -> {

            // mo productEidtActivity
            Intent intent = new Intent(context, ProductEditActivity.class);
            intent.putExtra("productId", product.getId());

            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        ImageButton btnEditProduct;
        ImageButton btnDeleteProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);

        }
    }
}
