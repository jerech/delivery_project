package com.paulpwo.delivery305.driver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.base.BaseSpiceActivity;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.models.Driver;
import com.paulpwo.delivery305.request.RetrofitSpiceRequestDriver;
import com.paulpwo.delivery305.utils.Helpers;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;

public class Profile_Drive extends BaseSpiceActivity {

    @BindView(R.id.ImageProfileDriver)    ImageButton imageForDriverProfile;
    @BindView(R.id.first_name)    EditText firstName;
    @BindView(R.id.last_name)    EditText lastName;
    @BindView(R.id.input_address_driver)    EditText inputAddressDriver;
    @BindView(R.id.input_phone_driver)    EditText inputPhoneDriver;
    @BindView(R.id.email)    AutoCompleteTextView email;
    @BindView(R.id.btn_signup)    AppCompatButton btnSignup;

    private String ID;
    private String base64arraydataImage;
    private Context c;
    private  String api_key;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_drive);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadPerfil();
        initImage();
    }

    private void loadPerfil() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                String tmp = Constants.BASE_URL + Helpers.getInstance().readUrlImageDriver(getApplicationContext());
                Picasso.with(getApplicationContext())
                        .load(tmp)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(imageForDriverProfile);
                firstName.setText( Helpers.getInstance().readFirstName(getApplicationContext()) );
                lastName.setText(Helpers.getInstance().readLastName(getApplicationContext()));
                inputAddressDriver.setText(Helpers.getInstance().readAddressName(getApplicationContext()));
                inputPhoneDriver.setText(Helpers.getInstance().readPhone(getApplicationContext()));
                email.setText(Helpers.getInstance().readEmail(getApplicationContext()));
                ID = Helpers.getInstance().readID(getApplicationContext());
                api_key = Helpers.getInstance().readApikey(getApplicationContext());


            }
        });
     /*
        new Thread(new Runnable() {
            public void run() {

            }
        }).start();*/

    }
    private void initImage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageForDriverProfile.buildDrawingCache();
                base64arraydataImage = setBase64Image(imageForDriverProfile.getDrawingCache());
            }
        }, 300);
    }
    @OnClick({R.id.ImageProfileDriver, R.id.btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageProfileDriver:
                load_image();
                break;
            case R.id.btn_signup:
                 callWebService();
                break;
        }
    }
    private void load_image(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == -1){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);

                        base64arraydataImage = setBase64Image(bitmap);

                        Picasso.with(getApplicationContext()).load(selectedImage).into(imageForDriverProfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
    private void callWebService(){
        if(Helpers.isOnline(getApplicationContext())){
        // Mostrar el ProgressDialog en este Thread
        pd = ProgressDialog.show(this, "Process", "Just a moment please!...", true, false);
        RetrofitSpiceRequestDriver queryUnit = new RetrofitSpiceRequestDriver(
                ID,
                firstName.getText().toString(),
                lastName.getText().toString(),
                inputAddressDriver.getText().toString(),
                inputPhoneDriver.getText().toString(),
                email.getText().toString(),
                base64arraydataImage,
                api_key);
        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new ListLoginRequestListenerDriver());
        }else{
            Toast.makeText(getApplicationContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
    }
    private String setBase64Image(Bitmap bitmap){
        //bitmap = imageForDriverProfile.getDrawingCache()
        //imageForDriverProfile.getDrawingCache();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.JPG_QUALITY, out);
        byte[]   dataImage = out.toByteArray();
        return Base64.encodeToString(dataImage, Base64.DEFAULT);
    }






    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class ListLoginRequestListenerDriver implements RequestListener<Driver> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            if(cause.toLowerCase().contains("404")){
                Toast.makeText(getApplicationContext(), "Failed to validate.", Toast.LENGTH_SHORT).show();

            }
            if(cause.toLowerCase().contains("401")){
                Toast.makeText(getApplicationContext(), "Failed to validate their credentials. Please check your username and password.", Toast.LENGTH_SHORT).show();

            }

            else{
                Toast.makeText(getApplicationContext(), "General error unknown.", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();

        }
        @Override
        public void onRequestSuccess(Driver drivers) {

            Log.v("Resul", drivers.toString());
            Helpers.getInstance().saveNewProfileDriver(getApplicationContext(), drivers, null);
            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }
}
