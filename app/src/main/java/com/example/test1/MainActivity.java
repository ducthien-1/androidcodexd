package com.example.test1;

import android.content.Intent;
import android.util.Log;
import android.view.View;  // Add this import for View.VISIBLE and View.GONE
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import com.example.test1.adapter.ProductAdapter;
import com.example.test1.dao.ProductDAO;
import com.example.test1.entity.Product;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> fullProductList; // To store all products for filtering
    private ProductDAO productDAO;
    private TextView cartBadge;

    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate called");

        recyclerView = findViewById(R.id.recyclerViewProducts);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found in activity_main.xml");
            Toast.makeText(this, "RecyclerView not found", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d(TAG, "RecyclerView initialized successfully");
        }

        productDAO = new ProductDAO(this);
        productDAO.insertSampleProducts(this);

        fullProductList = productDAO.getAllProducts();
        if (fullProductList == null) {
            fullProductList = new ArrayList<>();
        }
        productList = new ArrayList<>(fullProductList);

        Log.d(TAG, "Full Product List Size: " + (fullProductList != null ? fullProductList.size() : "null"));
        Log.d(TAG, "Product List Size: " + (productList != null ? productList.size() : "null"));

        if (productList.isEmpty()) {
            Log.w(TAG, "Product List is empty after inserting samples, adding manual data");
            productList.add(new Product(1, 1, "Sample Product", "Description", 99.99, 100, true, R.drawable.product1, 50));
            productList.add(new Product(2, 1, "Another Product", "Description", 149.99, 50, false, R.drawable.product2, 30));
            fullProductList.addAll(productList);
            Log.d(TAG, "Added manual sample data, Product List Size: " + productList.size());
        }

        productAdapter = new ProductAdapter(productList);
        if (productAdapter == null) {
            Log.e(TAG, "ProductAdapter is null");
        } else {
            Log.d(TAG, "ProductAdapter initialized with " + productList.size() + " items");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Log.d(TAG, "GridLayoutManager set with 3 columns");
        recyclerView.setAdapter(productAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "Search query submitted: " + query);
                    filterProducts(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "Search query changing: " + newText);
                    filterProducts(newText);
                    return true;
                }
            });
            Log.d(TAG, "SearchView initialized successfully");
        } else {
            Log.e(TAG, "SearchView not found in activity_main.xml");
        }

        cartBadge = findViewById(R.id.cartBadge);
        if (cartBadge == null) {
            Log.e(TAG, "Cart badge not found in activity_main.xml");
        } else {
            updateCartCount();
        }

        ImageButton cartButton = findViewById(R.id.cartButton);
        if (cartButton != null) {
            cartButton.setOnClickListener(v -> {
                Log.d(TAG, "Navigating to ShoppingCartActivity");
                startActivity(new Intent(this, ShoppingCartActivity.class));
            });
        } else {
            Log.e(TAG, "Cart button not found in activity_main.xml");
        }

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked, navigating back");
            onBackPressed(); // Call the default back press behavior
        });

    }

    private void filterProducts(String query) {
        Log.d(TAG, "Filtering products with query: " + query);
        if (fullProductList == null || fullProductList.isEmpty()) {
            Log.w(TAG, "Full product list is empty or null, cannot filter");
            productList.clear();
            productList.addAll(new ArrayList<>());
            productAdapter.notifyDataSetChanged();
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : fullProductList) {
            if (product != null && product.getProductName() != null &&
                    product.getProductName().toLowerCase().contains(query.toLowerCase().trim())) {
                filteredList.add(product);
            }
        }

        productList.clear();
        productList.addAll(filteredList);
        productAdapter.notifyDataSetChanged();
        Log.d(TAG, "Filtered product list size: " + filteredList.size());
    }

    public void updateCartCount() {
        int count = ShoppingCartManager.getInstance().getCartItemCount();
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(count));
            cartBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
            Log.d(TAG, "Cart count updated to: " + count);
        } else {
            Log.w(TAG, "Cart badge is null, cannot update cart count");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        Log.d(TAG, "MainActivity resumed, cart count updated");
    }
}
