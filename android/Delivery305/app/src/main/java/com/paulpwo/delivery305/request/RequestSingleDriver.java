package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.Driver;
import com.paulpwo.delivery305.models.DriversWorking;

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