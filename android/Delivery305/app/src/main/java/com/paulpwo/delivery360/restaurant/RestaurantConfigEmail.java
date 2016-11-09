package com.paulpwo.delivery360.restaurant;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.google.api.services.gmail.model.Profile;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.adapters.ContributorDemo;
import com.paulpwo.delivery360.base.EmailsCompletionView;
import com.paulpwo.delivery360.utils.Helpers;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by jeremias on 5/11/16.
 */

public class RestaurantConfigEmail extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    private EditText etEmail;
    //private EditText etEmails;
    private EditText etKeywords;
    private EmailsCompletionView emailsCompletionView;
    private AppCompatButton btnSave;
    private AppCompatButton btnPermission;

    ProgressDialog mProgress;

    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";


    private static final String[] SCOPES = { GmailScopes.GMAIL_MODIFY, GmailScopes.GMAIL_READONLY};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_config_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");


        initView();

    }

    private void initView(){

        etEmail = (EditText)findViewById(R.id.etEmail);
        btnPermission = (AppCompatButton) findViewById(R.id.btnPermission);
        //etEmails = (EditText)findViewById(R.id.etEmails);
        etKeywords = (EditText)findViewById(R.id.etKeywords);
        emailsCompletionView = (EmailsCompletionView)findViewById(R.id.searchView);

        Helpers helpers = Helpers.getInstance();
        String email = helpers.readEmailReceiveDeliveries(getApplicationContext());
        String emails = helpers.readEmailsToReceiveDeliveries(getApplicationContext());
        String allEmails = helpers.readAllEmailsToReceiveDeliveries(getApplicationContext());
        String keywords = helpers.readKeywordsSubjectReceiveDeliveries(getApplicationContext());


        etEmail.setText(email);

        String[] arrData= allEmails.split(",");


        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrData);
        FilteredArrayAdapter adapter = new FilteredArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrData) {
            @Override
            protected boolean keepObject(String email, String mask) {
                String[] arrEmail = email.split("@");
                String data1=email;
                String data2="";
                if(arrEmail.length>1){
                    data2 = arrEmail[1];
                }
                mask = mask.toLowerCase();
                return data1.toLowerCase().contains(mask) || data2.toLowerCase().contains(mask);
            }
        };
        emailsCompletionView.setAdapter(adapter);
        String[] arrEmails = emails.split(",");
        if(!emails.isEmpty()) {
            for (String str : arrEmails) {
                emailsCompletionView.addObject(str);
            }
        }
        emailsCompletionView.allowDuplicates(false);
        emailsCompletionView.setThreshold(1);

        //etEmails.setText(emails);
        etKeywords.setText(keywords);


        btnSave = (AppCompatButton)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnPermission.setOnClickListener(this);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


    }


    private void saveData(){
        String email = etEmail.getText().toString();
        List<String> emails = emailsCompletionView.getObjects();
        String strEmails = "";
        int c=0;
        for(String str:emails){
            c++;
            if(c==emails.size()){
                strEmails += str;
            }else {
                strEmails += str + ",";
            }
        }
        ;//etEmails.getText().toString();
        String keywords = etKeywords.getText().toString();

        Helpers helpers = Helpers.getInstance();
        helpers.setEmailReceiveDeliveries(getApplicationContext(), email);
        helpers.setEmailsToReceiveDeliveries(getApplicationContext(), strEmails);
        helpers.setKeywordsSubjectReceiveDeliveries(getApplicationContext(), keywords);

        Toast.makeText(getApplicationContext(), "Saved data", Toast.LENGTH_LONG);

        finish();
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnSave) {
            saveData();
        }else if(v.getId()==R.id.btnPermission){
            btnPermission.setEnabled(false);
            //getResultsFromApi();
            chooseAccount();
            btnPermission.setEnabled(true);

        }
    }


    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Toast.makeText(getApplicationContext(),"No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }


   /* private void requesPermission(){
        UserRecoverableAuthIOException ex = (UserRecoverableAuthIOException) new Exception("Error");
        Intent i =ex.getIntent();
        i.
    }*/

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
           /* if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                etEmail.setText(accountName);
                Helpers.getInstance().setEmailReceiveDeliveries(getApplicationContext(), accountName);
                getResultsFromApi();
            } else {*/
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
           // }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getApplicationContext(),
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        etEmail.setText(accountName);
                        Helpers.getInstance().setEmailReceiveDeliveries(getApplicationContext(), accountName);
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }else{
                    etEmail.setText("");
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                RestaurantConfigEmail.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         * @return List of Strings labels.
         * @throws IOException
         */
        private String getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "me";
            String q = "label:UNREAD";
            Long max = new Long(2);
            ListMessagesResponse listResponse =
                    mService.users().messages().list(user).setQ(q).setMaxResults(max).execute();



            return "";
        }


        @Override
        protected void onPreExecute() {

            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();

        }

        @Override
        protected void onCancelled(){
            mProgress.hide();
            if (mLastError != null){
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            RestaurantConfigEmail.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getApplicationContext(),"The following error occurred:\n"
                            + mLastError.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Request cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }



}
