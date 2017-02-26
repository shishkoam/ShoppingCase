package com.yo.shishkoam.simplepromclient.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yo.shishkoam.simplepromclient.FavoritesManager;
import com.yo.shishkoam.simplepromclient.R;
import com.yo.shishkoam.simplepromclient.model.Result;

import java.util.List;

/**
 * Created by User on 25.02.2017
 */

public class ProductsRVAdapter extends RecyclerView.Adapter<ProductsRVAdapter.ProductViewHolder> {

    private List<Result> productList;
    private ProductLayoutType type;

    public ProductsRVAdapter(List<Result> productList, ProductLayoutType type) {
        this.productList = productList;
        this.type = type;
    }

    public void addProducts(List<Result> productList) {
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public void setProductList(List<Result> productList) {
        this.productList = productList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int itemResource = 0;
        if (type == ProductLayoutType.Linear) {
            itemResource = R.layout.list_item;
        } else if (type == ProductLayoutType.Grid) {
            itemResource = R.layout.grid_item;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(itemResource, viewGroup, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
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

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
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
