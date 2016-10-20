package com.paulpwo.delivery360.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Keep;

import com.paulpwo.delivery360.config.Constants;

/**
 * Created by paulpwo on 13/7/16.
 */
@Keep
public class HelpersSniff {
    private static HelpersSniff ourInstance = new HelpersSniff();

    public static HelpersSniff getInstance() {
        return ourInstance;
    }

    private HelpersSniff() {
    }
    public void niff(final Context c,final String id_delivery){

     /*   Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsyncTaskSniff taskSniff = new AsyncTaskSniff();
                            taskSniff.execute(c, id_delivery);
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

       // timer.schedule(task, 0, 60*1000);  // interval of one minute
        timer.schedule(task, 0, 50*1000);  // interval of one minute*/


        Handler handler = new Handler();
        final Handler finalHandler = handler;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
      /* do what you need to do */
                AsyncTaskSniff taskSniff = new AsyncTaskSniff();
                taskSniff.execute(c, id_delivery);
      /* and here comes the "trick" */
               // finalHandler.postDelayed(this, 100);
            }
        };
        
        handler.postDelayed(runnable, Constants.TIME_SNIFF_NOTIFY_DB);



    }









}
