package com.example.test1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.test1.dtb.DatabaseHelper;
import com.example.test1.entity.Account;

public class AccountDAO {
    private static final String TABLE_NAME = "Accounts";
    private static final String COLUMN_ACCOUNT_ID = "accountId";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONENUMBER ="phoneNumber";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ROLE_ID = "roleId";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private final Context context;

    public AccountDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

    }
    // Get Account By Username
    public Account getAccountByUsername(String username) {
        String table = TABLE_NAME;
        String[] columns = {COLUMN_ACCOUNT_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONENUMBER, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_ROLE_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Account account = cursorToAccount(cursor);
            cursor.close();
            return account;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;

    }



    // Get Account By ID
    public Account getAccountById(int accountId) {
        String table = TABLE_NAME;
        String[] columns = {COLUMN_ACCOUNT_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONENUMBER, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_ROLE_ID};
        String selection = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accountId)};
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Account account = cursorToAccount(cursor);
            cursor.close();
            return account;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;


    }

    // Insert a new account
    public long insertAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, account.getUsername());
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_PHONENUMBER, account.getPhoneNumber());
        values.put(COLUMN_EMAIL, account.getEmail());
        values.put(COLUMN_ADDRESS, account.getAddress());
        values.put(COLUMN_ROLE_ID, account.getRoleId());

        return db.insert(TABLE_NAME, null, values);

    }


    // Login Method
    public Account login(String username, String password) {
        String table = TABLE_NAME;
        String[] columns = {COLUMN_ACCOUNT_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONENUMBER, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_ROLE_ID};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Account account = cursorToAccount(cursor);
            cursor.close();
            return account;
        }
        if(cursor != null) {
            cursor.close();
        }
        return null;
    }
    //Register Method
    public long register(String username, String password, String phoneNumber, String email, String address, int roleId) {

        if (checkUsername(username)) {
            return -1; // Username already exists
        }

        Account account = new Account(0, username, password, phoneNumber, email, address, roleId);
        return insertAccount(account);
    }
    // Check UserName
    private boolean checkUsername(String username) {
        String column = COLUMN_ACCOUNT_ID;
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_NAME, new String[]{column}, selection, selectionArgs, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Moethod to convert a cursor to an account object
    private Account cursorToAccount (Cursor cursor) {
        Account account = new Account();
        int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_ID));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
        String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONENUMBER));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
        int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE_ID));
        return new Account(accountId, username, password, phoneNumber, email, address, roleId);


    }


    public void close() {
        if(db != null && db.isOpen()) {
            db.close();
        }
    }



}
