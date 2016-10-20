package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.DriversWorking;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 17/7/16.
 */

public class RetrofitRequestDriversWorking extends RetrofitSpiceRequest<DriversWorking.List, interfaceAll> {
    private String api_key;

    public RetrofitRequestDriversWorking() {
        super(DriversWorking.List.class, interfaceAll.class);


    }
    @Override
    public DriversWorking.List loadDataFromNetwork() {
        Ln.d("Call web service Free deliveries");
        return getService().getDriversWorkingList();

    }
}