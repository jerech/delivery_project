package com.paulpwo.delivery360.interfaces;

/**
 * Created by paulpwo on 4/7/16.
 */

import com.paulpwo.delivery360.models.Manager;
import com.paulpwo.delivery360.models.Driver;
import com.paulpwo.delivery360.models.Restaurant;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface Interfacelogin {
    @FormUrlEncoded
     @POST("/loginDriver")
     Driver LoginDriver(@Field("email") String first, @Field("password") String last);

    @FormUrlEncoded
    @POST("/loginRestaurant")
    Restaurant LoginRestaurant(@Field("email") String first, @Field("password") String last);

    @FormUrlEncoded
    @POST("/loginManager")
    Manager LoginManager(@Field("email") String first, @Field("password") String last);


}
