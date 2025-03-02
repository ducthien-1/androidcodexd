package com.example.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.adapter.CartAdapter;
import com.example.test1.entity.CartItem;

import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView emptyCartMessage;
    private TextView textTotalPayment;
    private CartAdapter cartAdapter;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        emptyCartMessage = findViewById(R.id.emptyCartMessage);
        textTotalPayment = findViewById(R.id.textTotalPayment);
        backBtn = findViewById(R.id.backBtn);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        List<CartItem> cartItems = ShoppingCartManager.getInstance().getCartItems();

        cartAdapter = new CartAdapter(cartItems, this);
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalPayment();
        toggleCartVisibility(cartItems);
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    public void updateTotalPayment() {
        double total = 0;
        for (CartItem item : ShoppingCartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                total += item.getProduct().getUnitPrice() * item.getQuantity();
            }
        }
        textTotalPayment.setText(String.format("Total Payment: $%.2f", total));
    }

    private void toggleCartVisibility(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            recyclerViewCart.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerViewCart.setVisibility(View.VISIBLE);
            emptyCartMessage.setVisibility(View.GONE);
        }
    }
}
