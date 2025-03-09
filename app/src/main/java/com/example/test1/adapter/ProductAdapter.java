package com.example.test1.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.example.test1.ProductDetailActivity;
import com.example.test1.R;
import com.example.test1.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static final String TAG = "ProductAdapter";
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        Log.d(TAG, "ProductAdapter initialized with " + (productList != null ? productList.size() : "null") + " items");
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        Log.d(TAG, "Creating ViewHolder for viewType: " + viewType);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Product product = productList.get(position);
            if (product != null) {
                holder.textProductName.setText(product.getProductName());
                holder.textPrice.setText(String.format("$%.2f", product.getUnitPrice()));
                try {
                    holder.imageProduct.setImageResource(product.getImageResId());
                    Log.d(TAG, "Image set for product: " + product.getProductName());
                } catch (Exception e) {
                    Log.e(TAG, "Failed to set image for product: " + product.getProductName(), e);
                    holder.imageProduct.setImageResource(android.R.drawable.ic_menu_help); // Fallback image
                }

                // Set click listener to navigate to ProductDetailActivity
                holder.itemView.setOnClickListener(v -> {
                    Log.d(TAG, "Product clicked at position " + position + ", ID: " + product.getProductId());
                    if (product.getProductId() > 0) {
                        Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getProductId());
                        try {
                            v.getContext().startActivity(intent);
                            Log.d(TAG, "Navigating to ProductDetailActivity for product ID: " + product.getProductId());
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to start ProductDetailActivity: " + e.getMessage(), e);
                        }
                    } else {
                        Log.e(TAG, "Invalid product ID: " + product.getProductId());
                    }
                });
            } else {
                Log.e(TAG, "Product at position " + position + " is null, skipping rendering");
                holder.textProductName.setText("No Product");
                holder.textPrice.setText("N/A");
                holder.imageProduct.setImageResource(android.R.drawable.ic_menu_help);
            }
        } else {
            Log.e(TAG, "Invalid position or null productList at position: " + position);
            holder.textProductName.setText("Error");
            holder.textPrice.setText("N/A");
            holder.imageProduct.setImageResource(android.R.drawable.ic_menu_help);
        }
    }

    @Override
    public int getItemCount() {
        int count = productList != null ? productList.size() : 0;
        Log.d(TAG, "getItemCount returning: " + count);
        return count;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textPrice;
        ImageView imageProduct;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.productName);
            textPrice = itemView.findViewById(R.id.productPrice);
            imageProduct = itemView.findViewById(R.id.productImage);
            Log.d("ProductViewHolder", "ViewHolder created for item");
            itemView.setClickable(true);
            itemView.setFocusable(true);
        }
    }
}