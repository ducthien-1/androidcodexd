package com.example.test1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.dtb.DatabaseHelper;
import com.example.test1.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ProductManagementViewHolder> {
    private static final String TAG = "ProductManagementAdapter";
    private List<Product> productList;
    private Context context;
    private List<Integer> selectedProductIds = new ArrayList<>(); // Danh sách các productId được chọn
    private DatabaseHelper dbHelper;

    public ProductManagementAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        Log.d(TAG, "ProductManagementAdapter initialized with " + (productList != null ? productList.size() : "null") + " items");
    }

    @NonNull
    @Override
    public ProductManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_management, parent, false);
        Log.d(TAG, "Creating ViewHolder for viewType: " + viewType);
        return new ProductManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManagementViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Product product = productList.get(position);
            if (product != null) {
                holder.tvProductName.setText(product.getProductName());
                holder.tvProductPrice.setText(String.format("$%.2f", product.getUnitPrice()));
                holder.tvProductQuantity.setText("Qty: " + product.getUnitQuantity());
                holder.tvProductSales.setText("Sales: " + product.getSales());

                // Hiển thị hình ảnh sản phẩm
                try {
                    if (product.getImageResId() != 0) {
                        holder.ivProductImage.setImageResource(product.getImageResId());
                        Log.d(TAG, "Image set for product: " + product.getProductName());
                    } else {
                        holder.ivProductImage.setImageResource(android.R.drawable.ic_menu_gallery); // Hình ảnh mặc định
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to set image for product: " + product.getProductName(), e);
                    holder.ivProductImage.setImageResource(android.R.drawable.ic_menu_help); // Fallback image
                }

                // Xử lý CheckBox để chọn sản phẩm
                holder.cbSelectProduct.setChecked(selectedProductIds.contains(product.getProductId()));
                holder.cbSelectProduct.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (!selectedProductIds.contains(product.getProductId())) {
                            selectedProductIds.add(product.getProductId());
                        }
                    } else {
                        selectedProductIds.remove(Integer.valueOf(product.getProductId()));
                    }
                    Log.d(TAG, "Selected product IDs: " + selectedProductIds);
                });

                // Xử lý nút xóa (nếu có)
                holder.ivDeleteProduct.setOnClickListener(v -> {
                    int productIdToDelete = product.getProductId();
                    if (dbHelper.deleteProduct(productIdToDelete)) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                        Log.d(TAG, "Product deleted with ID: " + productIdToDelete);
                    } else {
                        Log.e(TAG, "Failed to delete product with ID: " + productIdToDelete);
                    }
                });
            } else {
                Log.e(TAG, "Product at position " + position + " is null, skipping rendering");
                holder.tvProductName.setText("No Product");
                holder.tvProductPrice.setText("N/A");
                holder.tvProductQuantity.setText("N/A");
                holder.tvProductSales.setText("N/A");
                holder.ivProductImage.setImageResource(android.R.drawable.ic_menu_help);
            }
        } else {
            Log.e(TAG, "Invalid position or null productList at position: " + position);
            holder.tvProductName.setText("Error");
            holder.tvProductPrice.setText("N/A");
            holder.tvProductQuantity.setText("N/A");
            holder.tvProductSales.setText("N/A");
            holder.ivProductImage.setImageResource(android.R.drawable.ic_menu_help);
        }
    }

    @Override
    public int getItemCount() {
        int count = productList != null ? productList.size() : 0;
        Log.d(TAG, "getItemCount returning: " + count);
        return count;
    }

    // Xóa các sản phẩm được chọn
    public void deleteSelectedProducts() {
        if (selectedProductIds.isEmpty()) {
            Log.d(TAG, "No products selected for deletion");
            return;
        }

        List<Product> updatedList = new ArrayList<>(productList);
        for (Integer productId : selectedProductIds) {
            // Xóa từ database
            if (dbHelper.deleteProduct(productId)) {
                // Xóa từ danh sách hiển thị
                updatedList.removeIf(product -> product.getProductId() == productId);
                Log.d(TAG, "Product deleted with ID: " + productId);
            } else {
                Log.e(TAG, "Failed to delete product with ID: " + productId);
            }
        }
        productList.clear();
        productList.addAll(updatedList);
        selectedProductIds.clear();
        notifyDataSetChanged();
        Log.d(TAG, "Deleted selected products. New list size: " + productList.size());
    }

    // Lấy danh sách các productId được chọn
    public List<Integer> getSelectedProductIds() {
        return new ArrayList<>(selectedProductIds);
    }

    public static class ProductManagementViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductSales;
        ImageView ivProductImage, ivDeleteProduct;
        CheckBox cbSelectProduct;

        public ProductManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductSales = itemView.findViewById(R.id.tvProductSales);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            cbSelectProduct = itemView.findViewById(R.id.cbSelectProduct);
            ivDeleteProduct = itemView.findViewById(R.id.ivDeleteProduct);
            Log.d("ProductManagementViewHolder", "ViewHolder created for item");
            itemView.setClickable(true);
            itemView.setFocusable(true);
        }
    }
}