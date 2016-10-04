package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.Driver;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 7/7/16.
 */

public class RetrofitSpiceRequestDriver  extends RetrofitSpiceRequest<Driver, interfaceAll> {
    private String ID;
    private String first_name;
    private String last_name;
    private String address;
    private String phone;
    private String email;
    private String image_url;
    private  String api_key;


    public RetrofitSpiceRequestDriver(String ID,
                                      String first_name,
                                      String last_name,
                                      String address,
                                      String phone,
                                      String email,
                                      String image_url,
                                      String api_key) {
        super(Driver.class, interfaceAll.class);
        this.ID = ID;
        this.first_name   = first_name;
        this.last_name   = last_name;
        this.address   = address;
        this.phone   = phone;
        this.email   = email;
        this.image_url   = image_url;
        this.api_key = api_key;
    }
    @Override
    public Driver loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().updateDriverProfile(ID,first_name,last_name,address,phone,email,image_url, api_key);

    }
}
