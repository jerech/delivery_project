package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Restaurant;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 8/7/16.
 */

public class RetrofitSpiceRequestRestaurantUpProfile extends RetrofitSpiceRequest<Restaurant, interfaceAll> {
    private String ID;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String image_url;
    private  String api_key;


    public RetrofitSpiceRequestRestaurantUpProfile(String ID,
                                                   String name,
                                                   String address,
                                                   String phone,
                                                   String email,
                                                   String image_url,
                                                   String api_key) {
        super(Restaurant.class, interfaceAll.class);
        this.ID = ID;
        this.name   = name;
        this.address   = address;
        this.phone   = phone;
        this.email   = email;
        this.image_url   = image_url;
        this.api_key = api_key;


    }
    @Override
    public Restaurant loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().updateRestaurantProfile(ID,name,address,phone,email,image_url, api_key);

    }
}

