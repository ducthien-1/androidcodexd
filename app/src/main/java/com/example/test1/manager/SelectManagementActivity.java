package com.example.test1.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.R;

public class SelectManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up button click listeners
        Button btnProductManagement = findViewById(R.id.btn_product_management);
        Button btnUserManagement = findViewById(R.id.btn_user_management);

        btnProductManagement.setOnClickListener(v -> {
            startActivity(new Intent(this, ProductManagementActivity.class));
        });

        btnUserManagement.setOnClickListener(v -> {
            startActivity(new Intent(this, UserManagementActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_user_profile) {// Handle Edit User Profile click
            System.out.println("Edit User Profile clicked");
            return true;
        } else if (itemId == R.id.menu_services) {// Handle Services click
            System.out.println("Services clicked");
            return true;
        } else if (itemId == R.id.menu_setting) {// Handle Setting click
            System.out.println("Setting clicked");
            return true;
        } else if (itemId == R.id.menu_management) {// Handle Management click
            System.out.println("Management clicked");
            return true;
        } else if (itemId == R.id.menu_req_gps) {// Handle Request GPS click
            System.out.println("Request GPS clicked");
            return true;
        } else if (itemId == R.id.menu_send_notification) {// Handle Send Notification click
            System.out.println("Send Notification clicked");
            return true;
        } else if (itemId == R.id.menu_logout) {// Handle Logout click
            System.out.println("Logout clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}