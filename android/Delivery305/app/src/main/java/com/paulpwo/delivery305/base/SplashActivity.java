package com.paulpwo.delivery305.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.main.MainActivity2;
import com.paulpwo.delivery305.utils.Helpers;

public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 700;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//esto quita el título de la activity en la parte superior
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       //// toolbar.setVisibility(toolbar.INVISIBLE);
        //getActionBar().hide();
/*//y esto para pantalla completa (oculta incluso la barra de estado) */
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setContentView(R.layout.activity_intro_banner);

   /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;

                if (loginUserStatus()){

                    i = new Intent(SplashActivity.this, MainActivity2.class);
                    Helpers.getInstance().updateToken(getBaseContext());
                }else{
                    i = new Intent(SplashActivity.this, SelectAccountActivity.class);
                }


                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private Boolean loginUserStatus(){
        Boolean  status = false;
        //Opening shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);
        //Getting the email
        String email = sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);

        if (email != null ){
            status = true;

        }

        return status;
    }
}
