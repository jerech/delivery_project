package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Deliverys;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 15/7/16.
 */

public class RetrofitSpaceRequestDeliveryDeltail  extends RetrofitSpiceRequest<Deliverys, interfaceAll> {
    private String id_delivery;
    private String api_key;

    public RetrofitSpaceRequestDeliveryDeltail(String id_delivery, String api_key) {
        super(Deliverys.class, interfaceAll.class);
        this.id_delivery = id_delivery;
        this.api_key = api_key;
    }
    @Override
    public Deliverys loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getDelivery(id_delivery, api_key);

    }
}
