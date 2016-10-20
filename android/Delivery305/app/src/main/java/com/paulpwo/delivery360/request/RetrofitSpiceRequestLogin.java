package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.Interfacelogin;
import com.paulpwo.delivery360.models.Driver;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 4/7/16.
 */

public class RetrofitSpiceRequestLogin extends RetrofitSpiceRequest<Driver, Interfacelogin> {
    private String email;
    private String password;


    public RetrofitSpiceRequestLogin(String email, String password) {
        super(Driver.class, Interfacelogin.class);
        this.email = email;
        this.password   = password;


    }
    @Override
    public Driver loadDataFromNetwork() {
        Ln.d("Call web service ");
            return getService().LoginDriver(email, password);

    }

}
