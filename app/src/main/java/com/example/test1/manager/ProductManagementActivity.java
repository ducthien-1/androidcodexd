package com.example.test1.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.adapter.ProductManagementAdapter;
import com.example.test1.dtb.DatabaseHelper;
import com.example.test1.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductManagementAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo productList
        productList = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);
        loadProducts();

        // Thiết lập RecyclerView
        RecyclerView recyclerViewProductList = findViewById(R.id.recyclerViewProductList);
        recyclerViewProductList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductManagementAdapter(productList, this); // Sử dụng ProductManagementAdapter
        recyclerViewProductList.setAdapter(adapter);

        TextView tvDelete = findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(v -> {
            if (adapter.getSelectedProductIds().isEmpty()) {
                Toast.makeText(this, "Please select at least one product to delete", Toast.LENGTH_SHORT).show();
            } else {
                adapter.deleteSelectedProducts();
                Toast.makeText(this, "Selected products deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProducts() {
        // Kiểm tra null và khởi tạo nếu cần
        if (productList == null) {
            productList = new ArrayList<>();
        } else {
            productList.clear();
        }

        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("productId"));
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("productName"));
                @SuppressLint("Range") String productDescription = cursor.getString(cursor.getColumnIndex("productDescription"));
                @SuppressLint("Range") double unitPrice = cursor.getDouble(cursor.getColumnIndex("unitPrice"));
                @SuppressLint("Range") int unitQuantity = cursor.getInt(cursor.getColumnIndex("unitQuantity"));
                @SuppressLint("Range") boolean isFeatured = cursor.getInt(cursor.getColumnIndex("isFeatured")) == 1;
                @SuppressLint("Range") int imageResId = cursor.getInt(cursor.getColumnIndex("imageResId"));
                @SuppressLint("Range") int sales = cursor.getInt(cursor.getColumnIndex("sales"));

                Product product = new Product(productId, categoryId, productName, productDescription, unitPrice, unitQuantity, isFeatured, imageResId, sales);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}