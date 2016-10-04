package com.paulpwo.delivery305.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paulpwo.delivery305.utils.MyService;
import com.paulpwo.delivery305.login.LoginDriverActivity;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.driver.DriverMain;
import com.paulpwo.delivery305.manager.ManagerMain;
import com.paulpwo.delivery305.manager.ManagerNoticeList;
import com.paulpwo.delivery305.restaurant.RestaurantMain;
import com.paulpwo.delivery305.restaurant.Restaurant_New_Delivery;
import com.paulpwo.delivery305.utils.CircleTransform;
import com.paulpwo.delivery305.utils.Helpers;
import com.paulpwo.delivery305.utils.HelpersMenuDriver;
import com.paulpwo.delivery305.utils.db.QuotesDataSource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RestaurantMain.OnFragmentInteractionListener,
        DriverMain.OnFragmentInteractionListener,
        ManagerMain.OnFragmentInteractionListener {
    private static MainActivity mainActivityRunningInstance;
    Context context;
    public Integer typeAccount;
    private FrameLayout fragment_container;

    private static String EMAIL;
    private ImageView imageForPerfil;
    private FloatingActionsMenu rightLabels;
    private Boolean menuLoaded = false;
    Integer IsManager = 0;
    private HelpersMenuDriver menuDriver;
    String first_name;
    String last_name;
    String phone;


    private IntentFilter myFilter;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String myParam = intent.getExtras().getString("parameter");
            if (myParam != null) {
              //  Log.d("MyService", "BroadcastReceiver on Activity OK");

                if(MainActivity.getInstace()!=null)
                    MainActivity.getInstace().updateUI();

            }
        }
    };
    public void updateUI() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //use findFragmentById for fragments defined in XML ((SimpleFragment)getSupportFragmentManager().findFragmentByTag(fragmentTag)).updateUI(str);
               switch (typeAccount){
                   case 1:
                       RestaurantMain tmp =     ((RestaurantMain)getSupportFragmentManager().findFragmentByTag(Constants.TAG_RESTAURANT));
                       if(tmp !=null){
                           tmp.updateUI();
                       }
                       break;
                   case 2:
                       if(IsManager == 0){
                           DriverMain tmp2 = ((DriverMain)getSupportFragmentManager().findFragmentByTag(Constants.TAG_DRIVER));
                           if(tmp2 !=null){
                               tmp2.updateUI();
                           }
                       }else{
                           ManagerMain tmp3 =((ManagerMain)getSupportFragmentManager().findFragmentByTag(Constants.TAG_MANAGER));
                           if(tmp3 !=null){
                               tmp3.updateUI();
                           }
                       }

                       break;



               }

            }
        });
    }
    public static MainActivity  getInstace(){
        return mainActivityRunningInstance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mainActivityRunningInstance =this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels);
        rightLabels.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                // fragment_container.setBackgroundColor(ContextCompat.getColor(context, R.color.back_menu_open));
            }

            @Override
            public void onMenuCollapsed() {
                /// fragment_container.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        rightLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightLabels.isExpanded()) {
                    rightLabels.collapse();
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                updateImageMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        readShared();

        /*
        *               INTANCE FOR CREATE NEW DB SQLITE
        */
        QuotesDataSource dataSource = new QuotesDataSource(this);
        LoadManager();
        startService(new Intent(getBaseContext(), MyService.class));

       myFilter = new IntentFilter();
        myFilter.addAction("NOMBRE_DE_NUESTRA_ACTION");

    }

    private void updateImageMenu() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                String tmp = Constants.BASE_URL + Helpers.getInstance().readUrlImageDriver(getApplicationContext());
                imageForPerfil = (ImageView) findViewById(R.id.image_header);
                Picasso.with(getApplicationContext()).invalidate(tmp);
                imageForPerfil.setImageDrawable(null);
                imageForPerfil.setImageURI(null);


                Picasso.with(getApplicationContext())
                        .load(tmp)
                        .transform(new CircleTransform())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(imageForPerfil);
                TextView tv = (TextView) findViewById(R.id.NameForUser);
                tv.setText(Helpers.getInstance().readName(context));

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_logout) {

            closeMain();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void closeMain() {
        //Opening shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);
        Integer TypeUser = sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
        String email = sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // ESTORE EMAIL
        editor.putString(Constants.EMAIL_USER_LOGGED, null);
        editor.putString(Constants.image_url, null);
        editor.putString(Constants.API_KEY, null);
        editor.putString(Constants.TOKEN_PUSH, null);
        editor.putString(Constants.address, null);
        editor.putString(Constants.first_name, null);
        editor.putString(Constants.last_name, null);
        editor.putString(Constants.phone, null);
        editor.putInt(Constants.MANAGER_PRIFILE_INTEGER, 0);
        //Applying the changes on sharedpreferences
        editor.apply();
        if (TypeUser == 1) {
           // Helpers.getInstance().logoutRestaurant(getApplicationContext(), email);
        }
        if (TypeUser == 2) {
          //  Helpers.getInstance().logoutDriver(getApplicationContext(), email);
        }
       /* if(TypeUser == 3){
            Helpers.getInstance().logoutManager(getApplicationContext(),email);
        }*/

        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            // Handle the camera action
            switch (typeAccount) {
                case 1:
                    Helpers.getInstance().loadRestaurantProfile(this);
                    break;
                case 2:
                    Helpers.getInstance().loadDriveProfile(this);
                    break;
                case 3:

                    break;
            }


        } else if (id == R.id.main_view_menu) {
            readShared();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void readShared() {
        //Opening shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);
        String email = sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);
        typeAccount = sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
        IsManager = sharedPreferences.getInt(Constants.MANAGER_PRIFILE_INTEGER, 0);

        if (email == null) {
            loadLoginActivity();
        } else {
            switch (typeAccount) {
                case 1:
                    Helpers.getInstance().loadRestaurantMain(this);
                    if (!menuLoaded) {
                        //HelpersMenu.getInstance().setMenuFloatingRestaurant(getApplicationContext(), rightLabels);
                        setMenuFloatingRestaurant();
                        menuLoaded = true;
                    }

                    break;
                case 2:
                    if (IsManager == 0) {
                        Helpers.getInstance().loadDriverMain(this);
                        if (!menuLoaded) {
                            // menuDriver = new HelpersMenuDriver();
                            // menuDriver.setMenuFloatingDriver(getApplicationContext(),rightLabels);
                            //HelpersMenu.getInstance().setMenuFloatingDriver(getApplicationContext(), rightLabels);
                            setMenuFloatingDriver();
                            menuLoaded = true;
                        }
                    } else {
                        Helpers.getInstance().loadManagerMain(this);
                        if (!menuLoaded) {
                           // HelpersMenu.getInstance().setMenuFloatingManager(getApplicationContext(), rightLabels);
                            setMenuFloatingManager();
                            menuLoaded = true;
                        }
                    }

                    break;
             /*   case 3:
                    Helpers.getInstance().loadManagerMain(this);
                    if(!menuLoaded) {
                        HelpersMenu.getInstance().setMenuFloatingManager(getApplicationContext(), rightLabels);
                        menuLoaded = true;
                    }
                    break;*/
            }
        }
    }

    private void loadLoginActivity() {
        Intent iii = new Intent(this, LoginDriverActivity.class);
        iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iii);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void setMenuFloatingRestaurant( ) {

        com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuNewDelivery.setTitle("New Delivery");
        menuNewDelivery.setColorNormalResId(R.color.colorNormalNewDelivery);
        menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
        menuNewDelivery.setIcon(R.drawable.ic_event_available_white_18dp);
        rightLabels.addButton(menuNewDelivery);
        menuNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(c, "HOLA PAUL", Toast.LENGTH_SHORT).show();
                rightLabels.collapse();
                Intent i = new Intent(MainActivity.this, Restaurant_New_Delivery.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton menuHistory = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuHistory.setTitle("History");
        menuHistory.setColorNormalResId(R.color.colorPrimary);
        menuHistory.setColorPressedResId(R.color.colorPrimaryDark);
        menuHistory.setIcon(R.drawable.ic_history_white_18dp);
        rightLabels.addButton(menuHistory);
        menuHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "HOLA PAUL", Toast.LENGTH_SHORT).show();
                rightLabels.collapse();
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton menuCall = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuCall.setTitle("Call manager");
        menuCall.setColorNormalResId(R.color.blue_semi_transparent);
        menuCall.setColorPressedResId(R.color.blue_semi_transparent_pressed);
        menuCall.setIcon(R.drawable.ic_call_white_24dp);
        rightLabels.addButton(menuCall);
        menuCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(c, "HOLA PAUL", Toast.LENGTH_SHORT).show();
                rightLabels.collapse();
                callM();
            }
        });

    }




    public void setMenuFloatingManager() {

        com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuNewDelivery.setTitle("Not Implemented");
        menuNewDelivery.setColorNormalResId(R.color.colorNormalNewDelivery);
        menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
        menuNewDelivery.setIcon(R.drawable.ic_event_available_white_18dp);
        rightLabels.addButton(menuNewDelivery);
        menuNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "HOLA PAUL", Toast.LENGTH_SHORT).show();
                rightLabels.collapse();
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton menuHistory = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuHistory.setTitle("Notice");
        menuHistory.setColorNormalResId(R.color.colorPrimary);
        menuHistory.setColorPressedResId(R.color.colorPrimaryDark);
        menuHistory.setIcon(R.drawable.ic_event_available_white_18dp);
        rightLabels.addButton(menuHistory);
        menuHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightLabels.collapse();
                Intent i = new Intent(MainActivity.this, ManagerNoticeList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });


    }
    public void setMenuFloatingDriver() {


/*       final com.getbase.floatingactionbutton.FloatingActionButton menuNewOff = new com.getbase.floatingactionbutton.FloatingActionButton(c);
        menuNewOff.setTitle("Set Offline");
        menuNewOff.setColorNormalResId(R.color.colorNormalNewDelivery);
        menuNewOff.setColorPressedResId( R.color.colorPressedNewDelivery);
        menuNewOff.setIcon(R.drawable.ic_visibility_off_white_18dp);
        menu.addButton(menuNewOff);
        menuNewOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c, "HOLA PAUL", Toast.LENGTH_SHORT).show();
                if(menuNewOff.getTitle() == "Set Offline"){
                    menuNewOff.setTitle("Set Online");
                    menuNewOff.setIcon(R.drawable.ic_visibility_off_white_18dp);
                    menuNewOff.setColorNormalResId(R.color.half_black);
                    menuNewOff.setColorPressedResId( R.color.blue_semi_transparent_pressed);
                }else{
                    menuNewOff.setTitle("Set offline");
                    menuNewOff.setIcon(R.drawable.ic_visibility_white_18dp);
                    menuNewOff.setColorNormalResId(R.color.colorNormalNewDelivery);
                    menuNewOff.setColorPressedResId( R.color.colorPressedNewDelivery);
                }
                menu.collapse();
            }
        });
        */


        com.getbase.floatingactionbutton.FloatingActionButton menuHistory = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuHistory.setTitle("History");
        menuHistory.setColorNormalResId(R.color.colorPrimary);
        menuHistory.setColorPressedResId(R.color.colorPrimaryDark);
        menuHistory.setIcon(R.drawable.ic_history_white_18dp);
        rightLabels.addButton(menuHistory);
        menuHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLabels.collapse();
            }
        });


        com.getbase.floatingactionbutton.FloatingActionButton menuCall = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity.this);
        menuCall.setTitle("Call manager");
        menuCall.setColorNormalResId(R.color.blue_semi_transparent);
        menuCall.setColorPressedResId(R.color.blue_semi_transparent_pressed);
        menuCall.setIcon(R.drawable.ic_call_white_24dp);
        rightLabels.addButton(menuCall);
        menuCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLabels.collapse();
                callM();

            }
        });

    }
    private void callM(){
        new AlertDialog.Builder(context)
                .setTitle("Call to Manager")
                .setMessage("Are you sure you want to call : " + first_name + " " + last_name)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.toString()));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void LoadManager() {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Response response;
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    final Request request = new Request.Builder()
                            .url(Constants.BASE_URL_PROFILE_MANAGER)
                            .get()
                            .build();


                    try {
                        response=   client.newCall(request).execute();

                        try {
                            String jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);
                            first_name = Jobject.getString("first_name");
                            last_name = Jobject.getString("last_name");
                            phone = Jobject.getString("phone");
                            // new TaskCallManager().execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }
        });
        thread.start();



  /*      AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
       unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, myFilter);
    }
}
