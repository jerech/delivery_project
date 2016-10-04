package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.DriverDelivery;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 24/7/16.
 */

public class RetrofitSpaceRequestDriverHistory extends RetrofitSpiceRequest<DriverDelivery.List, interfaceAll> {
    private String api_key;
    private String id;

    public RetrofitSpaceRequestDriverHistory(String id, String api_key) {
        super(DriverDelivery.List.class, interfaceAll.class);

        this.api_key = api_key;
        this.id = id;
    }
    @Override
    public DriverDelivery.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDriverListHistory(id, api_key );

    }
}
