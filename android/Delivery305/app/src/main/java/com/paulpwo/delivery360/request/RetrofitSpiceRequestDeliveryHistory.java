package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Deliverys;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 17/7/16.
 */

public class RetrofitSpiceRequestDeliveryHistory extends RetrofitSpiceRequest<Deliverys.List, interfaceAll> {
    private String api_key;
    private String id_restaurant;
    private String startDate;
    private String endDate;

    public RetrofitSpiceRequestDeliveryHistory(String id_restaurant,String api_key, String startDate, String endDate) {
        super(Deliverys.List.class, interfaceAll.class);

        this.api_key = api_key;
        this.id_restaurant = id_restaurant;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public Deliverys.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDeliverysHistory(id_restaurant,startDate,endDate,api_key);
    }
}

