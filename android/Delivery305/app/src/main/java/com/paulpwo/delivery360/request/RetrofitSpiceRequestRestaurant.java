package com.paulpwo.delivery360.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.paulpwo.delivery360.interfaces.Interfacelogin;
import com.paulpwo.delivery360.models.Restaurant;

import roboguice.util.temp.Ln;

/**
 * Created by paulpwo on 4/7/16.
 */

public class RetrofitSpiceRequestRestaurant extends RetrofitSpiceRequest<Restaurant, Interfacelogin> {
private String email;
private String password;


public RetrofitSpiceRequestRestaurant(String email, String password) {
        super(Restaurant.class, Interfacelogin.class);
        this.email = email;
        this.password   = password;


        }
@Override
public Restaurant loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().LoginRestaurant(email, password);

        }
}
