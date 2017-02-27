package com.yo.shishkoam.simplepromclient;

import android.app.Application;

import com.yo.shishkoam.simplepromclient.api.PromApi;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 21.02.2017
 */
public class App extends Application {

    private static PromApi promApi;

    private final static String ACCEPT_KEY = "Accept";
    private final static String ACCEPT = "application/json";
    private final static String BASE_URL = "http://prom.ua/";

    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //adding common headers
        Interceptor contentType = (Interceptor.Chain chain) -> {
            Request original = chain.request();
            Request.Builder request = original.newBuilder()
////                    .header(ACCEPT_KEY, ACCEPT);
                    .header("Content-Type", "application/json");
            request.method(original.method(), original.body());
            return chain.proceed(request.build());
        };

        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(contentType);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()).create())
                .client(httpClient.build())
                .build();
        promApi = retrofit.create(PromApi.class);
    }

    public static PromApi getApi() {
        return promApi;
    }
}
