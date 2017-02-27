package com.yo.shishkoam.simplepromclient.api;

import com.yo.shishkoam.simplepromclient.model.ProductsModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 21.02.2017
 */
public interface PromApi {
//
//    @POST("/_/graph/request")
//    Call<ProductsModel> getProducts(@Query("limit") int limit,
//                                    @Query("offset ") int offset,
//                                    @Query("category") long category,
//                                    @Query("sort") String sortType,
//                                    @Body RequestBody params);

    @POST("/_/graph/request")
    Call<ProductsModel> getProducts(@Query("limit") int limit,
                                                @Query("offset ") int offset,
                                                @Query("category") long category,
                                                @Query("sort") String sortType);
}
