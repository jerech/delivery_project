package com.paulpwo.delivery305.push;

/**
 * Created by pwxss on 1/7/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

//import com.google.android.gms.playlog.internal.LogEvent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.paulpwo.delivery305.main.MainActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by filipp on 5/23/2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
private static Context mContext;

    public void setRegistertoken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
        Log.d("FCN TOKEN GET", "Refreshed token: " + token);

       /* final Intent intent = new Intent("tokenReceiver");
        // You can also include some extra data.
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("token",token);
        broadcastManager.sendBroadcast(intent);*/
    }
    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
        Log.d("FCN TOKEN GET", "Refreshed token: " + token);

   /*     final Intent intent = new Intent("tokenReceiver");
        // You can also include some extra data.
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("token",token);
        broadcastManager.sendBroadcast(intent);*/

    }
    public void setNotify(Context context){
        this.mContext = context;
        showNotificationService();
    }

    private void registerToken(String token) {
        if(token == null){
            return;
        }
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("regId",token)
                    .add("email", "paulpwo@gmail.com")
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.0.102/api/v1/SetdeviceIdDriver")
                    .post(body)
                    .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        String res = call.toString();
                        Log.v("registerToken", res);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        Log.v("registerToken", res);

                    }
                });

    }
    private void showNotificationService(){
        Intent i = new Intent(this.mContext,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext)
                .setAutoCancel(false)
                .setContentTitle("Delivery 205")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) this.mContext.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

}
