package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.Interfacelogin;
import com.paulpwo.delivery360.models.Manager;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 4/7/16.
 */

public class RetrofitSpiceRequestManager extends RetrofitSpiceRequest<Manager, Interfacelogin> {
    private String email;
    private String password;
    public RetrofitSpiceRequestManager(String email, String password) {
        super(Manager.class, Interfacelogin.class);
        this.email = email;
        this.password   = password;


    }
    @Override
    public Manager loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().LoginManager(email, password);

    }
}
