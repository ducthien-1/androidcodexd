package com.example.test1;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import com.example.test1.adapter.ProductAdapter;
import com.example.test1.dao.ProductDAO;
import com.example.test1.entity.Product;
import com.example.test1.manager.SessionManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> fullProductList;
    private ProductDAO productDAO;
    private TextView cartBadge;
    private ImageButton backBtn, profileButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            Log.e(TAG, "Toolbar not found in layout");
            Toast.makeText(this, "Toolbar not found", Toast.LENGTH_SHORT).show();
        } else {
            setSupportActionBar(toolbar);
            Log.d(TAG, "Toolbar set as ActionBar");
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found");
            Toast.makeText(this, "RecyclerView not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Product DAO and data
        productDAO = new ProductDAO(this);
        productDAO.insertSampleProducts(this);
        fullProductList = productDAO.getAllProducts();
        if (fullProductList == null) {
            fullProductList = new ArrayList<>();
        }
        productList = new ArrayList<>(fullProductList);
        sessionManager = new SessionManager(this );
        if (sessionManager == null) {
            Log.e(TAG, "Failed to initialize SessionManager");
            Toast.makeText(this, "Error initializing session", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Setup RecyclerView
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(productAdapter);

        // Initialize SearchView
        SearchView searchView = findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterProducts(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterProducts(newText);
                    return true;
                }
            });
        }

        // Initialize Back Button
        backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> goBackToCategories(v));
        }

//         Initialize Cart components
        cartBadge = findViewById(R.id.cartBadge);
        ImageButton cartButton = findViewById(R.id.cartButton);
        if (cartButton != null) {
            cartButton.setOnClickListener(v -> {
                startActivity(new Intent(this, ShoppingCartActivity.class));
            });
        }
        updateCartCount();
        profileButton = findViewById(R.id.profileButton);
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                if (sessionManager != null && sessionManager.isLoggedIn()) {
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    showLoggedInDialog();
                }
            });
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.d(TAG, "Menu item selected: " + menuItem.getTitle()); // Thêm log để kiểm tra
        if (menuItem.getItemId() == R.id.menu_user_profile) {
            showEditUserProfile();
        } else if (menuItem.getItemId() == R.id.menu_services) {
            showServices();
        } else if (menuItem.getItemId() == R.id.menu_setting) {
            Toast.makeText(this, "Setting menu selected", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.menu_favourite) {
            Toast.makeText(this, "Favourite menu selected", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.menu_req_gps) {
            requestGPS();
        } else if (menuItem.getItemId() == R.id.menu_send_notification) {
            sendNotification();
        } else if (menuItem.getItemId() == R.id.menu_logout) {
            Toast.makeText(this, "Logout menu selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.d(TAG, "Menu inflated with " + menu.size() + " items");
        return true;
    }

    // Các phương thức giả định
    private void showEditUserProfile() {
        Toast.makeText(this, "Edit User Profile selected", Toast.LENGTH_SHORT).show();
    }

    private void showServices() {
        Toast.makeText(this, "Services selected", Toast.LENGTH_SHORT).show();
    }

    private void requestGPS() {
        Toast.makeText(this, "Request GPS selected", Toast.LENGTH_SHORT).show();
    }

    private void sendNotification() {
        Toast.makeText(this, "Send Notification selected", Toast.LENGTH_SHORT).show();
    }

    private void filterProducts(String query) {
        if (fullProductList == null || fullProductList.isEmpty()) {
            productList.clear();
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
    }

    public void updateCartCount() {
        int count = ShoppingCartManager.getInstance().getCartItemCount();
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(count));
            cartBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        }
    }
    public void  showLoggedInDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_not_logged_in);
        Button btnLogin = dialog.findViewById(R.id.btn_login_dialog);
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            dialog.dismiss();
        });
        dialog.show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    public void goBackToCategories(View view) {
        startActivity(new Intent(this, CategoriesActivity.class));
        finish();
    }
}