package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Driver;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 26/7/16.
 */

public class RequestSingleDriver extends RetrofitSpiceRequest<Driver, interfaceAll> {
    private String id;

    public RequestSingleDriver(String id) {
        super(Driver.class, interfaceAll.class);
        this.id = id;

    }
    @Override
    public Driver loadDataFromNetwork() {
        Ln.d("Call web service Free deliveries");
        return getService().getDriver(id);

    }
}