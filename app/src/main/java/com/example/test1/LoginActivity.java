package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.dao.AccountDAO;
import com.example.test1.entity.Account;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText et_username;
    private EditText et_password;
    private Button btn_sign_in;
    private TextView tv_register_link;
    private ImageView tv_password_visibility;
    private AccountDAO accountDAO;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        tv_register_link = findViewById(R.id.tv_register_link);
        tv_password_visibility = findViewById(R.id.iv_password_visibility);
        tv_register_link = findViewById(R.id.tv_register_link);

        // Initialize DAO

        accountDAO = new AccountDAO(this);
        //Password visibility toggle
        tv_password_visibility.setOnClickListener(v -> {
                    isPasswordVisible = !isPasswordVisible;

                    // Toggle the visibility of the password
                    if (isPasswordVisible) {
                        et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        tv_password_visibility.setImageResource(R.drawable.ic_visibility);
                    } else {
                        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        tv_password_visibility.setImageResource(R.drawable.ic_visibility);
                    }
                });

        // Set click listener for the sign-in button
        btn_sign_in.setOnClickListener(v -> {
                    String username = et_username.getText().toString();
                    String password = et_password.getText().toString();
                    if(username.isEmpty()) {
                        et_username.setError("Username is required");
                        et_username.requestFocus();
                        return;
                    }
                    if(password.isEmpty()) {
                        et_password.setError("Password is required");
                        et_password.requestFocus();
                        return;
                    }

                    if(!username.isEmpty() && !password.isEmpty()) {

                        Account account = accountDAO.login(username, password);
                        if(account != null) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(this, "Login failed. Password or username is incorrect", Toast.LENGTH_SHORT).show();
                            et_username.setText("");
                            et_password.setText("");
                        }

                    }

                });
        // Set click listener for register
        tv_register_link.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(accountDAO != null) {
            accountDAO.close();

        }
    }
}