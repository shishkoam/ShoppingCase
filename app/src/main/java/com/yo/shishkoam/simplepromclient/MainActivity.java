package com.yo.shishkoam.simplepromclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yo.shishkoam.simplepromclient.adapters.ProductLayoutType;
import com.yo.shishkoam.simplepromclient.adapters.ProductsRVAdapter;
import com.yo.shishkoam.simplepromclient.api.ApiRequest;
import com.yo.shishkoam.simplepromclient.db.DbType;
import com.yo.shishkoam.simplepromclient.db.ProductsDbHelper;
import com.yo.shishkoam.simplepromclient.model.Catalog;
import com.yo.shishkoam.simplepromclient.model.ProductsModel;
import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ProductsActivity {

    private List<Result> productsList;
    private List<String> sortTypeList;
    private RecyclerView productsRecyclerView;
    private ProductsDbHelper productsCacheHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View bottomProgress;
    private Spinner sortSpinner;
    private ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FavoritesManager.INSTANCE.init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomProgress = findViewById(R.id.request_progress);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        initSpinner();

        apiRequest = new ApiRequest(60, 0, 35402, "price");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->{
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        });

        productsCacheHelper = new ProductsDbHelper(this, DbType.CACHE);

        initializeData();
        productsRecyclerView = (RecyclerView) findViewById(R.id.rv);
        initProductsRecyclerView();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> requestProducts());

        requestProducts();
    }

    private void initSpinner() {
        String[] sortValues = getResources().getStringArray(R.array.sort_values);
        String[] sortTypes = getResources().getStringArray(R.array.sort_ids);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setSelection(1);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                apiRequest.setSortType(sortTypes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    protected void initProductsRecyclerView() {
        ProductLayoutType type = getRecyclerViewType(productsRecyclerView);
        ProductsRVAdapter adapter = new ProductsRVAdapter(productsList, type);
        productsRecyclerView.setAdapter(adapter);
        productsRecyclerView.setItemAnimator(new SlideInUpAnimator());

        productsRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if (!productsRecyclerView.canScrollVertically(1) && velocityY > 0) {
                    requestProductsToAdd();
                    return true;
                }
                return false;
            }
        });

        productsRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (!productsRecyclerView.canScrollVertically(1)) {
                    requestProductsToAdd();
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });

    }

    @Override
    public void searchProducts(String search) {

    }

    private void initializeData() {
        productsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Result product = new Result();
            product.setId(i);
            product.setName("Продукт");
            product.setPrice("10.0");
            product.setPriceCurrency("грн");
            productsList.add(product);
        }
    }

    private void requestProducts() {
        showProgress(true);
        //empty strings says that we request history for whole period
        App.getApi().getProducts(apiRequest.getLimit(), apiRequest.getOffset(),
                apiRequest.getCategory(), apiRequest.getSortType()).enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.code() == SUCCESS_CODE && response.body() != null) {
                    Catalog catalog = response.body().getCatalog();
                    productsList = catalog.getResults();
                    sortTypeList = catalog.getPossibleSorts();
                    ProductsRVAdapter adapter = ((ProductsRVAdapter) productsRecyclerView.getAdapter());
                    adapter.setProductList(productsList);
                    adapter.notifyDataSetChanged();
                    productsRecyclerView.setAdapter(adapter);
                    productsCacheHelper.clearDataBase();
                    productsCacheHelper.addProductList(productsList);
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                showProgress(false);
                Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestProductsToAdd() {
        bottomProgress.setVisibility(View.VISIBLE);
        //empty strings says that we request history for whole period
        apiRequest.setOffset(apiRequest.getLimit() + apiRequest.getOffset());
        App.getApi().getProducts(apiRequest.getLimit(), apiRequest.getOffset(),
                apiRequest.getCategory(), apiRequest.getSortType()).enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.code() == SUCCESS_CODE && response.body() != null) {
                    Catalog catalog = response.body().getCatalog();
                    List<Result> newProductsList = catalog.getResults();
                    productsList.addAll(newProductsList);
                    sortTypeList = catalog.getPossibleSorts();
                    ProductsRVAdapter adapter = ((ProductsRVAdapter) productsRecyclerView.getAdapter());
                    adapter.addProducts(newProductsList);
                    adapter.notifyDataSetChanged();
                    productsRecyclerView.setAdapter(adapter);
                    productsCacheHelper.addProductList(productsList);
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                bottomProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                bottomProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows the progress UI and hides the content form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        productsRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        productsRecyclerView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                productsRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        swipeRefreshLayout.setRefreshing(show);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            showSearchDialog("");
//            return true;
//        } else if (id == R.id.action_type) {
//            linearType = !linearType;
//            initProductsRecyclerView();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    //saving search string if opened
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
//        String searchText = sPref.getString(SEARCH, "");
//        if (!searchText.equals("")) {
//            showSearchDialog(searchText);
//        }
//    }
//
//    private void showSearchDialog(String searchText) {
//        final EditText findEditText = new EditText(this);
//        findEditText.setHint(DEFAULT_USER);
//        //this part for saving instance when rotation
//        if (searchText != null) {
//            findEditText.setText(searchText);
//        }
//        findEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                saveSearchText(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(findEditText)
//                .setPositiveButton(R.string.search, (dialog, which) -> {
////                        initListView(findEditText.getText().toString(), onAuthorClickCallback);
//                })
//                .setNegativeButton(R.string.cancel, null)
//                .setOnDismissListener(dialog -> {
//                    saveSearchText("");
//                })
//                .setTitle(R.string.search);
//        builder.show();
//    }
//
//    void saveSearchText(String searchText) {
//        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString(SEARCH, searchText);
//        ed.commit();
//    }
}
