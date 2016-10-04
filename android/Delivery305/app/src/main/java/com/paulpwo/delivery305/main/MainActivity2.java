package com.paulpwo.delivery305.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.driver.DriverListAvailable;
import com.paulpwo.delivery305.driver.DriverMain;
import com.paulpwo.delivery305.driver.Driver_History;
import com.paulpwo.delivery305.interfaces.publicOKhttp;
import com.paulpwo.delivery305.login.LoginDriverActivity;
import com.paulpwo.delivery305.manager.ManagerMain;
import com.paulpwo.delivery305.manager.ManagerViewDrivers;
import com.paulpwo.delivery305.restaurant.RestaurantHistory;
import com.paulpwo.delivery305.restaurant.RestaurantMain;
import com.paulpwo.delivery305.restaurant.Restaurant_New_Delivery;
import com.paulpwo.delivery305.utils.AppBarStateChangeListener;
import com.paulpwo.delivery305.utils.CircleTransform;
import com.paulpwo.delivery305.utils.Helpers;
import com.paulpwo.delivery305.utils.HelpersMenuDriver;
import com.paulpwo.delivery305.utils.MyService;
import com.paulpwo.delivery305.utils.db.QuotesDataSource;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RestaurantMain.OnFragmentInteractionListener,
        DriverMain.OnFragmentInteractionListener,
        ManagerMain.OnFragmentInteractionListener,
        DriverListAvailable.OnFragmentInteractionListener,
        RestaurantHistory.OnFragmentInteractionListener,publicOKhttp
        {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SectionsPagerAdapterRestaurant mSectionsPagerAdapterRestaurant;
            // Animation
            Animation animation;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

            NavItem[] navigationArray;
            List<NavItem> navigation;
            String ID_BLOCK = "1";
            private ProgressDialog pd = null;
    private static MainActivity2 mainActivityRunningInstance;
    Context context;
    public Integer typeAccount;
    private FrameLayout fragment_container;
    private Boolean NEW= false;
    private static String EMAIL;
    private ImageView imageForPerfil;
    private FloatingActionsMenu rightLabels;
    private Boolean menuLoaded = false;
    Integer IsManager = 0;
    private HelpersMenuDriver menuDriver;
    String first_name;
    String last_name;
    String phone;

            List<Managers> managers;
            int ID_MANAGER_LIST = 0;
            List<Restaurants> restaurants;
            int ID_SELECT_RESTAURANT = 0;



           public Button ICON__COUNTER;
            private IntentFilter myFilter;

            public enum State {
                EXPANDED,
                COLLAPSED,
                IDLE
            }

            private State mCurrentState = State.IDLE;



    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String myParam = intent.getExtras().getString("parameter");
            if (myParam != null) {
                //  Log.d("MyService", "BroadcastReceiver on Activity OK");

                if(MainActivity2.getInstace()!=null)
                    MainActivity2.getInstace().updateUI();

            }
        }
    };
    private String email;
            private DriverMain driverMain;
            private ManagerMain managerMain;
            private RestaurantMain restaurantMain;
            private DriverListAvailable driverListAvailable;
            private RestaurantHistory restaurantHistory;

            public void updateUI() {
        MainActivity2.this.runOnUiThread(new Runnable() {
            public void run() {
                //use findFragmentById for fragments defined in XML ((SimpleFragment)getSupportFragmentManager().findFragmentByTag(fragmentTag)).updateUI(str);
                Integer current = mViewPager.getCurrentItem();
                switch (typeAccount){
                    case 1:
                        if(current==0){
                            restaurantMain =     ((RestaurantMain)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                            if(restaurantMain !=null){
                                restaurantMain.updateUI();
                            }
                        }else{
                            restaurantHistory =     ((RestaurantHistory)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                            if(restaurantHistory !=null){
                                restaurantHistory.updateUI();
                            }
                        }

                        break;
                    case 2:
                        /**************************************************************************
                        * FOR DRIVER TABS
                         **************************************************************************/
                        if(IsManager == 0){
                            if(current==0){
                                driverMain = ((DriverMain)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                                if(driverMain !=null){
                                    driverMain.updateUI();
                                    GET_COUNTS_DELIVERYS_FOR_ANIMATE();
                                }
                            }else{
                                driverListAvailable = ((DriverListAvailable)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                                if(driverListAvailable !=null){
                                    driverListAvailable.updateUI();
                                }
                            }

                        }else{
                            /**************************************************************************
                             * FOR MANAGER TABS
                             **************************************************************************/
                            if(current==0){
                                driverMain =((DriverMain)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                                if(driverMain !=null){
                                    driverMain.updateUI();
                                    GET_COUNTS_DELIVERYS_FOR_ANIMATE();
                                }
                            }else{
                                driverListAvailable =((DriverListAvailable)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                                if(driverListAvailable !=null){
                                    driverListAvailable.updateUI();
                                }
                            }

                        }

                        break;



                }

            }
        });
    }
    public static MainActivity2  getInstace(){
        return mainActivityRunningInstance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        context = this;
        mainActivityRunningInstance =this;


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);




        if( getIntent().getExtras() != null)
        {
            NEW = getIntent().getExtras().getBoolean("new");
        }


        ICON__COUNTER = (Button) findViewById(R.id.start);
        ICON__COUNTER.bringToFront();

        final CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(50,50);

        /*
        * TODO POSITION ICON COUNTER DELIVERIES
         */

        // SET DEFAULT POSITION
        params.setMargins(toolbar.getWidth() - 60,appbar.getHeight() - 27 ,0,0);
        ICON__COUNTER.setLayoutParams(params);

        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if(state.name().contains("COLLAPSED")){

                    params.setMargins(toolbar.getWidth() - 60 ,(appbar.getHeight() / 2) - 27 ,0,0);
                    ICON__COUNTER.setLayoutParams(params);
                }else{
                    params.setMargins(toolbar.getWidth() - 60,appbar.getHeight() - 27,0,0);
                    ICON__COUNTER.setLayoutParams(params);
                }
            }
        });
        /*
        * END TODO END POSITION ICON COUNTER DELIVERIES
        */

        readShared();
        if(typeAccount ==1){
            mSectionsPagerAdapterRestaurant = new SectionsPagerAdapterRestaurant(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapterRestaurant);
            ICON__COUNTER.setVisibility(View.INVISIBLE);




        }else{
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            GET_COUNTS_DELIVERYS_FOR_ANIMATE();

        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Integer i =position;

                switch (typeAccount){
                    case 1: //typeAccount = 1 for RESTAURAT
                        if(i==0 ){
                            if(driverMain !=null){
                                // driverMain.updateUI();
                            }
                        }else{
                           // RestaurantHistory   restaurantHistory =     ((RestaurantHistory)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 1));
                            if(restaurantHistory !=null){
                                restaurantHistory.updateUI();
                            }
                        }
                        break;
                    case 2: //typeAccount =2 for MANAGER & DRIVER
                        if(i==0 ){
                            if(driverMain !=null){
                                // driverMain.updateUI();
                            }
                        }else{
                            if(driverListAvailable !=null){
                                driverListAvailable.updateUI();
                            }
                        }
                        break;
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels);
        final ViewGroup.LayoutParams openM ; //= CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, 40);
        openM =  rightLabels.getLayoutParams();

        rightLabels.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {


            @Override
            public void onMenuExpanded() {
                // fragment_container.setBackgroundColor(ContextCompat.getColor(context, R.color.back_menu_open));
                openM.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                openM.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                rightLabels.setLayoutParams(openM);
            }

            @Override
            public void onMenuCollapsed() {
                openM.height = 20;
                openM.width = 20;
                rightLabels.setLayoutParams(openM);
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

        if(typeAccount == 1){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_block).setVisible(false);
            nav_Menu.findItem(R.id.nav_new_for_restaurant).setVisible(false);


        }
        if(typeAccount == 2){
            if(IsManager == 0 ){
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_block).setVisible(false);
                nav_Menu.findItem(R.id.nav_new_for_restaurant).setVisible(false);
            }

        }

        /*
        *        TODO       INTANCE FOR CREATE NEW DB SQLITE
        */
        QuotesDataSource dataSource = new QuotesDataSource(this);

        /*********************************************************************
         *  start service
         **********************************************************************/
        if(typeAccount ==2){

            Intent serviceIntent = new Intent(getBaseContext(), MyService.class);
            serviceIntent.putExtra("DataType", "DriverMain");
            //serviceIntent.putExtra("model",deliverys );
            startService(serviceIntent);
        }else{
            startService(new Intent(getBaseContext(), MyService.class));
        }





        myFilter = new IntentFilter();
        myFilter.addAction("NOMBRE_DE_NUESTRA_ACTION");
        loadMenus();
        if(NEW){
            if(driverListAvailable !=null){
                mViewPager.setCurrentItem(1);
                driverListAvailable.updateUI();
            }else{
                //driverListAvailable = ((DriverListAvailable)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                mViewPager.setCurrentItem(1);
                //driverListAvailable.updateUI();
            }
        }

    }


            @Override
            public void onNewIntent(Intent newIntent) {
                this.setIntent(newIntent);
                if( getIntent().getExtras() != null)
                {
                    NEW = getIntent().getExtras().getBoolean("new");

                }
                if(NEW){
                    if(driverListAvailable !=null){
                        mViewPager.setCurrentItem(1);
                        driverListAvailable.updateUI();
                    }else{
                        //driverListAvailable = ((DriverListAvailable)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem()));
                        mViewPager.setCurrentItem(1);
                        //driverListAvailable.updateUI();
                    }
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

            menuActionClose();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
/*
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
* TODO SET AVAILABLE COUNT && ANIMATE INIT STATUS WEN INTRO APP
*/
    public void GET_COUNTS_DELIVERYS_FOR_ANIMATE(){
        CharSequence title = mSectionsPagerAdapter.getPageTitle(1);
        //mViewPager.setCurrentItem(1);
       // mSectionsPagerAdapter.updateTitleData(2);

        Helpers.getInstance().countDeliveries(getApplicationContext(),this);
    }
            private  void menuActionClose(){
                new AlertDialog.Builder(context)
                        .setTitle("Exit")
                        .setMessage("Sure you want to quit?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                closeMain();

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



    private void closeMain() {
        //Opening shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);
        final Integer TypeUser = sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
        final String email = sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);
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
                    Helpers.getInstance().logoutRestaurant(getApplicationContext(), email,MainActivity2.this);
                }
                if (TypeUser == 2) {
                    Helpers.getInstance().logoutDriver(getApplicationContext(), email,MainActivity2.this);
                }

        stopService(new Intent(getBaseContext(), MyService.class));
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
/*
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
* TODO LISTENER OK HTTP3 RESPONSES
 */
            @Override
            public void onFailureInMainThread(Call call, IOException e) {
                new DoBackgroundTask().execute("OK");
            }

            @Override
            public void onResponseInMainThread(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                final String responseData = response.body().string();

                new Thread()
                {
                    public void run()
                    {
                        MainActivity2.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                //Do your UI operations like dialog opening or Toast here
                               // showA(responseData);
                                if(responseData.contains("total")){
                                    convert_json_to_Total(responseData);
                                }else{
                                    new DoBackgroundTask().execute("OK");
                                }




                            }
                        });
                    }
                }.start();



            }
/*
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON
* TODO UPDATE COUNTER ICON Total
 */

            public void convert_json_to_Total(String jsonString) {

                Gson gson = new Gson();
                Total[] CountTotal = gson.fromJson(jsonString, Total[].class);

                ICON__COUNTER.setText(CountTotal[0].total);
                if(CountTotal[0].total != "0"){
                    ViewAnimator
                            .animate(ICON__COUNTER)
                            .scale(1f, 0.5f, 1f)
                            .accelerate()
                            .duration(700)
                            .repeatCount(2)
                            .start();

                }

            }



            class Total {

                private String total;
            }

/*
* TODO convert_json_to_Restaurants _______________________________________________________
* TODO convert_json_to_Restaurants _______________________________________________________
 */

            public void convert_json_to_Restaurants(String jsonString) {
                Gson gson = new Gson();
                //Total[] CountTotal = gson.fromJson(jsonString, Total[].class);
                @SuppressWarnings("serial")
                Type collectionType = new TypeToken<List<Restaurants>>() {
                }.getType();
                restaurants = gson.fromJson(jsonString, collectionType);

            }

            class Restaurants {
                private String id;
                private String name;
            }


            private void chooseRestaurant(){
                if(Helpers.isOnline(getApplicationContext())){


                    pd = ProgressDialog.show(this, "Loading", "Just a moment please!...", true, false);
                    OkHttpClient client = new OkHttpClient();
                    Request   request = new Request.Builder()
                            .url(Constants.BASE_URL_RESTAURANTS_LIST)
                            .get()
                            .build();


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            pd.dismiss();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                            final String responseData = response.body().string();

                            new Thread()
                            {
                                public void run()
                                {
                                    MainActivity2.this.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //Do your UI operations like dialog opening or Toast here
                                            showRestaurast(responseData);
                                        }
                                    });
                                }
                            }.start();


                        }
                    });
                }else{
                    Toast.makeText(MainActivity2.this, "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
                }

            }
            private AlertDialog showRestaurastInput( ) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Set up the input
                final TextView lb1 = new TextView(this);
                lb1.setText("Address: ");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                final EditText input2 = new EditText(this);
                input2.setInputType(InputType.TYPE_CLASS_TEXT);
                final TextView lb2 = new TextView(this);
                lb2.setText("Note: ");
                layout.addView(lb1);
                layout.addView(input);
                layout.addView(lb2);
                layout.addView(input2);
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Delivery for " + restaurants.get(ID_SELECT_RESTAURANT).name)
                        .setView(layout);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO TO SHOW NEW DELEVERY
                       final String direcction = input.getText().toString();
                        final String note = input2.getText().toString();

                        final Handler mHandler = new Handler();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Helpers.getInstance().SendnewDelivery(getApplicationContext(),
                                        direcction,
                                        note,restaurants.get(ID_SELECT_RESTAURANT).id);

                            }
                        });




                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();

                return dialog;
            }


            private AlertDialog showRestaurast( String result) {
                convert_json_to_Restaurants(result);

                final CharSequence[] items = new CharSequence[restaurants.size()];
                for (int i=0; i< restaurants.size(); i++){
                    items[i]=restaurants.get(i).name;
                }


                pd.dismiss();

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Choose Restaurant")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                ID_SELECT_RESTAURANT = item ;

                            }
                        });
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO TO SHOW ACTIVITY NEW DELEVERY
                        showRestaurastInput();

                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();

                return dialog;
            }
/*
*  TODO convert_json_to_Restaurants _______________________________________________________
* TODO convert_json_to_Restaurants _______________________________________________________
 */


          private class DoBackgroundTask extends AsyncTask<Object, Void, Void> {

                @Override
                protected Void doInBackground(Object... arg0) {
                    finish();
                    return null;
                }

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


               /* Picasso.with(getApplicationContext())
                        .load(tmp)
                        .transform(new CircleTransform())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(imageForPerfil);*/

                Picasso.with(getApplicationContext())
                        .load(tmp)
                        .transform(new CircleTransform())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
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

        } else if (id == R.id.nav_block) {
            chooseBlock();

        } else if (id == R.id.nav_new_for_restaurant) {
           //TODO NEW DELIVERY FOR RESTAURANT ONLY FUNCITON FOR MANAGER MANAGER
            chooseRestaurant();

        } else if (id == R.id.main_view_menu) {
            readShared();

        } else if (id == R.id.nav_exit) {
            closeMain();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadLoginActivity() {
        Intent iii = new Intent(this, LoginDriverActivity.class);
        iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iii);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void readShared() {
        //Opening shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_SHARE_PREFERENCES, MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.EMAIL_USER_LOGGED, null);
        typeAccount = sharedPreferences.getInt(Constants.TYPE_ACCOUNT_RESTAURAT, 1);
        IsManager = sharedPreferences.getInt(Constants.MANAGER_PRIFILE_INTEGER, 0);


    }
    private void loadMenus(){
        if (email == null) {
            loadLoginActivity();
        } else {
            switch (typeAccount) {
                case 1:
                    //Helpers.getInstance().loadRestaurantMain(this);
                    if (!menuLoaded) {
                        //HelpersMenu.getInstance().setMenuFloatingRestaurant(getApplicationContext(), rightLabels);
                        setMenuFloatingRestaurant();
                        menuLoaded = true;
                    }

                    break;
                case 2:
                    if (IsManager == 0) {
                        //Helpers.getInstance().loadDriverMain(this);
                        if (!menuLoaded) {
                            // menuDriver = new HelpersMenuDriver();
                            // menuDriver.setMenuFloatingDriver(getApplicationContext(),rightLabels);
                            //HelpersMenu.getInstance().setMenuFloatingDriver(getApplicationContext(), rightLabels);
                            setMenuFloatingDriver();
                            menuLoaded = true;
                        }
                    } else {
                        // Helpers.getInstance().loadManagerMain(this);
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


    private void createFastRequest(){

        Helpers.getInstance().SendnewDelivery(getApplicationContext(),
                "There are a delivery waiting for you.",
                "No comment",null );

    }




    public void setMenuFloatingRestaurant( ) {

        com.getbase.floatingactionbutton.FloatingActionButton menuNewFastRequest = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuNewFastRequest.setTitle("Fast Request");
        menuNewFastRequest.setColorNormalResId(R.color.colorNormalNewFastRequest);
        menuNewFastRequest.setColorPressedResId(R.color.colorPressedNewFastRequest);
        menuNewFastRequest.setIcon(R.drawable.ic_event_available_white_18dp);
        rightLabels.addButton(menuNewFastRequest);
        menuNewFastRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createFastRequest();
            }
        });


        com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
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
                Intent i = new Intent(MainActivity2.this, Restaurant_New_Delivery.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton menuCall = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
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
                LOAD_MANAGER_TO_CALL();
            }
        });





    }




    public void setMenuFloatingManager() {

        final com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuNewDelivery.setTitle("Off Line");
        menuNewDelivery.setColorNormalResId(R.color.colorNormalNewDelivery);
        menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
        menuNewDelivery.setIcon(R.drawable.ic_visibility_off_white_18dp);
        rightLabels.addButton(menuNewDelivery);
        menuNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOffline(menuNewDelivery);
                rightLabels.collapse();
            }
        });
/*        com.getbase.floatingactionbutton.FloatingActionButton menuNotice = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuNotice.setTitle("Notice");
        menuNotice.setColorNormalResId(R.color.colorPrimary);
        menuNotice.setColorPressedResId(R.color.colorPrimaryDark);
        menuNotice.setIcon(R.drawable.ic_event_available_white_18dp);
        rightLabels.addButton(menuNotice);
        menuNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightLabels.collapse();
                Intent i = new Intent(MainActivity2.this, ManagerNoticeList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });*/
/*        com.getbase.floatingactionbutton.FloatingActionButton menuNotice = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuNotice.setTitle("Block driver");
        menuNotice.setColorNormalResId(R.color.colorPrimary);
        menuNotice.setColorPressedResId(R.color.colorPrimaryDark);
        menuNotice.setIcon(R.drawable.ic_block_white_18dp);
        rightLabels.addButton(menuNotice);
        menuNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightLabels.collapse();
                chooseBlock();

            }
        });*/

        com.getbase.floatingactionbutton.FloatingActionButton menuDrivers = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuDrivers.setTitle("Drivers Working");
        menuDrivers.setColorNormalResId(R.color.colorPrimary);
        menuDrivers.setColorPressedResId(R.color.colorPrimaryDark);
        menuDrivers.setIcon(R.drawable.ic_list_white_18dp);
        rightLabels.addButton(menuDrivers);
        menuDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightLabels.collapse();
                Intent i = new Intent(MainActivity2.this, ManagerViewDrivers.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton menuHistory = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuHistory.setTitle("History");
        menuHistory.setColorNormalResId(R.color.colorPrimary2);
        menuHistory.setColorPressedResId(R.color.colorPrimaryDark);
        menuHistory.setIcon(R.drawable.ic_history_white_18dp);
        rightLabels.addButton(menuHistory);
        menuHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLabels.collapse();
                Intent i = new Intent(MainActivity2.this, Driver_History.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                //Toast.makeText(MainActivity2.this, "not implemented yet in this beta version", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void setMenuFloatingDriver() {

        final com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuNewDelivery.setTitle("Off Line");
        menuNewDelivery.setColorNormalResId(R.color.colorNormalNewDelivery);
        menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
        menuNewDelivery.setIcon(R.drawable.ic_visibility_off_white_18dp);
        rightLabels.addButton(menuNewDelivery);
        menuNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOffline(menuNewDelivery);
                rightLabels.collapse();
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton menuHistory = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuHistory.setTitle("History");
        menuHistory.setColorNormalResId(R.color.colorPrimary2);
        menuHistory.setColorPressedResId(R.color.colorPrimaryDark);
        menuHistory.setIcon(R.drawable.ic_history_white_18dp);
        rightLabels.addButton(menuHistory);
        menuHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLabels.collapse();
                Intent i = new Intent(MainActivity2.this, Driver_History.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                //Toast.makeText(MainActivity2.this, "not implemented yet in this beta version", Toast.LENGTH_SHORT).show();
            }
        });


        com.getbase.floatingactionbutton.FloatingActionButton menuCall = new com.getbase.floatingactionbutton.FloatingActionButton(MainActivity2.this);
        menuCall.setTitle("Call manager");
        menuCall.setColorNormalResId(R.color.blue_semi_transparent);
        menuCall.setColorPressedResId(R.color.blue_semi_transparent_pressed);
        menuCall.setIcon(R.drawable.ic_call_white_24dp);
        rightLabels.addButton(menuCall);
        menuCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLabels.collapse();
                LOAD_MANAGER_TO_CALL();

            }
        });

    }

            private void chooseBlock(){
                if(Helpers.isOnline(getApplicationContext())){


                pd = ProgressDialog.show(this, "Loading", "Just a moment please!...", true, false);
                OkHttpClient client = new OkHttpClient();
                Request   request = new Request.Builder()
                        .url(Constants.BASE_URL_LIST_BLOCK)
                        .get()
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        pd.dismiss();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }
                        final String responseData = response.body().string();

                        new Thread()
                        {
                            public void run()
                            {
                                MainActivity2.this.runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        //Do your UI operations like dialog opening or Toast here
                                        showA(responseData);
                                    }
                                });
                            }
                        }.start();


                    }
                });
                }else{
                    Toast.makeText(MainActivity2.this, "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
                }

            }
            private AlertDialog showA( String result) {
                convert_json_to_array_or_list(result);

                final CharSequence[] items = new CharSequence[navigation.size()];
                for (int i=0; i< navigation.size(); i++){
                    items[i]=navigation.get(i).first_name + " " + navigation.get(i).last_name;
                }


                pd.dismiss();

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Choose User to block")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                        ID_BLOCK = navigation.get(item).id ;

                            }
                        });
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        blockUser();

                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();

                return dialog;
            }

            public void convert_json_to_array_or_list(String jsonString) {

                Gson gson = new Gson();
                navigationArray = gson.fromJson(jsonString, NavItem[].class);
                @SuppressWarnings("serial")
                Type collectionType = new TypeToken<List<NavItem>>() {
                }.getType();
                navigation = gson.fromJson(jsonString, collectionType);

            }

            class NavItem {

                private String id;
                private String first_name;
                private String last_name;

            }

            private void blockUser(){
                //ID_BLOCK
                Helpers.getInstance().BlockUser(getApplicationContext(),ID_BLOCK);

            }
    private void callM(final int ID){
        new AlertDialog.Builder(context)
                .setTitle("Call to Manager")
                .setMessage("Are you sure you want to call : " + managers.get(ID).first_name + " " + managers.get(ID).last_name)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + managers.get(ID).phone.toString()));
                        if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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


    private void setOffline(final com.getbase.floatingactionbutton.FloatingActionButton menuNewDelivery) {
        if (menuNewDelivery.getTitle() == "Off Line") {
            new AlertDialog.Builder(context)
                    .setTitle("Off line")
                    .setMessage("Are you sure set off line mode?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Helpers.getInstance().setOffLine(getBaseContext());
                            menuNewDelivery.setTitle("On Line");
                            menuNewDelivery.setColorNormalResId(R.color.secondary_text);
                            menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
                            menuNewDelivery.setIcon(R.drawable.ic_visibility_white_18dp);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        } else {
                new AlertDialog.Builder(context)
                        .setTitle("Off line")
                        .setMessage("Are you sure set On line mode?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Helpers.getInstance().setOnLine(getBaseContext());
                                menuNewDelivery.setTitle("Off Line");
                                menuNewDelivery.setColorNormalResId(R.color.colorNormalNewDelivery);
                                menuNewDelivery.setColorPressedResId(R.color.colorPressedNewDelivery);
                                menuNewDelivery.setIcon(R.drawable.ic_visibility_off_white_18dp);
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
    }
    private void LOAD_MANAGER_TO_CALL() {
        pd = ProgressDialog.show(this, "Loading", "Just a moment please!...", true, false);
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


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                            final String responseData = response.body().string();

                            new Thread()
                            {
                                public void run()
                                {
                                    MainActivity2.this.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //Do your UI operations like dialog opening or Toast here
                                            showManagers(responseData);
                                        }
                                    });
                                }
                            }.start();
                        }
                    });

                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }
        });
        thread.start();


    }
            public void convert_json_to_Manager(String jsonString) {

                Gson gson = new Gson();
                //Managers[] managers= gson.fromJson(jsonString, Managers[].class);
                @SuppressWarnings("serial")
                Type collectionType = new TypeToken<List<Managers>>() {
                }.getType();
                managers = gson.fromJson(jsonString, collectionType);

            }
            class Managers{
                private String id;
                private  String first_name;
                private String last_name;
                private String phone;
            }


            private AlertDialog showManagers( String result) {
                convert_json_to_Manager(result);

                final CharSequence[] items = new CharSequence[managers.size()];
                for (int i=0; i< managers.size(); i++){
                    items[i]=managers.get(i).first_name + " " + managers.get(i).last_name;
                }


                pd.dismiss();

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Choose who to call")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                ID_MANAGER_LIST = item;

                            }
                        });
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       //TODO CALL MANAGER
                        callM(ID_MANAGER_LIST);

                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();

                return dialog;
            }





    public void loadAvailable(){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

       // closeMain();
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Integer FoundItems =  0;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fm= null;
            switch (position){
                case 0:
                    if(IsManager==0){
                           fm = DriverMain.newInstance("","");
                        }else{
                            fm = DriverMain.newInstance("","");
                    }

                    break;
                case 1:
                            fm = DriverListAvailable.newInstance("","");

                    break;
            }
            return fm;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence tmp = null;
                switch (position) {
                    case 0:
                           tmp = "Current Deliveries";
                        break;
                    case 1:
                        if (this.FoundItems == 0 ){
                            tmp = "Available Deliveries";
                        }else{
                            tmp = "Available Deliveries (" + this.FoundItems.toString() + ")";
                        }

                        break;
                }

            return tmp;
        }

        public void updateTitleData(int notFoundItems) {
            this.FoundItems = notFoundItems;
            notifyDataSetChanged();
        }




    }





    public class SectionsPagerAdapterRestaurant extends FragmentPagerAdapter {

        public SectionsPagerAdapterRestaurant(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fm= null;
            switch (position){
                case 0:
                        fm =  RestaurantMain.newInstance("","");
                    break;
                case 1:
                        fm =  RestaurantHistory.newInstance("","");

                    break;

            }
            return fm;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence tmp = null;
            switch (position) {
                case 0:
                    tmp = "Deliveries";
                    break;
                case 1:
                    tmp = "My History";
                    break;
            }
            return tmp;
        }
    }




            public static void trimCache(Context context) {
                try {
                    File dir = context.getCacheDir();
                    if (dir != null && dir.isDirectory()) {
                        deleteDir(dir);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            public static boolean deleteDir(File dir) {
                if (dir != null && dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        boolean success = deleteDir(new File(dir, children[i]));
                        if (!success) {
                            return false;
                        }
                    }
                }

                // The directory is now empty so delete it
                return dir.delete();
            }

}
