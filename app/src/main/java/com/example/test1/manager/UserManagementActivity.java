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
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.adapter.AccountAdapter;
import com.example.test1.dao.AccountDAO;
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

        dbHelper = new DatabaseHelper(this);
        loadAccounts();
        RecyclerView recyclerViewAccountList = findViewById(R.id.recyclerViewAccountList);
        recyclerViewAccountList.setLayoutManager(new LinearLayoutManager(this));
        AccountAdapter accountAdapterAdapter = new AccountAdapter(accountList, this);
        recyclerViewAccountList.setAdapter(accountAdapterAdapter);
        TextView tvDelete = findViewById(R.id.tvDelete);
    }

    private void loadAccounts() {
        accountList.clear();
        Cursor cursor = dbHelper.getAllAccounts();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int accountId = cursor.getInt(cursor.getColumnIndex("accountId"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                accountList.add(new Account());
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }


}