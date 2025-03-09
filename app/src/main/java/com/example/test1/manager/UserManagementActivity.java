package com.example.test1.manager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.adapter.AccountAdapter;
import com.example.test1.dtb.DatabaseHelper;
import com.example.test1.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AccountAdapter adapter;
    private List<Account> accountList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo accountList
        accountList = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);
        loadAccounts();

        // Thiết lập RecyclerView
        RecyclerView recyclerViewAccountList = findViewById(R.id.recyclerViewAccountList);
        recyclerViewAccountList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(accountList, this); // Khởi tạo adapter
        recyclerViewAccountList.setAdapter(adapter);

        TextView tvDelete = findViewById(R.id.tvDelete);
        // TODO: Thêm logic cho tvDelete nếu cần
    }

    private void loadAccounts() {
        // Kiểm tra null và khởi tạo nếu cần
        if (accountList == null) {
            accountList = new ArrayList<>();
        } else {
            accountList.clear();
        }

        Cursor cursor = dbHelper.getAllAccounts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int accountId = cursor.getInt(cursor.getColumnIndex("accountId"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") int roleId = cursor.getInt(cursor.getColumnIndex("roleId"));

                Account account = new Account(accountId, username, password, phoneNumber, email, address, roleId);
                accountList.add(account);
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