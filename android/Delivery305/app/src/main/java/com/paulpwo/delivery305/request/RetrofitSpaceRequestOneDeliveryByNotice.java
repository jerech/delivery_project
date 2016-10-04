package com.paulpwo.delivery305.request;

import android.support.annotation.Keep;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery305.interfaces.interfaceAll;
import com.paulpwo.delivery305.models.NoticeDelivery;

/**
 * Created by paulpwo on 14/7/16.
 */

public class RetrofitSpaceRequestOneDeliveryByNotice extends RetrofitSpiceRequest<NoticeDelivery, interfaceAll> {

    private String api_key;
    private String id_delvery;

    public RetrofitSpaceRequestOneDeliveryByNotice(String id_delvery,String api_key) {
        super(NoticeDelivery.class, interfaceAll.class);
        this.api_key = api_key;
        this.id_delvery = id_delvery;
    }

    @Override
    public NoticeDelivery loadDataFromNetwork() throws Exception {
        return getService().getNoticeDelivery(id_delvery, api_key );
    }
}
