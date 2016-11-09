package com.paulpwo.delivery360.utils;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.paulpwo.delivery360.config.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceReadEmails extends Service {

    public static final int SERVICE_SYNC_ID = 9760;
    private String email;
    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_MODIFY };

    SharedPreferences sharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("TAG", "SYNC");

        sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);


        email = Helpers.getInstance().readEmailReceiveDeliveries(getApplicationContext());

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mCredential.setSelectedAccountName(email);
        getResultsFromApi();

        stopSelf();

        return START_NOT_STICKY;
    }


    private boolean createFastRequest(String note) {

        RequestBody body;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();

        body = new FormBody.Builder()
                    .add("id_restaurant", Helpers.getInstance().readID(getApplicationContext()))
                    .add("address", "There are a delivery waiting for you.")
                    .add("note",note)
                    .build();

        Request request = new Request.Builder()
                .url(Constants.BASE_URL_NEW_DELIVERY)
                .post(body)
                .header("Authorization" , Helpers.getInstance().readApikey(getApplicationContext()))
                .build();
        boolean result=false;
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                result=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;



    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {


        int typeAccount = sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);

        if(typeAccount==1) {
            Calendar calendar = Calendar.getInstance();

            Log.i("TAG", "Re program service");

            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    calendar.getTimeInMillis() + (1000 * 60 * 2),
                    PendingIntent.getService(this, SERVICE_SYNC_ID, new Intent(this, ServiceReadEmails.class), 0)
            );
        }


    }


    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            return;
        } else if (mCredential.getSelectedAccountName() == null) {
            return;
        } else if (! isDeviceOnline()) {
            Log.e("ERROR","No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
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
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... params) {
            try {
                getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return null;
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         * @return List of Strings labels.
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            String emails = Helpers.getInstance().readEmailsToReceiveDeliveries(getApplicationContext());
            String[] arrFromEmails = emails.split(",");
            String qFrom = "{";
            if(emails.isEmpty()){
                return ;
            }
            for (int i=0;i<arrFromEmails.length;i++){
                qFrom += "from:"+arrFromEmails[i]+" ";

            }
            qFrom+="}";

            String[] arrKeywords =  Helpers.getInstance().readKeywordsSubjectReceiveDeliveries(getApplicationContext()).split(",");
            String qSubject="";
            if(arrKeywords.length!=0){
                qSubject="{";
                for (int i =0;i<arrKeywords.length;i++){
                    qSubject +="subject:\""+arrKeywords[i]+"\" ";
                }
                qSubject+="}";
            }



            // Get the labels in the user's account.
            String user = "me";

            String q = qFrom +" label:UNREAD "+qSubject;
            ListMessagesResponse listResponse =
                    mService.users().messages().list(user).setQ(q).execute();
            if(listResponse.getMessages()==null){
                return;
            }
            for (com.google.api.services.gmail.model.Message messageTemp : listResponse.getMessages()) {

                com.google.api.services.gmail.model.Message message = mService.users().messages().get(user, messageTemp.getId()).execute();
                String mailBody="";
                if(message.getPayload()!=null) {
                    String mimeType = message.getPayload().getMimeType();

                    List<MessagePart> parts = message.getPayload().getParts();
                    if (mimeType.contains("alternative")) {
                        Log.d("READ MESSAGE GMAIL", "entering alternative loop");
                        for (MessagePart part : parts) {
                            mailBody = new String(Base64.decodeBase64(part.getBody()
                                    .getData().getBytes()));

                        }

                    }

                    boolean result = createFastRequest(mailBody);

                    if(result) {

                        ModifyMessageRequest modifyMessageRequest = new ModifyMessageRequest();
                        ArrayList<String> removeLabels = new ArrayList<>();
                        removeLabels.add("UNREAD");
                        modifyMessageRequest.setRemoveLabelIds(removeLabels);
                        mService.users().messages().modify(user, message.getId(), modifyMessageRequest).execute();
                    }
                }


            }
            return ;
        }



        @Override
        protected void onPostExecute(Void output) {

        }

        @Override
        protected void onCancelled() {
            Log.e("ERROR SERVICE", mLastError.getMessage());
        }
    }





}
