package com.example.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication1.Model.Customer;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "customerapp";
    public static final int DATABASE_VERSION = 10;

    public static final String TABLE_CUSTOMER = "customer";
    public static final String ROW_ID = "id";
    public static final String ROW_NAME = "name";
    public static final String ROW_ADDRESS = "address";
    public static final String ROW_DESCRIPTION = "description";
    Context context;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CUSTOMER + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ROW_NAME + " TEXT NOT NULL, " + ROW_ADDRESS + " TEXT NOT NULL, " + ROW_DESCRIPTION + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }

    public void AddData(Customer cst) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ROW_NAME, cst.getName().trim());
        cv.put(ROW_ADDRESS, cst.getAdress().trim());
        cv.put(ROW_DESCRIPTION, cst.getDescription().trim());

        db.insert(TABLE_CUSTOMER, null, cv);
        db.close();
    }

    public ArrayList<Customer> ListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Customer> data = new ArrayList<>();
        String[] columns = {ROW_ID, ROW_NAME, ROW_ADDRESS, ROW_DESCRIPTION};
        Cursor cursor = db.query(TABLE_CUSTOMER, columns, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            data.add(new Customer(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3)));
        }
        cursor.close();
        return data;
    }

    public void EditData(Customer cst) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ROW_ID + " = " + cst.getId();

        ContentValues cv = new ContentValues();
        cv.put(ROW_NAME, cst.getName());
        cv.put(ROW_ADDRESS, cst.getAdress());
        cv.put(ROW_DESCRIPTION, cst.getDescription());
        db.update(TABLE_CUSTOMER, cv, where, null);

        db.close();
    }

    public Customer ShowData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {ROW_ID, ROW_NAME, ROW_ADDRESS, ROW_DESCRIPTION};
        String where = ROW_ID + " = " + id;
        Cursor cursor = db.query(TABLE_CUSTOMER, columns, where, null,
                null, null, null);
        Customer cst = new Customer(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        return cst;
    }

    public void DeleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ROW_ID + " = " + id;
        db.delete(TABLE_CUSTOMER, where, null);
        db.close();
    }

//    public void DeleteData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_TODO, null, null);
//        db.close();
//    }

}
