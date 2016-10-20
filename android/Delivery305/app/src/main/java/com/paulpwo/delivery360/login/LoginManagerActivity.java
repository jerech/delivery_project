package com.paulpwo.delivery360.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.LoaderManager.LoaderCallbacks;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.base.SelectAccountActivity;
import com.paulpwo.delivery360.interfaces.publicOKhttp;
import com.paulpwo.delivery360.main.MainActivity2;
import com.paulpwo.delivery360.models.Manager;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestManager;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginManagerActivity extends BaseSpiceActivity implements LoaderCallbacks<Cursor> ,publicOKhttp {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "paulpwo@gmail.com:dragon", "bar@example.com:world"
    };
    /**
     * Keep track of the Interfacelogin task to ensure we can cancel it if requested.
     */
    // private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private  final Integer  typeAccount = 3;
    private ImageView imageLogin;

    private String mEmail;
    private String mPassword;
    private RetrofitSpiceRequestManager queryUnit;
    private RelativeLayout colorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);


        // Set up the Interfacelogin form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       // imageLogin = (ImageView) findViewById(R.id.user_profile_photo);

        mProgressView = findViewById(R.id.login_progress);
        mEmailSignInButton.setText(mEmailSignInButton.getText().toString() + " (MANAGER)");

        //imageLogin.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_assessment_white_24dp, null));

     /*   colorSet = (RelativeLayout) findViewById(R.id.LayoutChangeColorLogin);
        colorSet.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the Interfacelogin form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual Interfacelogin attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the Interfacelogin attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt Interfacelogin and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Interfacelogin attempt.
            showProgress(true);



            queryUnit = new RetrofitSpiceRequestManager(email,password);
            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new ListLoginRequestListenerManager());







        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    /**
     * Shows the progress UI and hides the Interfacelogin form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            //  mLoginFormView.animate().setDuration(shortAnimTime).alpha(
            //          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            //     @Override
            //     public void onAnimationEnd(Animator animation) {
            //        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            //     }
            //  });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //  mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }




    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ii = new Intent(this,SelectAccountActivity.class);
        startActivity(ii);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, SelectAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onFailureInMainThread(Call call, IOException e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Error to set session on server", Toast.LENGTH_SHORT).show();
                Helpers.getInstance().cancelNotificationNew_delivery(getApplicationContext());
                // mListener.onBackResult();


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

               /* Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);*/
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);

            }
        });
    }




    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================

    public  class ListLoginRequestListenerManager implements RequestListener<Manager> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            if(cause.toLowerCase().contains("404 not found")){
                Toast.makeText(getApplicationContext(), "Failed to validate their credentials. Please check your username and password.", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(), "General error unknown.", Toast.LENGTH_SHORT).show();
            }
            mProgressView.setVisibility(View.INVISIBLE);
        }
        @Override
        public void onRequestSuccess(Manager manager) {
            mProgressView.setVisibility(View.INVISIBLE);
            Log.v("Resul", manager.toString());
            Helpers.getInstance().updateManagerLogin(getApplicationContext(),3,manager,LoginManagerActivity.this);
        }
    }

}
