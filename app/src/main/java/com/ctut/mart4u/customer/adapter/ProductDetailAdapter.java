package com.ctut.mart4u.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctut.mart4u.model.Product;
import com.ctut.mart4u.R;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHoler> {
    private Product product;
    private Context context;

    // Constructor
    public ProductDetailAdapter(Product product, Context context) {
        this.product = product;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductDetailViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_activity_product_detail, parent, false);
       return new ProductDetailViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailAdapter.ProductDetailViewHoler holder, int position) {

//      //chuc nang cong, tru so luong san pham dat hang
//        holder.btnIncreaseQuantity.setOnClickListener(v -> {
//            String quantityText = holder.editTextQuantity.getText().toString();
//            int quantity = Integer.parseInt(quantityText);
//            Toast.makeText(context, "Số lượng sản phẩm đã đạt tối đa", Toast.LENGTH_SHORT).show();
//        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ProductDetailViewHoler extends RecyclerView.ViewHolder {
//        Button btnIncreaseQuantity; // buttonIncreaseQuantity
//
//        Button btnDecreaseQuantity; // buttonDecreaseQuantity
//
//        EditText editTextQuantity; // editTextQuantity


        public ProductDetailViewHoler(@NonNull View itemView) {
            super(itemView);
//            btnIncreaseQuantity = itemView.findViewById(R.id.buttonIncreaseQuantity);
//            btnDecreaseQuantity = itemView.findViewById(R.id.buttonDecreaseQuantity);
//            editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
        }
    }
}
