package com.paulpwo.delivery360.driver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.utils.db.HelpersDB;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.interfaces.publicOKhttp;
import com.paulpwo.delivery360.models.DriverDelivery;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDeliveryDetail;
import com.paulpwo.delivery360.utils.Helpers;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class DriverDetailDelivery extends BaseSpiceActivity implements publicOKhttp {

    @BindView(R.id.toolbar)     Toolbar toolbar;
    @BindView(R.id.name)     TextView restaurantName;
    @BindView(R.id.textView5)     TextView textView5;
    @BindView(R.id.driver_address)     TextView restaurantAddress;
    @BindView(R.id.textView3)     TextView textView3;
    @BindView(R.id.delivery_address)     TextView deliveryAddress;
    @BindView(R.id.textView6)     TextView textView6;
    @BindView(R.id.tvOnlineTime)     TextView time;
    @BindView(R.id.textView8)     TextView textView8;
    @BindView(R.id.txtNote)     TextView txtNote;
    @BindView(R.id.btnCallDriver)     Button btnCallRestaurant;
    @BindView(R.id.btnFinish)     Button btnFinish;
    @BindView(R.id.btnChoose)     Button btnChoose;


    private String id;
    private Boolean finish;
    private DriverDelivery delivery;
    private boolean choose = false;
    String choosetime;
    private boolean finishDelivery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail_delivery);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getExtras().getString("id");
        finish = getIntent().getExtras().getBoolean("finish");

        if(getIntent().hasExtra("delivery")){
            delivery = (DriverDelivery)getIntent().getSerializableExtra("delivery");
            updateList(delivery);
        }else {

            callWebService();
        }
    }

    @OnClick({R.id.btnCallDriver, R.id.btnFinish, R.id.btnChoose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCallDriver:
                callRestaurant();
                break;
            case R.id.btnFinish:
               finishDelivery();
                break;
            case R.id.btnChoose:
                choose = true;
                chooseDelivery();
                break;
        }
    }

    private void finishDelivery(){
        new AlertDialog.Builder(DriverDetailDelivery.this)
                .setTitle("Finish")
                .setMessage("Are yousure to end this item" )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishDelivery = true;
                        Helpers.getInstance().finishDelivery(DriverDetailDelivery.this,id, DriverDetailDelivery.this);
                        //callWebService();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private AlertDialog chooseDelivery(){




        final CharSequence[] items = {"5 MINS", "10 MINS", "15 MINS"};
        AlertDialog dialog;
        choosetime="00:05:00";
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverDetailDelivery.this);
        builder.setTitle("Choose time")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item){
                            case 0:
                                choosetime="00:05:00";
                                break;
                            case 1:
                                choosetime="00:10:00";
                                break;
                            case 2:
                                choosetime="00:15:00";
                                break;
                        }

                    }
                }) ;
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseFinish();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();

        return dialog;

        // Helpers.getInstance().chooseDelivery(DriverDetailDelivery.this,id,);
    }

    private void chooseFinish(){
        Helpers.getInstance().chooseDelivery(DriverDetailDelivery.this,id,choosetime,DriverDetailDelivery.this);

    }
    private void callRestaurant(){
        new AlertDialog.Builder(DriverDetailDelivery.this)
                .setTitle("Call to Restaurant")
                .setMessage("Are you sure you want to call : " + delivery.getRestaurant() )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + delivery.getPhone_restaurant()));
                        if (ActivityCompat.checkSelfPermission(DriverDetailDelivery.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void callWebService(){
        if(Helpers.isOnline(getBaseContext())){
            RetrofitSpiceRequestDeliveryDetail queryUnit =
                    new RetrofitSpiceRequestDeliveryDetail(id);
        
            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.NEVER,
                    new RequestResult());
        }else{
            Toast.makeText(DriverDetailDelivery.this, "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
      
    }

    private void updateList(DriverDelivery response){
        restaurantName.setText(response.getRestaurant());
        restaurantAddress.setText(response.getRestaurant_address());
        deliveryAddress.setText(response.getAddress());
        txtNote.setText(response.getNote());
        time.setText(response.getTime());
        delivery = response;
        //MARCAR PARA ESCONDER FINISH

       String tmp = Helpers.getInstance().readID(DriverDetailDelivery.this);
        String tmp2 = delivery.getId_driver();
        if(tmp2.equals(tmp) ){
            if(response.getTime_driver().contains("00:00:00")){
                if(!response.getStatus().equalsIgnoreCase("0")){
                    btnFinish.setVisibility(View.VISIBLE);
                }
            }
        }else{
            btnChoose.setVisibility(View.VISIBLE);
        }
        if(Helpers.getInstance().readUserStatus(getBaseContext()) != 1){
            btnFinish.setVisibility(View.INVISIBLE);
            btnChoose.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onFailureInMainThread(Call call, IOException e) {

        finish();
    }

    @Override
    public void onResponseInMainThread(Call call, Response response) throws IOException {
        
        if(choose){
            if(response.code() != 404){
                HelpersDB.getInstance().newDeliveryLocalDB(getApplicationContext(),
                        delivery.getId(),
                        delivery.getAddress(),
                        delivery.getNote(),
                        delivery.time,
                        choosetime,
                        delivery.getStatus(),
                        delivery.getRestaurant(),
                        delivery.getPhone_restaurant(),
                        delivery.getRestaurant_address());
                finish();
            }else{

                new Thread()
                {
                    public void run()
                    {
                        DriverDetailDelivery.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(DriverDetailDelivery.this, "Sorry, could not be assigned this delivery." +
                                    " Try again in the free list.", Toast.LENGTH_SHORT).show();
                                //Do your UI operations like dialog opening or Toast here
                            }
                        });
                    }
                }.start();


            }
        }

        if(finishDelivery){
            HelpersDB.getInstance().finishDelivery(getApplicationContext(),id);
            finish();
        }



    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class RequestResult implements RequestListener<DriverDelivery> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            getSpiceManager().cancelAllRequests();

        }
        @Override
        public void onRequestSuccess(DriverDelivery deliverys) {

                        Log.v("Resul", deliverys.toString());
                        updateList(deliverys);

        }
    }

}
