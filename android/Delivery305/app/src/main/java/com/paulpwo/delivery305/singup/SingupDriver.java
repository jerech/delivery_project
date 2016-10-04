package com.paulpwo.delivery305.singup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SingupDriver extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    @BindView(R.id.first_name) EditText firstName;
    @BindView(R.id.last_name) EditText lastName;
    @BindView(R.id.input_address_driver) EditText inputAddressDriver;
    @BindView(R.id.input_phone_driver) EditText inputPhoneDriver;
    @BindView(R.id.email) AutoCompleteTextView email;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.input_password2) EditText inputPassword2;
    @BindView(R.id.image_for_driver_profile) ImageButton imageForDriverProfile;
    @BindView(R.id.btn_signup) AppCompatButton btnSignup;
    @BindView(R.id.link_login) TextView linkLogin;
    //@BindView(R.id.fab) FloatingActionButton fab;
    private Uri selectedImage;
    private String base64arraydataImage;
    private ProgressDialog pd = null;
    private Boolean imageLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup_driver);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.image_for_driver_profile, R.id.btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_for_driver_profile:
                load_image();
                break;
            case R.id.btn_signup:
                attemptLogin();
                break;
        }
    }

    private void load_image(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.JPG_QUALITY, out);
                        byte[]   dataImage = out.toByteArray();
                        base64arraydataImage = Base64.encodeToString(dataImage, Base64.DEFAULT);
                        imageLoad = true;
                        Picasso.with(getApplicationContext()).load(selectedImage).into(imageForDriverProfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
    private void attemptLogin() {

        // Reset errors.
        firstName.setError(null);
        lastName.setError(null);
        inputAddressDriver.setError(null);
        inputPhoneDriver.setError(null);
        email.setError(null);
        inputPassword.setError(null);
        inputPassword2.setError(null);



        // Store values at the time of the Interfacelogin attempt.
        String tinputName = firstName.getText().toString();
        String tlastName = lastName.getText().toString();
        String tinputAddressDriver = inputAddressDriver.getText().toString();
        String tinputPhoneDriver = inputPhoneDriver.getText().toString();
        String temail = email.getText().toString();
        String tinputPassword = inputPassword.getText().toString();
        String tinputPassword2 = inputPassword2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(imageForDriverProfile.getDrawable() == null){
            cancel = true;
            focusView = imageForDriverProfile;
            Toast.makeText(SingupDriver.this,
                    getResources().getText(R.string.error_image_profile), Toast.LENGTH_SHORT).show();
        }
        // Check for a valid firstName, if the user entered one.
        if (!isTextValid(tinputName)) {
            firstName.setError(getString(R.string.error_invalid_length));
            focusView = firstName;
            cancel = true;

        }
        // Check for a valid lastName, if the user entered one.
        if (!isTextValid(tlastName)) {
            lastName.setError(getString(R.string.error_invalid_length));
            focusView = lastName;
            cancel = true;

        }



        // Check for a valid inputAddressDriver, if the user entered one.
        if (!isTextValid(tinputAddressDriver)) {
            inputAddressDriver.setError(getString(R.string.error_invalid_length));
            focusView = inputAddressDriver;
            cancel = true;

        }


        // Check for a valid inputPhoneDriver, if the user entered one.
        if (!isTextValid(tinputPhoneDriver)) {
            inputPhoneDriver.setError(getString(R.string.error_invalid_length));
            focusView = inputPhoneDriver;
            cancel = true;

        }



        // Check for a valid password, if the user entered one.
        if (!isEmailValid(temail)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isTextValid(tinputPassword)) {
            inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = inputPassword;
            cancel = true;
        }

        if(!isSamePassword(tinputPassword, tinputPassword2)){
            inputPassword2.setError(getString(R.string.error_not_same_passwords));
            focusView = inputPassword2;
            cancel = true;
        }

        if(!imageLoad){
            cancel = true;
            focusView = imageForDriverProfile;
            Toast.makeText(SingupDriver.this, "Select image pleace!", Toast.LENGTH_SHORT).show();
        }


        if (cancel) {
            // There was an error; don't attempt Interfacelogin and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // CALL TO WEB SERVICE REGISTER
            sendRegisterDriver(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    inputAddressDriver.getText().toString(),
                    inputPhoneDriver.getText().toString(),
                    inputPassword.getText().toString(),
                    email.getText().toString(),
                    base64arraydataImage); }



    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        Boolean tmp = email.contains("@");
        tmp= email.contains(".");
        return tmp;
    }

    private boolean isTextValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    private boolean isSamePassword(String password, String password2){
        if(password.equals(password2)){
            return true;
        }else{
            return false;
        }

    }
    public void sendRegisterDriver(String tinputName,
                                   String tlastName,
                                   String tinputAddressDriver,
                                   String tinputPhoneDriver,
                                   String tinputPassword,
                                   String temail,
                                   String base64arraydataImage){
        pd = ProgressDialog.show(this, "Process", "Just a moment please!...", true, false);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("first_name", tinputName)
                .add("last_name", tlastName)
                .add("address", tinputAddressDriver)
                .add("phone", tinputPhoneDriver)
                .add("email", temail)
                .add("password", tinputPassword)
                .add("image_url", base64arraydataImage)
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL_DRIVER_SINGUP)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String res = call.toString();
                Log.v("FAILURE ", res);

                new Thread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed response server.", Toast.LENGTH_SHORT).show();
                    }
                }).start();
                pd.dismiss();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.v("SERVER RESGISTER", res);

                pd.dismiss();
                finish();

            }
        });

    }
}
