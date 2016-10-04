package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.DriverDeliveryAvailable;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 17/7/16.
 */

public class RetrofitSpaceRequestDeliveries extends RetrofitSpiceRequest<DriverDeliveryAvailable.List, interfaceAll> {
    private String api_key;

    public RetrofitSpaceRequestDeliveries() {
        super(DriverDeliveryAvailable.List.class, interfaceAll.class);


    }
    @Override
    public DriverDeliveryAvailable.List loadDataFromNetwork() {
        Ln.d("Call web service Free deliveries");
        return getService().getDeliveriesFree();

    }

}