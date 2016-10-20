package com.paulpwo.delivery360.restaurant;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.utils.Helpers;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Restaurant_New_Delivery extends AppCompatActivity {

    @BindView(R.id.toolbar)     Toolbar toolbar;
    @BindView(R.id.input_address_delivery)     EditText inputAddressDelivery;
    @BindView(R.id.textView1)     TextInputLayout textView1;
    @BindView(R.id.input_note_delivery)     EditText inputNoteDelivery;
    @BindView(R.id.view)     TextInputLayout view;
    @BindView(R.id.btnPlaceRequest)     Button btnPlaceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__new__delivery);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btnPlaceRequest)
    public void onClick() {

        atemp();
    }
    private void atemp(){
        // Reset errors.
        inputAddressDelivery.setError(null);
        inputNoteDelivery.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (!isTextValid(inputAddressDelivery.getText().toString())) {
            inputAddressDelivery.setError(getString(R.string.error_invalid_length));
            focusView = inputAddressDelivery;
            cancel = true;
        }
       /* if (!isTextValid(inputNoteDelivery.getText().toString())) {
            inputNoteDelivery.setError(getString(R.string.error_invalid_length));
            focusView = inputNoteDelivery;
            cancel = true;
        }*/
        if (cancel) {
            focusView.requestFocus();
        } else {

            sendData();

        }




    }
    private boolean isTextValid(String t) {
        //TODO: Replace this with your own logic
        return t.length() > 1;
    }
    private void sendData(){
      //  Handler uiHandler = new Handler(Looper.getMainLooper());
      //  uiHandler.post(new Runnable(){
      //      @Override
       //     public void run() {

        String note="No comment";
        if(!inputNoteDelivery.getText().toString().isEmpty()){
            note=inputNoteDelivery.getText().toString();
        }
                Helpers.getInstance().SendnewDelivery(null,getApplicationContext(),
                        inputAddressDelivery.getText().toString(),
                        note,null);
       //     }

      ///  });


        finish();
    }

}
