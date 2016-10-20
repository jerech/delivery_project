package com.paulpwo.delivery360.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.config.Constants;
import com.paulpwo.delivery360.driver.DriverMain;
import com.paulpwo.delivery360.driver.DriverNotifyNewDelivery;
import com.paulpwo.delivery360.driver.Profile_Drive;
import com.paulpwo.delivery360.interfaces.ListenerCreateDelivery;
import com.paulpwo.delivery360.interfaces.publicOKhttp;
import com.paulpwo.delivery360.manager.ManagerMain;
import com.paulpwo.delivery360.models.Driver;
import com.paulpwo.delivery360.models.Manager;
import com.paulpwo.delivery360.models.Restaurant;
import com.paulpwo.delivery360.restaurant.RestaurantMain;
import com.paulpwo.delivery360.restaurant.Restaurant_Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by paulpwo on 7/7/16.
 */
@Keep
public class Helpers {

    private publicOKhttp listener;
    private static Helpers ourInstance = new Helpers();


    public static Helpers getInstance() {

        return ourInstance;
    }

    private Helpers() {

    }
    public void logoutRestaurant(final Context c,String email,final publicOKhttp listener){
        this.listener = listener;
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(Constants.BASE_URL_RESTAURANT_LOGOUT)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFailureInMainThread(call, e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    listener.onResponseInMainThread(call, response);
                }
            });
        }catch (Exception e){
            //Toast.makeText(c, "Failed response server.", Toast.LENGTH_SHORT).show();
        }

    }
    public void logoutDriver(final Context c,String email,final publicOKhttp listener ){
        this.listener = listener;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL_DRIVER_LOGOUT)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              listener.onFailureInMainThread(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               listener.onResponseInMainThread(call, response);
            }
        });
    }
    public void logoutManager(final Context c, String email){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL_MANAGER_LOGOUT)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String res = call.toString();
                Log.v("FAIL SERVER LOGOUT", res);
               // Toast.makeText(c, "Failed response server.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.v("SERVER LOGOUT", res);
            }
        });
    }
    public void loadRestaurantMain(final FragmentActivity c){
        RestaurantMain fragment = new RestaurantMain();
        Bundle args = new Bundle();
        args.putString("param1", "parametro 1");
        args.putString("param2", "parametro 2");

        fragment.setArguments(args);
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, Constants.TAG_RESTAURANT)
                .commit();
    }
    public void loadDriverMain(final FragmentActivity c){
        DriverMain fragment = new DriverMain();
        Bundle args = new Bundle();
        args.putString("param1", "parametro 1");
        args.putString("param2", "parametro 2");

        fragment.setArguments(args);
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,Constants.TAG_DRIVER )
                .commit();
    }
    public void loadManagerMain(final FragmentActivity c){
        ManagerMain fragment = new ManagerMain();
        Bundle args = new Bundle();
        args.putString("param1", "parametro 1");
        args.putString("param2", "parametro 2");

        fragment.setArguments(args);
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,Constants.TAG_DRIVER)
                .commit();
    }
/*    public void loadDriveProfile(final FragmentActivity c){
        DriverProfile fragment = new DriverProfile();
        Bundle args = new Bundle();
        args.putString("param1", "parametro 1");
        args.putString("param2", "parametro 2");

        fragment.setArguments(args);
        FragmentManager fragmentManager = c.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }*/
    public void loadDriveProfile(final FragmentActivity c){
        Intent i = new Intent(c, Profile_Drive.class);
        c.startActivity(i);
    }
    public void loadRestaurantProfile(final FragmentActivity c){
        Intent i = new Intent(c, Restaurant_Profile.class);
        c.startActivity(i);
    }
    public void loadNotifyNewDelivery(final Context c,
                                      String title,
                                      String body,
                                      String id_delivery,
                                      String id_restaurant){

        Intent i = new Intent(c, DriverNotifyNewDelivery.class);
        Bundle mb = new Bundle();
        mb.putString("title", title);
        mb.putString("body", body);
        mb.putString("id_delivery", id_delivery);
        mb.putString("id_restaurant", id_restaurant);
        i.putExtras(mb);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        c.startActivity(i);
    }
    public String readName(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  "Welcome! : " +  sharedPreferences.getString(Constants.first_name, null) + " " +
                sharedPreferences.getString(Constants.last_name, null);

    }
    public String readUrlImage(final Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        String tmp = sharedPreferences.getString(Constants.image_url, Constants.BASE_URL_IMAGE_BASE);

        return tmp;

    }
    public String readUrlImageDriver(final Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        String tmp = sharedPreferences.getString(Constants.image_url, Constants.BASE_URL_IMAGE_BASE_DRIVE_NO_PICTURE);

        return tmp;

    }
    public String readToken_Push(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.TOKEN_PUSH, null);
    }
    public String readID(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.ID_PROFILE, null);
    }
    public String readFirstName(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.first_name, null);
    }
    public String readLastName(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.last_name, null);
    }
    public String readAddressName(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.address, null);
    }
    public String readPhone(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.phone, null);
    }
    public String readEmail(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);
    }
    public Integer readTypeAcount(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
    }
    public String readApikey(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.API_KEY, null);
    }

    public Integer readIsManager(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getInt(Constants.MANAGER_PRIFILE_INTEGER, 0);
    }
    public Integer readUserStatus(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        return  sharedPreferences.getInt(Constants.USER_STATUS, 0);
    }

    public void updateDriverLogin(final Context c ,Integer typeAccount,final  Driver result){

        String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE TOKEN", "InstanceID token: " + token);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("FIREBASE TOPIC", "Subscribed to news topic");
        String Rest = typeAccount.toString();
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        if(token==null){
            token="";
        }
        builder.add("regId", token);
        builder.add("email",result.getEmail());
        builder.add("typeUser",Rest);
        RequestBody body = builder.build();

        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_FIREBASE_SMS_DRIVE)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String res = call.toString();
                Log.v("SERVER REGISTER TOKEN", res);
               // Toast.makeText(c.getApplicationContext(), "Failed response server.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.v("SERVER REGISTER TOKEN", res);
            }
        });
        saveNewProfileDriver(c,result,token);

    }
    public void saveNewProfileDriver(final Context c, final  Driver result, @Nullable final String token){
        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // ESTORE EMAIL
        editor.putString(Constants.EMAIL_USER_LOGGED, result.getEmail());
        editor.putInt(Constants.TYPE_ACCOUNT_RESTAURAT, 2);
        //FOR PROFILE
        editor.putString(Constants.first_name,result.getFirst_name());
        editor.putString(Constants.last_name,result.getLast_name());
        editor.putString(Constants.address,result.getAddress());
        editor.putString(Constants.phone,result.getPhone());
        if (result.getImage_url() != null){
            if(result.image_url.length() > 8) {
                editor.putString(Constants.image_url, result.getImage_url());
            }
        }else{
            editor.putString(Constants.image_url,Constants.BASE_URL_IMAGE_BASE_DRIVE_NO_PICTURE);
        }
        editor.putString(Constants.created_At,result.getCreated_At());
        editor.putString(Constants.ID_PROFILE,result.getId());
        editor.putString(Constants.API_KEY,result.getApi_key());
        editor.putInt(Constants.MANAGER_PRIFILE_INTEGER,result.getManager());
        editor.putInt(Constants.USER_STATUS, 1);
        if(token != null){
            editor.putString(Constants.TOKEN_PUSH,token);
        }

        editor.apply();

    }

    public void updateRestaurantLogin(final Context c ,Integer typeAccount, final Restaurant result){

        String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE TOKEN", "InstanceID token: " + token);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("FIREBASE TOPIC", "Subscribed to news topic");
        String Rest = typeAccount.toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("regId", token)
                .add("email",result.getEmail())
                .add("typeUser",Rest)
                .build();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_FIREBASE_SMS_DRIVE)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String res = call.toString();
                Log.v("SERVER REGISTER TOKEN", res);
                Toast.makeText(c.getApplicationContext(), "Failed response server.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.v("SERVER REGISTER TOKEN", res);
            }
        });
       saveNewProfileRestaurant(c, result, token);
    }
    public void saveNewProfileRestaurant(final Context c, final  Restaurant result, @Nullable final String token) {
        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // ESTORE EMAIL
        editor.putString(Constants.EMAIL_USER_LOGGED, result.getEmail());
        editor.putInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
        //FOR PROFILE
        editor.putString(Constants.first_name,result.getName());
        editor.putString(Constants.address,result.getAddress());
        editor.putString(Constants.phone,result.getPhone());
        if (result.image_url != null){
            if(result.image_url.length() > 8) {
                editor.putString(Constants.image_url, result.getImage_url());
            }
        }else{
            editor.putString(Constants.image_url,Constants.BASE_URL_IMAGE_BASE_DRIVE_NO_PICTURE);
        }
        editor.putString(Constants.created_At,result.getCreated_at());
        editor.putString(Constants.ID_PROFILE,result.getId());
        editor.putString(Constants.API_KEY,result.getApi_key());
        if (token != null){
            editor.putString(Constants.TOKEN_PUSH, token);
        }

        //Applying the changes on sharedpreferences
        editor.apply();
    }
/*
*                   methods for manager
*/
    public void updateManagerLogin(final Context c ,
                                   Integer typeAccount,
                                   final Manager result,
                                   final publicOKhttp listener){
        this.listener = listener;

        String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE TOKEN", "InstanceID token: " + token);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("FIREBASE TOPIC", "Subscribed to news topic");
        String Rest = typeAccount.toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("regId", token)
                .add("email",result.getEmail())
                .add("typeUser",Rest)
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL_FIREBASE_SMS_DRIVE)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailureInMainThread(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponseInMainThread(call, response);
            }
        });
        saveNewProfileManager(c, result, token);
    }
    public void saveNewProfileManager(final Context c,
                                      final  Manager result,
                                      @Nullable final String token){

        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // ESTORE EMAIL
        editor.putString(Constants.EMAIL_USER_LOGGED, result.getEmail());
        editor.putInt(Constants.TYPE_ACCOUNT_RESTAURAT, 3);
        //FOR PROFILE
        editor.putString(Constants.EMAIL_USER_LOGGED,result.getEmail());
        editor.putString(Constants.API_KEY,result.getApi_key());
        editor.putInt(Constants.USER_STATUS, 1);
        if (token != null){
            editor.putString(Constants.TOKEN_PUSH, token);
        }
        editor.putString(Constants.created_At,result.getCreated_at());

        //Applying the changes on sharedpreferences
        editor.apply();

    }

    public void updateToken(final Context c){

        String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE TOKEN", "InstanceID token: " + token);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("FIREBASE TOPIC", "Subscribed to news topic");

        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);

        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.TOKEN_PUSH, token);
        //Applying the changes on sharedpreferences
        editor.apply();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("regId", token)
                .add("email",this.readEmail(c))
                .add("typeUser",this.readTypeAcount(c).toString())
                .build();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_FIREBASE_SMS_DRIVE)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String res = call.toString();
                Log.v("SERVER REGISTER TOKEN", res);
                // Toast.makeText(c.getApplicationContext(), "Failed response server.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.v("SERVER REGISTER TOKEN", res);
            }
        });


    }
    /*
    *                   methods for manager
    */
    public void SendnewDelivery(final ListenerCreateDelivery listenerCreateDelivery, final Context c , final String address, final String note, @Nullable String ID_RESTAURANT){
        RequestBody body;



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        if(ID_RESTAURANT == null){
             body = new FormBody.Builder()
                    .add("id_restaurant", this.readID(c))
                    .add("address", address)
                    .add("note",note)
                    .build();
        }else{
             body = new FormBody.Builder()
                    .add("id_restaurant", ID_RESTAURANT)
                    .add("address", address)
                    .add("note",note)
                    .build();
        }

        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_NEW_DELIVERY)
                .post(body)
                .header("Authorization" , this.readApikey(c))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                new CustomTask().execute(listenerCreateDelivery, c, response);


            }
        });




    }
    public void BlockUser(final Context c ,final String id){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_BLOCK_USER)
                .post(body)
                .header("Authorization" , this.readApikey(c))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Run your task here
                        Toast.makeText(c.getApplicationContext(), "User blocked", Toast.LENGTH_SHORT).show();
                    }
                }, 100 );
            }
        });




    }
    /*
    *                   methods for finish delivery
    */
    public void finishDelivery(final Context c ,final String id,final publicOKhttp listener){
        this.listener = listener;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .add("id_driver", this.readID(c))
                .build();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_FINISH_DELIVERy)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailureInMainThread(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

               listener.onResponseInMainThread(call,response);


            }
        });
    }

    public void resetPssword(final Context c ,final String email){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
           Request   request = new Request.Builder()
                .url(Constants.BASE_URL_RESET_PASS + email)
                .get()
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               // listener.onFailureInMainThread(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Run your task here
                        Toast.makeText(c.getApplicationContext(), "He was sent an email with instructions to reset your password", Toast.LENGTH_SHORT).show();
                    }
                }, 100 );
            }
        });
    }
    public void resetPsswordRestaurat(final Context c ,final String email){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_RESET_PASS_RESTAURANT + email)
                .get()
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // listener.onFailureInMainThread(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.code() == 200){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Run your task here
                            Toast.makeText(c.getApplicationContext(), "He was sent an email with instructions to reset your password", Toast.LENGTH_SHORT).show();
                        }
                    }, 100 );

                }else
                if(response.code() == 500){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Run your task here
                            Toast.makeText(c.getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                        }
                    }, 100 );

                }



            }
        });
    }


    /*
   *                   methods for CHOOSE delivery
   */
    public void chooseDelivery(final Context c ,
                               final String id_delivery,
                               final String timer_driver,
                               final publicOKhttp listener){
        this.listener = listener;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id_delivery", id_delivery)
                .add("id_driver", this.readID(c))
                .add("name_driver", this.readFirstName(c) + " " + this.readLastName(c))
                .add("timer_driver", timer_driver)
                .build();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_CHOOSE_DELIVERY)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailureInMainThread(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                listener.onResponseInMainThread(call,response);


            }
        });
    }
    /*
     *                   methods for CHOOSE delivery
     */
    public void countDeliveries(final Context c ,final publicOKhttp listener){
        this.listener = listener;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request   request = new Request.Builder()
                .url(Constants.BASE_URL_COUNT_DELIVERIES)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               // listener.onFailureInMainThread(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponseInMainThread(call,response);
            }
        });
    }

    public static String getMinutesString(String time){

        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));
            if (hour> 0 ){
                hour = hour * 60;
            }
            Integer  result = hour + minute;
            return result.toString() + "m";
        } catch (NumberFormatException e) {
            return "0m";
        }
    }
    public int getInteger(String tmp){

        try {
            int temp = Integer.parseInt(tmp);

            return temp;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    public String getMinutesInteger(String time) throws  Throwable{
//        int hour = Integer.parseInt(time.
        int minute=0;
        try {
            try {
              minute = Integer.parseInt(time.substring(3, 5));
            } catch (NumberFormatException e) {
                minute =0;
            }
                String mm = null;
                if (minute > 0 ){
                    minute = minute -1;
                    if (minute < 10 ){
                         mm = "0" + String.valueOf(minute);
                    }if(minute >= 10) {
                         mm =  String.valueOf(minute);
                    }
                    if(minute == 0) {
                         mm = "00";
                    }

                }else{
                    mm = "00";
                }

                String  result = "00:" + mm + ":00"  ;
            return result;
        } catch (NumberFormatException e) {
            return "0m";
        }
    }

    public int getMinutesIntegerV2(String time) throws  Throwable{
//        int hour = Integer.parseInt(time.
        int minute=0;
        try {
            try {
                minute = Integer.parseInt(time.substring(3, 5));
            } catch (NumberFormatException e) {
                minute =0;
            }

            return minute ;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void SendDriverStatusNewDeliveryResponse(final Context c ,
                                                    final Boolean accept,
                                                    final String id_delivery,
                                                    @Nullable final String timer,
                                                    final String id_restaurant,
                                                    final publicOKhttp listener){
        this.listener = listener;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("device_id", this.readToken_Push(c))
                .add("responseAccept", accept.toString())
                .add("id_driver", this.readID(c))
                .add("id_delivery", id_delivery)
                .add("timer_driver", timer)
                .add("id_restaurant", id_restaurant)
                .build();
        final Request   request = new Request.Builder()
                .url(Constants.BASE_URL_STATUS_DRIVER_NEW_DELIVERY_ACCEPT)
                .post(body)
                .header("Authorization" , this.readApikey(c))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailureInMainThread(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponseInMainThread(call, response);
            }
        });
    }
    public void cancelNotificationNew_delivery(Context c) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) c.getSystemService(ns);
        nMgr.cancel(Constants.NOTIFY_ID_NEW_DELIVERY);
    }
    public void cancelNotificationNoticeMana(Context c) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) c.getSystemService(ns);
        nMgr.cancel(Constants.NOTIFY_ID_NEW_DELIVERY);
    }

    public void deleteDelivery(Context c , String id_delivery){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                         .build();
        final Request   request = new Request.Builder()
                .url(Constants.BASE_URL_DELETE_DELIVERY + "/" + id_delivery )
                .post(body)
                .header("Authorization" , this.readApikey(c))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void setOffLine(Context c){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", this.readID(c))
                .build();
        final Request   request = new Request.Builder()
                .url(Constants.BASE_URL_SET_OFF_LINE )
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(Constants.USER_STATUS, 0);
        editor.apply();
    }
    public void setOnLine(Context c){

        String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASE TOKEN", "InstanceID token: " + token);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("FIREBASE TOPIC", "Subscribed to news topic");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", this.readID(c))
                .add("device_id", token)
                .build();
        final Request   request = new Request.Builder()
                .url(Constants.BASE_URL_SET_ON_LINE )
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        //Opening shared preference
        SharedPreferences sharedPreferences = c.getSharedPreferences(Constants.MY_SHARE_PREFERENCES, c.MODE_PRIVATE);
        //Opening the shared preferences editor to save values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (token != null){
            editor.putString(Constants.TOKEN_PUSH, token);
        }        //Applying the changes on sharedpreferences
        editor.putInt(Constants.USER_STATUS, 1);
        editor.apply();

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return !(ni == null || !ni.isConnected());
    }






    private class CustomTask extends AsyncTask<Object, Void, Boolean> {
        Context c;
        ListenerCreateDelivery listenerCreateDelivery;
        Response response;
        String id_delivery;

        protected Boolean doInBackground(Object... param) {
            listenerCreateDelivery = (ListenerCreateDelivery) param[0];
             c = (Context) param[1];
            response = (Response)  param[2];
            id_delivery = null;

            try {
                String jsonData = response.body().string();
                JSONObject Jobject = new JSONObject(jsonData);
                id_delivery = Jobject.getString("id_delivery");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (id_delivery!=null){
                return true;
            }

            return false;
        }

        protected void onPostExecute(Boolean param) {
            HelpersSniff.getInstance().niff(c,id_delivery);
            if(listenerCreateDelivery!=null){
                listenerCreateDelivery.showMassege(param);
            }


        }
    }





}
