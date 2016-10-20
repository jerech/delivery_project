package com.paulpwo.delivery360.driver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.adapters.RecyclerAdapterDriverDeliveryList;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.manager.ManagerDeliveryHistoty;
import com.paulpwo.delivery360.request.RetrofitSpaceRequestDriverHistory;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDriverDeliveryList;
import com.paulpwo.delivery360.restaurant.RestaurantHistory;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.utils.db.HelpersDB;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.models.DriverDelivery;
import com.paulpwo.delivery360.utils.DividerItemDecoration;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.paulpwo.delivery360.R.id.calendarView;

public class Driver_History extends BaseSpiceActivity implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;
    private RecyclerAdapterDriverDeliveryList adapter;
    private DriverDelivery.List deliverys;
    RecyclerView recyclerView;
    private RetrofitSpiceRequestDriverDeliveryList queryUnit;

    static String startDate="";
    static String endDate="";
    protected static MaterialCalendarView calendarView;
    private Button btnDate;
    static SimpleDateFormat formatDate;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

       // progressBar = (ProgressBar) findViewById(R.id.progressBar);
       // progressBar.setVisibility(View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        //datePickerDialog.setStyle(DatePickerDialog.,R.style.AppTheme);



        formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = formatDate.format(calendar.getTime());
        endDate = formatDate.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate=formatDate.format(calendar.getTime());

        //loadDB();
        swipeRefreshLayout.setRefreshing(true);

        btnDate = (Button) findViewById(R.id.btn_dialog_calendar);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
                dialogFragment.setmManagerHistory(Driver_History.this);
                dialogFragment.show(getSupportFragmentManager(), "Select Date");

            }
        });

        if(!endDate.isEmpty()){
            btnDate.setText("From " + startDate + " to " + endDate);
        }else{
            btnDate.setText("" + startDate);
        }

        callWebService();

    }
    @Override
    public void onStart() {
        super.onStart();
        //loadDB();
    }
    public void setRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }
    private void loadDB(){

        if (deliverys != null){
            deliverys.clear();

        }else{
            this.deliverys = new DriverDelivery.List();
        }


        SQLiteDatabase db = HelpersDB.getInstance().getDB(getApplicationContext());
        String[] columns = {"id","address", "note", "time", "time_driver","status",
                "id_restaurant", "restaurant","phone_restaurant","restaurant_address"};
        Cursor cursor = db.query("deliveries", columns, "status=0", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new Games object and retrieve the data from the cursor to be stored in this Games object
                DriverDelivery tmp = new DriverDelivery();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank Games object to contain our data
                tmp.setId(cursor.getString(cursor.getColumnIndex("id")));
                tmp.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                tmp.setNote(cursor.getString(cursor.getColumnIndex("note")));
                tmp.setTime(cursor.getString(cursor.getColumnIndex("time")));
                tmp.setTime_driver(cursor.getString(cursor.getColumnIndex("time_driver")));
                tmp.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                tmp.setId_restaurant(cursor.getString(cursor.getColumnIndex("id_restaurant")));
                tmp.setRestaurant(cursor.getString(cursor.getColumnIndex("restaurant")));
                tmp.setPhone_restaurant(cursor.getString(cursor.getColumnIndex("phone_restaurant")));
                tmp.setRestaurant_address(cursor.getString(cursor.getColumnIndex("restaurant_address")));
                deliverys.add(tmp);
            } while (cursor.moveToNext());
            adapter = new RecyclerAdapterDriverDeliveryList(
                    getApplicationContext(), deliverys);
            adapter.notifyDataSetChanged();

            setRecycler();
        }
        if(deliverys.size()==0){
            if(adapter != null){
                adapter.clearData();
                adapter.notifyDataSetChanged();
            }
        }


       // progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        if(searchView!=null){
            searchView.setIconified(true);
        }


    }
    @Override
    public void onRefresh() {
        //loadDB();
       // updateUI();

        callWebService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_history, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            Log.i("TAG", "Not null");
            searchView.setOnQueryTextListener(queryListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
            dialogFragment.setmManagerHistory(this);
            dialogFragment.show(getSupportFragmentManager(), "Select Date");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String s) {
            if(adapter != null) {
                adapter.getFilter().filter(s.toLowerCase());
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            if(adapter != null){
                if(s.trim().isEmpty()){
                    adapter.deleteFilter();
                }else{
                    adapter.getFilter().filter(s.toLowerCase());
                }
            }

            return true;
        }

    };

    private void updateList(DriverDelivery.List deliverys){
        this.deliverys=deliverys;
        if (deliverys == null){
            this.deliverys = new DriverDelivery.List();

        }

        if(deliverys.size()==0){
            if(adapter != null){
                adapter.clearData();
                adapter.notifyDataSetChanged();
            }
        }else{
            //if(adapter == null) {
                adapter = new RecyclerAdapterDriverDeliveryList(
                        getApplicationContext(), deliverys);
                adapter.notifyDataSetChanged();
            //}else{
            //    adapter.setDriverDeliveries(deliverys);
            //}

            setRecycler();
        }

        // progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        if(searchView!=null){
            searchView.setIconified(true);
        }

    }


    private void callWebService() {
        if (Helpers.isOnline(getApplicationContext())) {
            String id = Helpers.getInstance().readID(getApplicationContext());
            String apiKey = Helpers.getInstance().readApikey(getApplicationContext());
            String endDateNew="";
            String startDateNew="";

            SimpleDateFormat newFormat=new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDate=newFormat.format(formatDate.parse(startDate));
                if(!endDate.isEmpty()){
                    endDate=newFormat.format(formatDate.parse(endDate));
                }else{
                    endDate=startDate;
                }

                startDateNew=startDate+" 00:00:00";
                endDateNew=endDate+" 23:59:59";
            }catch (Exception e){
                e.printStackTrace();
            }

            RetrofitSpaceRequestDriverHistory queryUnit =
                    new RetrofitSpaceRequestDriverHistory(id, apiKey, startDateNew, endDateNew);

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new Driver_History.ListListenerDeliverys());
        }else{
            Toast.makeText(getApplicationContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUI(){
        Log.d("MyService", "BroadcastReceiver on Activity OK");

        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(true);
        }
        if(!endDate.isEmpty()) {
            btnDate.setText("From " + startDate + " to " + endDate);
        }else{
            btnDate.setText("" + startDate);

        }

        callWebService();
    }

    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class ListListenerDeliverys implements RequestListener<DriverDelivery.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            if(cause.contains("404")){
                if(adapter !=null){
                    adapter.clearData();
                }

            }
            swipeRefreshLayout.setRefreshing(false);
        }
        @Override
        public void onRequestSuccess(DriverDelivery.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateSelectedListener, OnRangeSelectedListener, View.OnClickListener{

        private Driver_History mHistory;
        private Button button;

        public void setmManagerHistory(Driver_History mManagerHistory){
            this.mHistory=mManagerHistory;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_basic, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            calendarView = (MaterialCalendarView)view.findViewById(R.id.calendarView);
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
            calendarView.setOnDateChangedListener(this);
            calendarView.setOnRangeSelectedListener(this);
            calendarView.setSelectionColor(getResources().getColor(R.color.colorAccent));

            button = (Button)view.findViewById(R.id.btnOk);
            button.setOnClickListener(this);
        }

        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


            List<CalendarDay> days = widget.getSelectedDates();
            if(days.size()>1) {

                endDate = formatDate.format(days.get(days.size() - 1).getDate());
                startDate = formatDate.format(days.get(0).getDate());
            }else if(days.size()==1){
                startDate = formatDate.format(days.get(0).getDate());
                endDate="";
            }

        }

        @Override
        public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates){
            List<CalendarDay> days = dates;
            if(days.size()>1) {

                endDate = formatDate.format(days.get(days.size() - 1).getDate());
                startDate = formatDate.format(days.get(0).getDate());
            }else if(days.size()==1){
                startDate = formatDate.format(days.get(0).getDate());
                endDate="";
            }



        }


        @Override
        public void onClick(View v) {
            mHistory.updateUI();
            this.dismiss();
        }
    }


}
