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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.interfaces.publicOKhttp;
import com.paulpwo.delivery360.models.DriverDelivery;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDeliveryDetail;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.utils.db.HelpersDB;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jeremias on 8/11/16.
 */

public class WebViewNoteDelivery extends BaseSpiceActivity {

    private int id;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_note_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);

        id = getIntent().getExtras().getInt("id");
        callWebService();

    }


    private void callWebService(){
        if(Helpers.isOnline(getBaseContext())){
            RetrofitSpiceRequestDeliveryDetail queryUnit =
                    new RetrofitSpiceRequestDeliveryDetail(String.valueOf(id));

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.NEVER,
                    new WebViewNoteDelivery.RequestResult());
        }else{
            Toast.makeText(WebViewNoteDelivery.this, "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateNote(String note){


        String mime = "text/html";
        String encoding = "utf-8";


        webView.loadDataWithBaseURL(null, note, mime, encoding, null);

    }



    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class RequestResult implements RequestListener<DriverDelivery> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            updateNote("<h1><b>"+cause+"</b></h1>");
            getSpiceManager().cancelAllRequests();

        }
        @Override
        public void onRequestSuccess(DriverDelivery deliverys) {

            Log.v("Resul", deliverys.toString());
            updateNote(deliverys.getNote());

        }
    }
}
