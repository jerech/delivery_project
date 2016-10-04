package com.paulpwo.delivery305.driver;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.interfaces.RefreshDatas;
import com.paulpwo.delivery305.utils.db.HelpersDB;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.base.BaseSpiceActivity;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.interfaces.publicOKhttp;
import com.paulpwo.delivery305.models.DriverDelivery;
import com.paulpwo.delivery305.request.RetrofitSpiceRequestDeliveryDetail;
import com.paulpwo.delivery305.utils.Helpers;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class DriverNotifyNewDelivery extends BaseSpiceActivity implements publicOKhttp {

    @BindView(R.id.txtBody)
    TextView txtBody;
    @BindView(R.id.txtCount)
    TextView txtcount;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn10)
    Button btn10;
    @BindView(R.id.btn15)
    Button btn15;
    @BindView(R.id.btnDecline)
    Button btnDecline;
    String id_delivery;
    String id_restaurant;
    String timeDriver ="0";
    RefreshDatas mListener;
    CountDownTimer ct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_notify_new_delivery);
        ButterKnife.bind(this);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        String title = getIntent().getExtras().getString("title");
        String body = getIntent().getExtras().getString("body");
        id_delivery = getIntent().getExtras().getString("id_delivery");
        id_restaurant = getIntent().getExtras().getString("id_restaurant");

        txtBody.setText(body);

        setTime();




    }
    private void setTime() {
       ct = new CountDownTimer(Constants.NOTIFY_TIME_OUT, 1000) {

            public void onTick(long millisUntilFinished) {
                txtcount.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        txtcount.setText("rejected!");
                        decline();
                    }
                });
            }
        }.start();
    }

    @OnClick({R.id.btn5, R.id.btn10, R.id.btn15, R.id.btnDecline})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn5:

                accept("00:05:00");
                break;
            case R.id.btn10:

                accept("00:10:00");
                break;
            case R.id.btn15:

                accept("00:15:00");
                break;
            case R.id.btnDecline:
                ct.cancel();
                decline();
                break;
        }
    }
    private void decline(){
         Helpers.getInstance().SendDriverStatusNewDeliveryResponse(
                getApplicationContext(),
                false,id_delivery,"0", id_restaurant, this);
        //finish();
    }
    private void accept(String timer){
        ct.cancel();
        timeDriver=timer;
        Helpers.getInstance().SendDriverStatusNewDeliveryResponse(
                getApplicationContext(),
                true,id_delivery, timer,id_restaurant , this);
       // chooseFinish(timer);
        callWebService(id_delivery);
  /*      new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 300);*/


        //finish();
    }
    private void chooseFinish(String timer){
        Helpers.getInstance().chooseDelivery(DriverNotifyNewDelivery.this,id_delivery,timer,DriverNotifyNewDelivery.this);

    }
    @Override
    public void onFailureInMainThread(Call call, IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "General error to set response", Toast.LENGTH_SHORT).show();
                Helpers.getInstance().cancelNotificationNew_delivery(getApplicationContext());
                //listener.onBackResult();
                finish();

            }
        });
    }

    @Override
    public void onResponseInMainThread(Call call, Response response) throws IOException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Helpers.getInstance().cancelNotificationNew_delivery(getApplicationContext());
               // mListener.onBackResult();

               // finish();

            }
        });
    }








    private void callWebService(String id){

        RetrofitSpiceRequestDeliveryDetail queryUnit =
                new RetrofitSpiceRequestDeliveryDetail(id);

        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new RequestResult());
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
            HelpersDB.getInstance().newDeliveryLocalDB(getApplicationContext(),
                    deliverys.getId(),
                    deliverys.getAddress(),
                    deliverys.getNote(),
                    timeDriver,
                    deliverys.getTime_driver(),
                    deliverys.getStatus(),
                    deliverys.getRestaurant(),
                    deliverys.getPhone_restaurant(),
                    deliverys.getRestaurant_address());
            finish();

        }
    }
}
