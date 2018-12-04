package com.example.activeledgersdk;

import com.example.activeledgersdk.API.APIService;
import com.example.activeledgersdk.utility.Utility;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class HttpClient {

    private static HttpClient instance;
    private APIService apiService;


    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
        }
        return instance;
    }



    private HttpClient(){
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.getInstance().getHTTPURL())
                                                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .build();

        apiService = retrofit.create(APIService.class);
    }



    public Observable<String> sendTransaction(String transaction){

        return apiService.sendTransaction(transaction);
    }


}
