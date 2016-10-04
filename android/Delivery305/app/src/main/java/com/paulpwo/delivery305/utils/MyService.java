package com.paulpwo.delivery305.utils;

/**
 * Created by paulpwo on 15/7/16.
 */

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.Keep;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.main.MainActivity2;
import com.paulpwo.delivery305.models.DriverDelivery;
import com.paulpwo.delivery305.utils.db.HelpersDB;

import org.json.JSONArray;
@Keep
public class MyService extends Service implements
        OnInitListener {
    int counter = 0;
   // static final int UPDATE_INTERVAL = 2000;
    private Timer timer = new Timer();
    private LocalBroadcastManager broadcaster;
    static final public String REQUEST_PROCESSED = "com.paulpwo.delivery305.REQUEST_PROCESSED";

    static final public String COPA_MSG = "com.paulpwo.delivery305.COPA_MSG";
    private DriverDelivery.List deliverys;
    private boolean  driver=false;
    private Date oldDateandTime;
    private Timestamp timeStampDate;
    private int mitepass;
    private String tmp2;
    private TextToSpeech tts;
    private Boolean trueSpeech = false;
    private TextToSpeech myTTS;
    private String str;
    private TextToSpeech mTts;
    private static final String TAG="TTSService";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        mTts = new TextToSpeech(this,
                this  // OnInitListener
        );
        //mTts.setSpeechRate(0.5f);
        Log.v(TAG, "oncreate_service TextToSpeech");
        str ="Welcome to Delivery every day";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        // Queremos que este servicio se ejecute continuamente
        // hasta que sea detenido manualmente, por lo que retornaremos
        // START_STICKY
       // sayHello(str);
        Bundle extras = intent.getExtras();

        if(extras == null)
            Log.d("Service","null");
        else
        {
            Log.d("Service","not null");
            String tmp = (String) extras.get("DataType");
            if(tmp.equalsIgnoreCase("DriverMain")){

                driver=true;
            }
        }



        doSomethingRepeatedly();
        showNotificationService();
           // new DoBackgroundTask().execute("OK");


        return START_STICKY;
    }
    private void loadDB(){
        if (deliverys != null){
            deliverys.clear();
        }else{
            this.deliverys = new DriverDelivery.List();
        }

        SQLiteDatabase db = HelpersDB.getInstance().getDB(getApplicationContext());
        String[] columns = {"id","address", "note", "time", "time_driver","status",
                "id_restaurant", "restaurant","phone_restaurant","restaurant_address, time_init"};
        Cursor cursor = db.query("deliveries", columns, "status=1", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new Games object and retrieve the data from the cursor to be stored in this Games object
                DriverDelivery tmp = new DriverDelivery();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank Games object to contain our data
                tmp.setId(cursor.getString(cursor.getColumnIndex("id")));
                tmp.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                tmp.setNote(cursor.getString(cursor.getColumnIndex("note")));
                tmp.setTime(cursor.getString(cursor.getColumnIndex("time")));
                tmp.setTime_driver(cursor.getString(cursor.getColumnIndex("time_driver")));
                tmp.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                tmp.setId_restaurant(cursor.getString(cursor.getColumnIndex("id_restaurant")));
                tmp.setRestaurant(cursor.getString(cursor.getColumnIndex("restaurant")));
                tmp.setPhone_restaurant(cursor.getString(cursor.getColumnIndex("phone_restaurant")));
                tmp.setRestaurant_address(cursor.getString(cursor.getColumnIndex("restaurant_address")));
                tmp.setTimeInit(cursor.getString(cursor.getColumnIndex("time_init")));
                deliverys.add(tmp);
            } while (cursor.moveToNext());
        }

    }
    private void showNotificationService(){
        Intent i = new Intent(getBaseContext(),MainActivity2.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                .setAutoCancel(false)
                .setContentTitle("Delivery 205")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getBaseContext().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        manager.notify(999,builder.build());
    }
    private void doSomethingRepeatedly(){
        timer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.d("MyService", String.valueOf(++counter));
                new DoBackgroundTask().execute("OK");
            }

        },0, Constants.UPDATE_INTERVAL_SERVICE);
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {

                sayHello(str);

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }
    private void sayHello(String str) {
        mTts.speak(str,
                TextToSpeech.QUEUE_FLUSH,
                null);
    }
    private class DoBackgroundTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... arg0) {
            // TODO Auto-generated method stub


            if(!driver){
                String message = (String) arg0[0];
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("NOMBRE_DE_NUESTRA_ACTION");
                broadcastIntent.putExtra("parameter", "value");
                getBaseContext().sendBroadcast(broadcastIntent);
            }else{
                loadDB();
                if (deliverys != null) {
                    if (deliverys.size() != 0){
                        ArrayList<String[]> list = new ArrayList<String[]>();

                        int t = deliverys.size();
                        for (int i = 0; i < t; i++) {
                            String tmp = deliverys.get(i).getTime_driver();

                            String id = deliverys.get(i).getId();
                            String time_init = deliverys.get(i).getTimeInit();
                            Timestamp stamp = new Timestamp(System.currentTimeMillis());


                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            try {
                                 oldDateandTime = sdf.parse(time_init);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                            Date dNow = new Date( ); // Instantiate a Date object
                            Date dNow2 = new Date( ); // Instantiate a Date object
                            Calendar cal = Calendar.getInstance();

                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            Date date = null;
                            try {
                                date = dateFormat.parse(time_init);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                tmp2 = Helpers.getInstance().getMinutesInteger(tmp);

                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }


                            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

                            cal.setTime(timeStampDate);
                            cal.add(Calendar.MINUTE, 1);
                            dNow = cal.getTime();

                            if (dNow.getTime() <= dNow2.getTime() ){
                                    if(tmp.equalsIgnoreCase("00:12:00")){ // 10
                                        sayHello("remaining 11 minutes to complete!");
                                    }

                                    if(tmp.equalsIgnoreCase("00:07:00")){ //
                                        sayHello("remaining 6 minutes to complete!");

                                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        long [] patron = {0, 300, 300};
                                        v.vibrate(1000);
                                    }

                                    if(tmp.equalsIgnoreCase("00:04:00")){
                                        sayHello("remaining 3 minutes to complete!");
                                    }
                                    if(tmp.equalsIgnoreCase("00:03:00")){
                                        sayHello("remaining 2 minutes to complete!");

                                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        long [] patron = {0, 300, 300};
                                        v.vibrate(1000);
                                    }
                                    if(tmp.equalsIgnoreCase("00:02:00")){
                                        sayHello("remaining 1 minutes to complete!");
                                    }


                                if (tmp.equalsIgnoreCase("00:00:00")){
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        HelpersDB.getInstance().uptadeTimeDelivery(getApplicationContext(),id,tmp2 );
                                        sayHello("completion time for: " + deliverys.get(i).getRestaurant()  );

                                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        long [] patron = {0, 300, 300};
                                        v.vibrate(1000);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                deliverys.get(i).setTime_driver(tmp2);
                                String[] item = {deliverys.get(i).getId(), tmp2};
                                list.add(item);
                                timeStampDate = new Timestamp(dNow2.getTime());
                                deliverys.get(i).setTimeInit(timeStampDate.toString());
                                HelpersDB.getInstance().uptadeTimeDelivery(getApplicationContext(),id,tmp2 );


                            }

                        } // END FOR for (int i = 0; i < t; i++)

                       // adapter.notifyDataSetChanged();
                        if (list != null){
                            if (list.size() >0){
                                JSONArray jsArray = new JSONArray(list);
                                String json = new Gson().toJson(list );

                                // new DoBackgroundTask().execute(jsArray);
                                // JSONArray jsArray = (JSONArray) arg0[0];
                                HelpersUpdateDBTick.getInstance().UpdateTimer_Delivery(
                                        Helpers.getInstance().readID(getApplicationContext()), json);
                            }
                        }

                    }
                }

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("NOMBRE_DE_NUESTRA_ACTION");
                broadcastIntent.putExtra("parameter", "value");
                getBaseContext().sendBroadcast(broadcastIntent);
            }

            return null;
        }

    }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            if (timer != null) {
                timer.cancel();
            }
            if (mTts != null) {
                mTts.stop();
                mTts.shutdown();
            }
            Toast.makeText(getBaseContext(), "background service stop",
                    Toast.LENGTH_SHORT).show();
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getBaseContext().getSystemService(ns);
            nMgr.cancel(999);
        }

}
