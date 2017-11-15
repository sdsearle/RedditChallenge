package com.example.admin.redditchallenge;

import com.example.admin.redditchallenge.model.RedditResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by admin on 11/6/2017.
 */

public class RetrofitHelper {

    private final static String BASE_URL = "https://www.reddit.com/";

    public static Retrofit create(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Observable<Response<RedditResponse>> createSearch(String query){
        Retrofit retrofit = create();
        ApiService apiService =  retrofit.create(ApiService.class);
        return apiService.getSearch(query);

    }

    interface ApiService{

        @GET("r/{subject}/.json")
        Observable<Response<RedditResponse>> getSearch(@Path("subject") String subject);
    }

}
