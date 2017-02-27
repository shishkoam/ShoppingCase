package com.yo.shishkoam.simplepromclient;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.yo.shishkoam.simplepromclient.adapters.ProductLayoutType;

/**
 * Created by User on 26.02.2017
 */

abstract public class ProductsActivity extends AppCompatActivity implements Const {


    private boolean linearType = true;

    abstract protected void initProductsRecyclerView();

    abstract protected void searchProducts(String search);

    protected ProductLayoutType getRecyclerViewType(RecyclerView productsRecyclerView) {
        if (linearType) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            productsRecyclerView.setLayoutManager(mLayoutManager);
            return ProductLayoutType.Linear;
        } else {
            boolean twoColumn = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            productsRecyclerView.setLayoutManager(new GridLayoutManager(this, twoColumn ? 2 : 4));
            return ProductLayoutType.Grid;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showSearchDialog("");
            return true;
        } else if (id == R.id.action_type) {
            linearType = !linearType;
            initProductsRecyclerView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //saving search string if opened

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        String searchText = sPref.getString(SEARCH, "");
        if (!searchText.equals("")) {
            showSearchDialog(searchText);
        }
    }

    private void showSearchDialog(String searchText) {
        final EditText findEditText = new EditText(this);
        findEditText.setHint(DEFAULT_USER);
        //this part for saving instance when rotation
        if (searchText != null) {
            findEditText.setText(searchText);
        }
        findEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveSearchText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(findEditText)
                .setPositiveButton(R.string.search, (dialog, which)
                        -> searchProducts(findEditText.getText().toString()))
                .setNegativeButton(R.string.cancel, null)
                .setOnDismissListener(dialog -> saveSearchText(""))
                .setTitle(R.string.search);
        builder.show();
    }

    private void saveSearchText(String searchText) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SEARCH, searchText);
        ed.commit();
    }
}
