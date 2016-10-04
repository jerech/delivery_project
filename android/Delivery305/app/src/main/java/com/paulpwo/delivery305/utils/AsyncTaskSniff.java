package com.paulpwo.delivery305.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Keep;

import com.paulpwo.delivery305.config.Constants;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by paulpwo on 13/7/16.
 */
@Keep
public class AsyncTaskSniff extends AsyncTask<Object, Void, Void> {
    final String TAG = "AsyncTaskSniff.java";

    // set json string url

    @Override
    protected void onPreExecute() {}
    @Override
    protected Void doInBackground(Object... arg0) {


        Context c = (Context) arg0[0];
        String id_delivery = (String) arg0[1];


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id_delivery", id_delivery )
                .add("id_restaurant", Helpers.getInstance().readID(c))
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL_SNIFF_NOTIFY)
                .post(body)
                .header("Authorization" , Helpers.getInstance().readApikey(c))
                .build();


        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
    protected void onPostExecute(String result){

    }
}
