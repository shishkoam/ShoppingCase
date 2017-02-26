
package com.yo.shishkoam.simplepromclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("price_currency")
    @Expose
    private String priceCurrency;
    @SerializedName("url_main_image_200x200")
    @Expose
    private String urlMainImage200x200;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Result() {
    }

    public Result(String priceCurrency, String urlMainImage200x200, String name, String discountedPrice, String price, Integer id) {
        this.priceCurrency = priceCurrency;
        this.urlMainImage200x200 = urlMainImage200x200;
        this.name = name;
        this.discountedPrice = discountedPrice;
        this.price = price;
        this.id = id;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getUrlMainImage200x200() {
        return urlMainImage200x200;
    }

    public void setUrlMainImage200x200(String urlMainImage200x200) {
        this.urlMainImage200x200 = urlMainImage200x200;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
