package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.DriverDelivery;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 24/7/16.
 */

public class RetrofitSpaceRequestDriverHistory extends RetrofitSpiceRequest<DriverDelivery.List, interfaceAll> {
    private String api_key;
    private String id;
    private String startDate;
    private String endDate;

    public RetrofitSpaceRequestDriverHistory(String id, String api_key, String startDate, String endTime) {
        super(DriverDelivery.List.class, interfaceAll.class);

        this.api_key = api_key;
        this.id = id;
        this.startDate = startDate;
        this.endDate = endTime;
    }
    @Override
    public DriverDelivery.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDriverListHistory(id, startDate, endDate,api_key );

    }
}
