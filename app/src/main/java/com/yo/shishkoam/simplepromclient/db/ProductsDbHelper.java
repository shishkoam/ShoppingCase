package com.yo.shishkoam.simplepromclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07.02.2017
 */

public class ProductsDbHelper extends SQLiteOpenHelper {

    private final static String CURRENCY = "priceCurrency";
    private final static String IMAGE = "urlMainImage200x200";
    private final static String NAME = "name";
    private final static String DISCOUNT_PRICE = "discountedPrice";
    private final static String PRICE = "price";
    private final static String ID = "id";
    private final static String PRODUCT_ID = "product_id";
    private DbType dbType;

    public ProductsDbHelper(Context context, DbType type) {
        super(context, "myDB", null, 1);
        this.dbType = type;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(dbType.getName(), db);
    }

    private void createDatabase(String databaseName, SQLiteDatabase db) {
        // create table
        db.execSQL("create table " + databaseName + " ("
                + ID + " integer primary key autoincrement,"
                + NAME + " string,"
                + PRODUCT_ID + " integer,"
                + IMAGE + " string,"
                + PRICE + " string,"
                + CURRENCY + " string,"
                + DISCOUNT_PRICE + " string"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addProductList(List<Result> productList) {
        SQLiteDatabase db = getWritableDatabase();
        for (Result product : productList) {
            // create object for data
            ContentValues cv = new ContentValues();
            // prepare pairs to insert
            cv.put(PRODUCT_ID, product.getId());
            cv.put(NAME, product.getName());
            cv.put(IMAGE, product.getUrlMainImage200x200());
            cv.put(PRICE, product.getPrice());
            cv.put(DISCOUNT_PRICE, product.getDiscountedPrice());
            cv.put(CURRENCY, product.getPriceCurrency());
            //insert object to db
            db.insert(dbType.getName(), null, cv);
        }
        close();
    }

    public void addProduct(Result product) {
        SQLiteDatabase db = getWritableDatabase();
        // create object for data
        ContentValues cv = new ContentValues();
        // prepare pairs to insert
        cv.put(PRODUCT_ID, product.getId());
        cv.put(NAME, product.getName());
        cv.put(IMAGE, product.getUrlMainImage200x200());
        cv.put(PRICE, product.getPrice());
        cv.put(DISCOUNT_PRICE, product.getDiscountedPrice());
        cv.put(CURRENCY, product.getPriceCurrency());
        //insert object to db
        db.insert(dbType.getName(), null, cv);
        close();
    }

    public List<Result> readData() {
        List<Result> productList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        // request all data (cursor) from table
        Cursor c = db.query(dbType.getName(), null, null, null, null, null, null);
        // check that table has data
        if (c.moveToFirst()) {
            // get column index by name
            int idColIndex = c.getColumnIndex(ID);
            int productIdColIndex = c.getColumnIndex(PRODUCT_ID);
            int nameColIndex = c.getColumnIndex(NAME);
            int imageColIndex = c.getColumnIndex(IMAGE);
            int priceColIndex = c.getColumnIndex(PRICE);
            int discountPriceColIndex = c.getColumnIndex(DISCOUNT_PRICE);
            int currencyColIndex = c.getColumnIndex(CURRENCY);

            do {

                // get data by column indexes
                int id = c.getInt(idColIndex);
                int productId = c.getInt(productIdColIndex);
                String name = c.getString(nameColIndex);
                String urlImage = c.getString(imageColIndex);
                String price = c.getString(priceColIndex);
                String discountedPrice = c.getString(discountPriceColIndex);
                String currency = c.getString(currencyColIndex);
                Result product = new Result(currency, urlImage, name, discountedPrice, price, productId);
                productList.add(product);
            } while (c.moveToNext());
        }
        c.close();
        close();
        return productList;
    }

    public void remove(Result product) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + dbType.getName() + " WHERE " + PRODUCT_ID + " = '" + product.getId() + "'");
    }

    public int clearDataBase() {
        SQLiteDatabase db = getWritableDatabase();
        int clearCount = db.delete(dbType.getName(), null, null);
        close();
        return clearCount;
    }
}
