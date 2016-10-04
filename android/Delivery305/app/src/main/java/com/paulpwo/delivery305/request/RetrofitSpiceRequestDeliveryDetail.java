package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.DriverDelivery;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 15/7/16.
 */

public class RetrofitSpiceRequestDeliveryDetail extends RetrofitSpiceRequest<DriverDelivery, interfaceAll> {
    private String id;

    public RetrofitSpiceRequestDeliveryDetail(String id) {
        super(DriverDelivery.class, interfaceAll.class);
        this.id = id;
    }
    @Override
    public DriverDelivery loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDriverListDetail(id );

    }
}
