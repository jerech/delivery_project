package com.paulpwo.delivery305.utils;

import android.support.annotation.Keep;
import android.util.Log;

import com.paulpwo.delivery305.config.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by paulpwo on 16/7/16.
 */
@Keep
public class HelpersUpdateDBTick {
    private static HelpersUpdateDBTick ourInstance = new HelpersUpdateDBTick();

    public static HelpersUpdateDBTick getInstance() {
        return ourInstance;
    }

    private HelpersUpdateDBTick() {
    }
    public void UpdateTimer_Delivery(final String id_restaurant, final String dataJson ){

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("data", dataJson)
                .build();
        final Request request = new Request.Builder()
                .url(Constants.BASE_URL_UPDATE_DELIVERY )
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("SERVER REGISTER TOKEN", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("SERVER REGISTER TOKEN", response.toString());
            }
        });
    }
}
