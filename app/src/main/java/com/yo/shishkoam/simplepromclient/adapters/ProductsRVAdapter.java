package com.yo.shishkoam.simplepromclient.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yo.shishkoam.simplepromclient.FavoritesManager;
import com.yo.shishkoam.simplepromclient.R;
import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by User on 25.02.2017
 */

public class ProductsRVAdapter extends RecyclerView.Adapter<ProductsRVAdapter.ProductViewHolder> {

    private Context context;
    private List<Result> productList;
    private ProductLayoutType type;

    public ProductsRVAdapter(Context context, List<Result> productList, ProductLayoutType type) {
        this.productList = productList;
        this.type = type;
        this.context = context;
    }

    public void addProducts(List<Result> productList) {
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public void setProductList(List<Result> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int itemResource = 0;
        if (type == ProductLayoutType.Linear) {
            itemResource = R.layout.list_item;
        } else if (type == ProductLayoutType.Grid) {
            itemResource = R.layout.grid_item;
        }
        View v = LayoutInflater.from(context).inflate(itemResource, viewGroup, false);
        return new ProductViewHolder(v);
    }

    public ProductLayoutType getType() {
        return type;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder personViewHolder, int i) {
        Result product = productList.get(i);
        personViewHolder.productName.setText(product.getName());
        personViewHolder.productPrice.setText(product.getPrice() + " / "
                + product.getDiscountedPrice() + " " + product.getPriceCurrency());
        personViewHolder.productPhoto.setImageResource(R.drawable.dummy200);

        personViewHolder.favoriteCheckbox.setChecked(FavoritesManager.INSTANCE.isFavorite(product));
        personViewHolder.favoriteCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FavoritesManager.INSTANCE.addToFavorite(product);
            } else {
                FavoritesManager.INSTANCE.removeFromFavorite(product);
            }
        });
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
//                Looper.prepare();
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.
                            with(context).
                            load(product.getUrlMainImage200x200()).
                            asBitmap().
                            into(-1, -1).
                            get();
                } catch (ExecutionException | InterruptedException e) {
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    personViewHolder.productPhoto.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView productName;
        TextView productPrice;
        ImageView productPhoto;
        View buyButton;
        CheckBox favoriteCheckbox;

        ProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            buyButton = itemView.findViewById(R.id.buy_button);
            favoriteCheckbox = (CheckBox) itemView.findViewById(R.id.add_to_favorite_button);

        }
    }
}
