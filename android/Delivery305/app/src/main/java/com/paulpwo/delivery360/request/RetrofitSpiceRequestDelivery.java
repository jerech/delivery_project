package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Deliverys;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 11/7/16.
 */


public class RetrofitSpiceRequestDelivery extends RetrofitSpiceRequest<Deliverys.List, interfaceAll> {
    private String api_key;
    private String id_restaurant;

    public RetrofitSpiceRequestDelivery(String id_restaurant,String api_key) {
        super(Deliverys.List.class, interfaceAll.class);

        this.api_key = api_key;
        this.id_restaurant = id_restaurant;
    }
    @Override
    public Deliverys.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDeliverys(id_restaurant, api_key );

    }
}
