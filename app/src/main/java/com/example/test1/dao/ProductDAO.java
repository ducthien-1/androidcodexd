package com.example.test1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.test1.R;
import com.example.test1.dtb.DatabaseHelper;
import com.example.test1.entity.Product;

import java.util.ArrayList;
import java.util.List;



    public class ProductDAO {
        private static final String TAG = "ProductDAO";
        private DatabaseHelper dbHelper;

        public ProductDAO(Context context) {
            dbHelper = new DatabaseHelper(context);
            Log.d(TAG, "ProductDAO initialized");
        }
        // ProductDAO.java
        public void addProduct(Product product) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.d(TAG, "Adding product: " + product.getProductName());
            ContentValues values = new ContentValues();
            values.put("categoryId", product.getCategoryId());
            values.put("productName", product.getProductName());
            values.put("productDescription", product.getProductDescription());
            values.put("unitPrice", product.getUnitPrice());
            values.put("unitQuantity", product.getUnitQuantity());
            values.put("isFeatured", product.isFeatured() ? 1 : 0);
            values.put("imageResId", product.getImageResId());
            values.put("sales", product.getSales()); // Add sales
            long result = db.insert("Products", null, values);
            if (result != -1) {
                Log.d(TAG, "Product added successfully with ID: " + result);
            } else {
                Log.e(TAG, "Failed to add product: " + product.getProductName());
            }
            db.close();
        }

        public Product getProduct(int productId) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Log.d(TAG, "Querying product with ID: " + productId);
            Cursor cursor = db.rawQuery("SELECT * FROM Products WHERE productId = ?", new String[]{String.valueOf(productId)});
            Product product = null;
            if (cursor.moveToFirst()) {
                product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("productName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("productDescription")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("unitPrice")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("unitQuantity")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isFeatured")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("imageResId")), // Use imageResId
                        cursor.getInt(cursor.getColumnIndexOrThrow("sales"))
                );
                Log.d(TAG, "Found product: " + product.getProductName());
            } else {
                Log.w(TAG, "No product found with ID: " + productId);
            }
            cursor.close();
            db.close();
            return product;
        }

        public List<Product> getAllProducts() {
            List<Product> productList = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Log.d(TAG, "Querying all products");
            Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("productId"));
                        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                        String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                        String productDescription = cursor.getString(cursor.getColumnIndexOrThrow("productDescription"));
                        double unitPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("unitPrice"));
                        int unitQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("unitQuantity"));
                        boolean isFeatured = cursor.getInt(cursor.getColumnIndexOrThrow("isFeatured")) == 1;
                        int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId")); // Use imageResId
                        int sales = cursor.getInt(cursor.getColumnIndexOrThrow("sales"));

                        Log.d(TAG, "Raw data - productId: " + productId + ", productName: " + (productName != null ? productName : "null") + ", imageResId: " + imageResId + ", sales: " + sales);

                        if (productName == null) productName = "Unnamed Product";
                        if (productDescription == null) productDescription = "No description";

                        Product product = new Product(productId, categoryId, productName, productDescription, unitPrice, unitQuantity, isFeatured, imageResId, sales);
                        productList.add(product);
                        Log.d(TAG, "Retrieved product: " + product.getProductName());
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to create Product from cursor: " + e.getMessage(), e);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No products found in database");
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Total products retrieved: " + productList.size());
            return productList;
        }

        public void updateProduct(Product product) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.d(TAG, "Updating product: " + product.getProductName());
            ContentValues values = new ContentValues();
            values.put("categoryId", product.getCategoryId());
            values.put("productName", product.getProductName());
            values.put("productDescription", product.getProductDescription());
            values.put("unitPrice", product.getUnitPrice());
            values.put("unitQuantity", product.getUnitQuantity());
            values.put("isFeatured", product.isFeatured() ? 1 : 0);
            values.put("imageResId", product.getImageResId());
            values.put("sales", product.getSales()); // Add sales
            int rowsAffected = db.update("Products", values, "productId = ?", new String[]{String.valueOf(product.getProductId())});
            if (rowsAffected > 0) {
                Log.d(TAG, "Product updated successfully");
            } else {
                Log.e(TAG, "Failed to update product with ID: " + product.getProductId());
            }
            db.close();
        }

        public void deleteProduct(int productId) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.d(TAG, "Deleting product with ID: " + productId);
            int rowsAffected = db.delete("Products", "productId = ?", new String[]{String.valueOf(productId)});
            if (rowsAffected > 0) {
                Log.d(TAG, "Product deleted successfully");
            } else {
                Log.w(TAG, "No product found to delete with ID: " + productId);
            }
            db.close();
        }

        public void insertSampleProducts(Context context) {
            Log.d(TAG, "Inserting sample products");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Clear existing products
            db.delete("Products", null, null);

            // Insert products with specific IDs
            Product product1 = new Product(1, 1, "Sample Product", "Description", 99.99, 100, true, R.drawable.product1, 50);
            if (product1 == null) {
                Log.e(TAG, "Product1 is null! Using fallback.");
                product1 = new Product(1, 1, "Fallback Product 1", "Fallback Description", 99.99, 100, true, R.drawable.product2, 0);
            }
            ContentValues values1 = new ContentValues();
            values1.put("productId", product1.getProductId());
            values1.put("categoryId", product1.getCategoryId());
            values1.put("productName", product1.getProductName());
            values1.put("productDescription", product1.getProductDescription());
            values1.put("unitPrice", product1.getUnitPrice());
            values1.put("unitQuantity", product1.getUnitQuantity());
            values1.put("isFeatured", product1.isFeatured() ? 1 : 0);
            values1.put("imageResId", product1.getImageResId()); // Use imageResId
            values1.put("sales", product1.getSales());
            db.insertWithOnConflict("Products", null, values1, SQLiteDatabase.CONFLICT_REPLACE);

            Product product2 = new Product(2, 1, "Another Product", "Description", 149.99, 50, false, R.drawable.product2, 30);
            if (product2 == null) {
                Log.e(TAG, "Product2 is null! Using fallback.");
                product2 = new Product(2, 1, "Fallback Product 2", "Fallback Description", 149.99, 50, false, android.R.drawable.ic_menu_help, 0);
            }
            ContentValues values2 = new ContentValues();
            values2.put("productId", product2.getProductId());
            values2.put("categoryId", product2.getCategoryId());
            values2.put("productName", product2.getProductName());
            values2.put("productDescription", product2.getProductDescription());
            values2.put("unitPrice", product2.getUnitPrice());
            values2.put("unitQuantity", product2.getUnitQuantity());
            values2.put("isFeatured", product2.isFeatured() ? 1 : 0);
            values2.put("imageResId", product2.getImageResId()); // Use imageResId
            values2.put("sales", product2.getSales());
            db.insertWithOnConflict("Products", null, values2, SQLiteDatabase.CONFLICT_REPLACE);

            Log.d(TAG, "Sample products inserted successfully with IDs 1 and 2");
            db.close();
        }
    }