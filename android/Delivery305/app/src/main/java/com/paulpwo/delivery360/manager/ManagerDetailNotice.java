package com.paulpwo.delivery360.manager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.models.NoticeDelivery;
import com.paulpwo.delivery360.request.RetrofitSpaceRequestOneDeliveryByNotice;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.utils.db.HelpersDB;
import com.paulpwo.delivery360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerDetailNotice extends BaseSpiceActivity {

    String id_delivery;
    String id_db;
    String phone;

    @BindView(R.id.name)
    TextView restaurantName;
    @BindView(R.id.driver_address)
    TextView restaurantAddress;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.delivery_address)
    TextView deliveryAddress;
    @BindView(R.id.tvOnlineTime)
    TextView tvtime;
    @BindView(R.id.btn_call_restaurant)
    Button btnCallRestaurant;
    @BindView(R.id.btnAssignDriver)
    Button btnAssignDriver;
    @BindView(R.id.button)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_detail_notice);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id_delivery = getIntent().getExtras().getString("id_delivery");
        id_db = getIntent().getExtras().getString("id_db");
        callWebService();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void callWebService() {
        String apiKey = Helpers.getInstance().readApikey(ManagerDetailNotice.this);

        RetrofitSpaceRequestOneDeliveryByNotice queryUnit =
                new RetrofitSpaceRequestOneDeliveryByNotice(id_delivery, apiKey);

        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new NoticeRequestListener());
    }

    private void updateList(NoticeDelivery noticeDelivery) {
        restaurantName.setText(noticeDelivery.getRestaurant_name());
        restaurantAddress.setText(noticeDelivery.getRestaurant_address());
        deliveryAddress.setText(noticeDelivery.getDelivery_address());
        tvtime.setText(noticeDelivery.getTime());
        phone = noticeDelivery.getPhone();

    }

    @OnClick({R.id.btn_call_restaurant, R.id.btnAssignDriver, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call_restaurant:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.toString()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                startActivity(intent);


                break;
            case R.id.btnAssignDriver:
                break;
            case R.id.button:
                Helpers.getInstance().deleteDelivery(ManagerDetailNotice.this,id_delivery);
                HelpersDB.getInstance().delete(ManagerDetailNotice.this,id_db);

                finish();

                break;
        }
    }
    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public class NoticeRequestListener implements RequestListener<NoticeDelivery> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            if (cause.toLowerCase().contains("404")) {
                Toast.makeText(ManagerDetailNotice.this, "Failed to validate.", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onRequestSuccess(NoticeDelivery noticeDelivery) {

            Log.v("Resul", noticeDelivery.toString());
            updateList(noticeDelivery);
            Toast.makeText(ManagerDetailNotice.this, "Success!", Toast.LENGTH_SHORT).show();


        }
    }
}
