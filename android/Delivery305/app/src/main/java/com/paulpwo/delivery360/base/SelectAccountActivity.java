package com.paulpwo.delivery360.base;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.paulpwo.delivery360.login.LoginDriverActivity;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.singup.SingupDriver;
import com.paulpwo.delivery360.singup.SingupRestaurant;
import com.paulpwo.delivery360.login.LoginRestaurantActivity;


public class SelectAccountActivity extends AppCompatActivity {

    Button btnRestaurant;
    Button btnDriver;
   // Button btnManager;
    private  Integer typeAccountRestaurant;
    Animation slideUpAnimation, slideDownAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        setContentView(R.layout.activity_select_acount);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnRestaurant = (Button) findViewById(R.id.btnRestaurant);
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccount(1);
            }
        });

        btnDriver = (Button) findViewById(R.id.btnDriver);
        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccount(2);
            }
        });

       /* btnManager = (Button) findViewById(R.id.btnManager);
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccount(3);
            }
        });*/

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);
        slideUpAnimation.start();

        TextView btn_new = (TextView) findViewById(R.id.btn_Sing_Up_New_User);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPoppup();
            }
        });

    }
    private void setPoppup(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectAccountActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.new_list_select_layout_alert, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("SELECT YOUR TYPE ACCOUNT");
        TextView title = new TextView(getApplicationContext());
        title.setTextColor(getResources().getColor(R.color.colorAccent));
        title.setText("SELECT YOUR TYPE ACCOUNT");
        title.setTextSize(20);
        title.setPadding(5,5,5,5);
        title.setTypeface(null, Typeface.BOLD);
        alertDialog.setCustomTitle(title);
        ListView lv = (ListView) convertView.findViewById(R.id.mi_lista);
        String[] items ={"RESTAURANT", "DRIVER"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.item_for_select_new_singup,
                R.id.textItem,items);
        alertDialog.setCancelable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent ac = new Intent(getApplicationContext(),SingupRestaurant.class);
                    startActivity(ac);
                }else{
                    Intent ac = new Intent(getApplicationContext(),SingupDriver.class);
                    startActivity(ac);
                }

            }
        });
        lv.setAdapter(adapter);
        final AlertDialog dlg = alertDialog.show();


        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable () {

            public void run() {
                if(dlg != null){
                    dlg.dismiss();
                    dlg.cancel();
                }
            }
        };
        mHandler.postDelayed(mRunnable,3000);

    }

    private void setAccount(Integer typeAccount){
        this.typeAccountRestaurant = typeAccount;
        Intent i;
        switch (typeAccount){
            case 1:
                i  = new Intent(this, LoginRestaurantActivity.class);
                //i.putExtra(Constants.TYPE_ACCOUNT_RESTAURAT,this.typeAccountRestaurant);
                this.startActivity(i);
                break;
            case 2:
                i  = new Intent(this, LoginDriverActivity.class);
               // i.putExtra(Constants.TYPE_ACCOUNT_RESTAURAT,this.typeAccountRestaurant);
                this.startActivity(i);
                break;
           /* case 3:
                i  = new Intent(this, LoginManagerActivity.class);
               // i.putExtra(Constants.TYPE_ACCOUNT_RESTAURAT,this.typeAccountRestaurant);
                this.startActivity(i);
                break;*/
        }
        finish();
    }

}

