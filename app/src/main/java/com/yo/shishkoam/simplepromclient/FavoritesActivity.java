package com.yo.shishkoam.simplepromclient;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.yo.shishkoam.simplepromclient.adapters.ProductLayoutType;
import com.yo.shishkoam.simplepromclient.adapters.ProductsRVAdapter;
import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class FavoritesActivity extends ProductsActivity {
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        productsRecyclerView = (RecyclerView) findViewById(R.id.rv);
        initProductsRecyclerView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initProductsRecyclerView() {
        ProductLayoutType type = getRecyclerViewType(productsRecyclerView);
        ArrayList<Result> productList = new ArrayList(FavoritesManager.INSTANCE.getFavoriteProducts());
        ProductsRVAdapter adapter = new ProductsRVAdapter(this, productList, type);
        productsRecyclerView.setAdapter(adapter);
        productsRecyclerView.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    protected void searchProducts(String search) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
