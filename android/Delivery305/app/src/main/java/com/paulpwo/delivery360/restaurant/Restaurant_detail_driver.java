package com.paulpwo.delivery360.restaurant;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.models.Deliverys;
import com.paulpwo.delivery360.utils.CircleTransform;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.config.Constants;
import com.paulpwo.delivery360.request.RetrofitSpaceRequestDeliveryDeltail;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Restaurant_detail_driver extends BaseSpiceActivity {

    @BindView(R.id.imageDriver)     ImageView imageDriver;
    TextView driverName;
    @BindView(R.id.textView5)     TextView textView5;
    @BindView(R.id.PhoneDriver)    TextView phone_driver;
    @BindView(R.id.textView3)     TextView textView3;
    @BindView(R.id.delivery_address)     TextView deliveryAddress;
    @BindView(R.id.textView6)     TextView textView6;
    @BindView(R.id.tvOnlineTime)     TextView tvTime;
    @BindView(R.id.textView8)     TextView textView8;
    @BindView(R.id.txtNote)     TextView txtNote;
    @BindView(R.id.btnCallDriver)     Button btnCallDriver;
    @BindView(R.id.btnFinish)     Button btnFinish;
    @BindView(R.id.id_delivery)     TextView idDelivery;
    private String id;
    public Deliverys deliverys;


    private void updateList(Deliverys deliverys) {
        this.deliverys = deliverys;

        Picasso.with(getApplicationContext())
                .load(Constants.BASE_URL + deliverys.getImage_url())
                .transform(new CircleTransform())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageDriver);

        driverName.setText(deliverys.getFirst_name() + " " + deliverys.getLast_name());
        phone_driver.setText(deliverys.getPhone());
        deliveryAddress.setText(deliverys.getDelivery_address());
        txtNote.setText(Html.fromHtml(deliverys.getNote()));
        tvTime.setText(deliverys.getTime());



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_driver);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        driverName = (TextView) findViewById(R.id.NameDriver) ;

        id = getIntent().getExtras().getString("id");
        callWebService();

    }


    private void callWebService() {

        RetrofitSpaceRequestDeliveryDeltail queryUnit =
                new RetrofitSpaceRequestDeliveryDeltail(id, Helpers.getInstance().readApikey(getApplicationContext()));

        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new RequestThis());
    }
    private void callRestaurant(){
        new AlertDialog.Builder(Restaurant_detail_driver.this)
                .setTitle("Call to Driver")
                .setMessage("Are you sure you want to call : " + deliverys.getFirst_name() + " " + deliverys.getLast_name() )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + deliverys.getPhone()));
                        if (ActivityCompat.checkSelfPermission(Restaurant_detail_driver.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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


    @OnClick({R.id.btnCallDriver, R.id.btnFinish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCallDriver:
                callRestaurant();
                break;
            case R.id.btnFinish:
                break;
        }
    }
    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public class RequestThis implements RequestListener<Deliverys> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            getSpiceManager().cancelAllRequests();

        }

        @Override
        public void onRequestSuccess(Deliverys deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);



        }
    }

}
