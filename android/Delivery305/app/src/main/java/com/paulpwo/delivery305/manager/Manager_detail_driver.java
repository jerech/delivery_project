package com.paulpwo.delivery305.manager;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.base.BaseSpiceActivity;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.models.Driver;
import com.paulpwo.delivery305.request.RequestSingleDriver;
import com.paulpwo.delivery305.utils.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Manager_detail_driver extends BaseSpiceActivity {
    @BindView(R.id.toolbar)    Toolbar toolbar;
    @BindView(R.id.imageDriver)    ImageView imageDriver;
    @BindView(R.id.name)    TextView driverFirstName;
    @BindView(R.id.textView5)    TextView textView5;
    @BindView(R.id.driver_address)    TextView driverAddress;
    @BindView(R.id.btnCallDriver)    Button btnCallDriver;
    private String id;
    public Driver deliverys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_detail_driver);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        id = getIntent().getExtras().getString("id");
        callWebService();
    }

    private void updateList(Driver deliverys) {
        this.deliverys = deliverys;

        Picasso.with(getApplicationContext())
                .load(Constants.BASE_URL + deliverys.getImage_url())
                .transform(new CircleTransform())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageDriver);
        driverAddress.setText(deliverys.getAddress());



    }


    private void callWebService() {

        RequestSingleDriver queryUnit =
                new RequestSingleDriver(id);

        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new RequestThis());
    }

    @OnClick(R.id.btnCallDriver)
    public void onClick() {
    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public class RequestThis implements RequestListener<Driver> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            getSpiceManager().cancelAllRequests();

        }

        @Override
        public void onRequestSuccess(Driver deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);


        }
    }
}
