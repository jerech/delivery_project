package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.interfaceAll;
import com.paulpwo.delivery360.models.Contributor;

import roboguice.util.temp.Ln;

/**
 * Created by pwol on 18/2/16.
 */

public class RetrofitSpiceRequestUnit extends RetrofitSpiceRequest<Contributor.List, interfaceAll> {
    private String base;
    private String query;
    private boolean search;

    public RetrofitSpiceRequestUnit(String base, String query, boolean search) {
        super(Contributor.List.class, interfaceAll.class);
        this.base = base;
        this.query   = query;
        this.search = search;

    }
    @Override
    public Contributor.List loadDataFromNetwork() {
        Ln.d("Call web service ");
        if(search){
            return getService().contributorsGetOne(base, query);

        }else{
            return getService().contributors(base);
        }

    }
}
