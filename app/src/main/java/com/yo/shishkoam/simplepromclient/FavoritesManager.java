package com.yo.shishkoam.simplepromclient;

import android.content.Context;

import com.yo.shishkoam.simplepromclient.db.DbType;
import com.yo.shishkoam.simplepromclient.db.ProductsDbHelper;
import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 26.02.2017
 */

public enum FavoritesManager implements Const {
    INSTANCE;

    private HashMap<Integer, Result> favoritesMap = new HashMap<>();
    private ProductsDbHelper dbHelper;

    FavoritesManager() {
    }

    public void init(Context context) {
        dbHelper = new ProductsDbHelper(context, DbType.FAVORITES);
        List<Result> productsList = dbHelper.readData();
        for (Result product: productsList) {
            favoritesMap.put(product.getId(), product);
        }
    }

    public Collection<Result> getFavoriteProducts() {
        return favoritesMap.values();
    }

    public void removeFromFavorite(Result product) {
        dbHelper.remove(product);
        favoritesMap.remove(product.getId());
    }

    public void addToFavorite(Result product) {
        favoritesMap.put(product.getId(), product);
        dbHelper.addProduct(product);
    }

    public boolean isFavorite(Result product) {
        return favoritesMap.containsKey(product.getId());
    }

}