package com.paulpwo.delivery360.push;

/**
 * Created by Paul Osinga on 1/jun/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.paulpwo.delivery360.config.Constants;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.main.MainActivity2;
import com.paulpwo.delivery360.manager.ManagerNoticeList;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.utils.db.HelpersDB;

import org.json.JSONObject;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    private String title;
    private String title_restaurant;
    private String type;
    private String receiver;

    private String body;
    private String id_delivery;
    private  String id_restaurant;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            String tmp = remoteMessage.getData().get("message");
        getType(tmp);
        int isManager = Helpers.getInstance().readIsManager(getBaseContext());
        int TYPE = Helpers.getInstance().readTypeAcount(getBaseContext());

        if(isManager ==1){
            if(receiver.equalsIgnoreCase("manager")) {
                if(type.equalsIgnoreCase("manager_decline_delivery")){
                    //showNotificationDeclineDelivery(tmp);
                }
            }
        }

        if(TYPE== 1){

        }else{
            if(receiver.contains("driver")) {
                switch (type){
                    case "new":
                        //showNotificationNewDelivery(tmp);
                        break;
                    case "new_all":
                        showNotificationNewDeliveryV2(tmp);
                        break;
                }
            }
        }








    }

    private Boolean IsRestaurant(String tmp){


        return  true;
    }
    private  void getType(String message){
        try {

            final JSONObject obj = new JSONObject(message);
                type = obj.getString("type");
                receiver = obj.getString("receiver");

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }

    }
   /* private void showNotificationNewDelivery(String message) {

        try {
           final JSONObject obj = new JSONObject(message);
                title = obj.getString("title");
                body = obj.getString("body");
                id_delivery = obj.getString("id_delivery");
                id_restaurant = obj.getString("id_restaurant");
            Log.d("My App", obj.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();

        Intent i = new Intent(this,DriverNotifyNewDelivery.class);
        Bundle mb = new Bundle();
        mb.putString("title", title);
        mb.putString("body", body);
        mb.putString("id_delivery", id_delivery);
        mb.putString("id_restaurant", id_restaurant);

        i.putExtras(mb);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 1, 1)
                .setSound(alarmSound)
                .setPriority(Notification.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)

                .setNumber(1)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            builder.setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFY_ID_NEW_DELIVERY,builder.build());


        if(type.contains("new")){
            Helpers.getInstance().loadNotifyNewDelivery(getApplicationContext(),title,body,id_delivery,id_restaurant);
        }else{
            Intent myIntent = new Intent(getApplicationContext(), MainActivity2.class);
            // myIntent.putExtra("key", value); //Optional parameters
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getApplicationContext().startActivity(myIntent);
        }


    }*/
    private void showNotificationNewDeliveryV2(String message) {

        try {
            final JSONObject obj = new JSONObject(message);
            title = obj.getString("title");
            body = obj.getString("body");
            id_delivery = obj.getString("id_delivery");
            id_restaurant = obj.getString("id_restaurant");
            Log.d("My App", obj.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();

/*        Intent i = new Intent(this,DriverListAvailable.class);
        Bundle mb = new Bundle();
        mb.putString("title", title);
        mb.putString("body", body);
        mb.putString("id_delivery", id_delivery);
        mb.putString("id_restaurant", id_restaurant);

        i.putExtras(mb);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);*/


        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        i.putExtra("new", true); //Optional parameters
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 1, 1)
                .setSound(alarmSound)
                .setPriority(Notification.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)

                .setNumber(1)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            builder.setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFY_ID_NEW_DELIVERY,builder.build());

           // Helpers.getInstance().loadNotifyNewDelivery(getApplicationContext(),title,body,id_delivery,id_restaurant);

        //MainActivity2 ma = MainActivity2.getInstace();
        //ma.loadAvailable();
           Intent myIntent = new Intent(getApplicationContext(), MainActivity2.class);
            myIntent.putExtra("new", true); //Optional parameters
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getApplicationContext().startActivity(myIntent);


    }

    private void showNotificationDeclineDelivery(String message) {

        try {
            final JSONObject obj = new JSONObject(message);
            title = obj.getString("title");
            body = obj.getString("body");
            id_delivery = obj.getString("id_delivery");
            id_restaurant = obj.getString("id_restaurant");
            Log.d("My App", obj.toString());
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }
        try {

            HelpersDB.getInstance().saveNoticeDeclineDeliveryForManager(getApplicationContext(),
                    title,
                    id_delivery, id_restaurant,
                    body);
        } catch (Throwable t) {
            Log.e("My App", "Error SqLite: \"" + t.toString() + "\"");
        }
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();

        Intent i = new Intent(this,ManagerNoticeList.class);
        Bundle mb = new Bundle();
        mb.putString("title", title);
        mb.putString("body", body);
        mb.putString("id_delivery", id_delivery);
        mb.putString("id_restaurant", id_restaurant);

        i.putExtras(mb);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 1, 1)
                .setSound(alarmSound)
                .setPriority(Notification.PRIORITY_LOW)

                .setNumber(1)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            builder.setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFY_ID_NEW_DELIVERY,builder.build());



         /*   Intent myIntent = new Intent(getApplicationContext(), ManagerNoticeList.class);
            // myIntent.putExtra("key", value); //Optional parameters
            myIntent.putExtras(mb);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getApplicationContext().startActivity(myIntent);*/



    }


}
