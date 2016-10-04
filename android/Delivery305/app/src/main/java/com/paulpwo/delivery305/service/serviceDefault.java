package com.paulpwo.delivery305.service;



import android.support.annotation.Keep;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.config.Constants;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class serviceDefault extends RetrofitGsonSpiceService {

    private final static String BASE_URL = Constants.BASE_URL;



    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(interfaceAll.class);
    }


    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = super.createRestAdapterBuilder().setRequestInterceptor(
                new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                       // request.addHeader("Authorization", getTokent());
                    }
                }
        );
        builder.build();
        return builder;
    }


    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }



    public String getTokent() {
        return "62bb474dc8b12fe6bf3362a5a1a70ee1";
    }


    //______________________________________________________________________________________




}
