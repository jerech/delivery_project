package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.DeliverysManager;

import roboguice.util.temp.Ln;

/**
 * Created by jeremias on 5/10/16.
 */

public class RetrofitSpiceRequestDeliveryHistoryManager extends RetrofitSpiceRequest<DeliverysManager.List, interfaceAll> {
    private String api_key;
    private String startDate;
    private String endDate;

    public RetrofitSpiceRequestDeliveryHistoryManager(String startDate, String endDate,String api_key) {
        super(DeliverysManager.List.class, interfaceAll.class);

        this.api_key = api_key;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public DeliverysManager.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDeliverysHistoryManager(startDate,endDate, api_key );
    }
}
