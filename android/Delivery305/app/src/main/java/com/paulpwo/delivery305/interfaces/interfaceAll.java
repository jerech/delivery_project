package com.paulpwo.delivery305.interfaces;



import android.support.annotation.Keep;

import com.paulpwo.delivery305.models.Contributor;
import com.paulpwo.delivery305.models.Deliverys;
import com.paulpwo.delivery305.models.Driver;
import com.paulpwo.delivery305.models.DriverDelivery;
import com.paulpwo.delivery305.models.DriverDeliveryAvailable;
import com.paulpwo.delivery305.models.DriversWorking;
import com.paulpwo.delivery305.models.NoticeDelivery;
import com.paulpwo.delivery305.models.Restaurant;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface interfaceAll {
    @GET("/{base}/{query}")
    Contributor.List contributorsGetOne(@Path("base") String base, @Path("query") String id);

    @GET("/{item}")
    Contributor.List contributors(@Path("item") String item);
    //@GET("/repos/{owner}/{repo}/contributors")

    @FormUrlEncoded
    @POST("/logindriver")
    Driver.List LoginDriver(@Field("email") String first, @Field("password") String last);

    @FormUrlEncoded
    @POST("/driverPut/{item}")
    Driver updateDriverProfile(@Path("item") String  item ,
                                    @Field("first_name") String first_name,
                                    @Field("last_name") String last_name,
                                    @Field("address") String address,
                                    @Field("phone") String phone,
                                    @Field("email") String email,
                                    @Field("image_url") String image_url,
                                    @Header("Authorization") String api_key);

    @FormUrlEncoded
    @POST("/restaurantPut/{item}")
    Restaurant updateRestaurantProfile(@Path("item") String  item ,
                                       @Field("name") String Name,
                                       @Field("address") String Address,
                                       @Field("phone") String Phone,
                                       @Field("email") String Email,
                                       @Field("image_url") String image_url,
                                       @Header("Authorization") String api_key);

    @GET("/deliverys/{item}")
    Deliverys.List getDeliverys(@Path("item") String  item, @Header("Authorization") String api_key);

    @GET("/deliverysHistory/{item}")
    Deliverys.List getDeliverysHistory(@Path("item") String  item, @Header("Authorization") String api_key);


    @GET("/deliveryDetail/{item}")
    Deliverys getDelivery(@Path("item") String  item, @Header("Authorization") String api_key);

    @GET("/driverList/{item}")
    DriverDelivery.List getDriverList(@Path("item") String  item, @Header("Authorization") String api_key);


    @GET("/deliverysHistoryDriver/{item}")
    DriverDelivery.List getDriverListHistory(@Path("item") String  item, @Header("Authorization") String api_key);


    @GET("/noticeDelivery/{item}")
    NoticeDelivery getNoticeDelivery(@Path("item") String  item, @Header("Authorization") String api_key);


    @GET("/driverListDetail/{item}")
    DriverDelivery getDriverListDetail(@Path("item") String  item);


    @GET("/deliveriesFree")
    DriverDeliveryAvailable.List getDeliveriesFree();


    @GET("/driversWorkingList")
    DriversWorking.List getDriversWorkingList();

    @GET("/driver/{item}")
    Driver getDriver(@Path("item") String  item);


}