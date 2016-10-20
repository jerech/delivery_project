package com.paulpwo.delivery360.manager;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.adapters.RecyclerAdapterDeliverysManager;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.models.DeliverysManager;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDeliveryHistoryManager;
import com.paulpwo.delivery360.utils.DividerItemDecoration;
import com.paulpwo.delivery360.utils.Helpers;

import java.text.SimpleDateFormat;

public class ListDeliveries extends BaseSpiceActivity {


    private RecyclerAdapterDeliverysManager adapter;
    private DeliverysManager.List deliverys;
    RecyclerView recyclerView;

    private LinearLayout progressBar;

    SimpleDateFormat formatDate;
    private static String startDate;
    private static String endDate;
    private static String restaurant;
    private static String driver;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_deliveries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("start_date")) {
            startDate = getIntent().getStringExtra("start_date");
            endDate = getIntent().getStringExtra("end_date");
            restaurant = getIntent().getStringExtra("restaurant");
            driver = getIntent().getStringExtra("driver");
        }
        progressBar = (LinearLayout)findViewById(R.id.progressBar);

        getSupportActionBar().setTitle(""+startDate.split(" ")[0]+" to "+endDate.split(" ")[0]);

        callWebService();


    }

    private void callWebService() {
        if (Helpers.isOnline(getApplicationContext())) {
            String apiKey = Helpers.getInstance().readApikey(getApplicationContext());

            RetrofitSpiceRequestDeliveryHistoryManager queryUnit =
                    new RetrofitSpiceRequestDeliveryHistoryManager(startDate, endDate,apiKey);

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new ListRequestListenerDeliverys());
        }else{
            Toast.makeText(getApplicationContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
    }

    protected void updateUI(DeliverysManager.List deliverysManager){
        this.deliverys = deliverysManager;
        filterData();
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        adapter=new RecyclerAdapterDeliverysManager(getApplicationContext(),deliverys);
        recyclerView.setAdapter(adapter);




    }

    private void filterData(){

        DeliverysManager.List list=new DeliverysManager.List();

        for (DeliverysManager deliverysManager:deliverys){
            boolean agreggate=true;

            if(!restaurant.isEmpty()){
                if(deliverysManager.getName_restaurant().equalsIgnoreCase(restaurant)){
                    agreggate=agreggate&&true;
                }else{
                    agreggate=agreggate&&false;
                }
            }else{
                agreggate=agreggate&&true;
            }

            if(!driver.isEmpty()){
                if(deliverysManager.getName_driver().equalsIgnoreCase(driver)){
                    agreggate=agreggate&&true;
                }else{
                    agreggate=agreggate&&false;
                }
            }else{
                agreggate=agreggate&&true;
            }

            if(agreggate){
                list.add(deliverysManager);
            }
        }

        this.deliverys=list;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_deliveries, menu);
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



        return super.onOptionsItemSelected(item);
    }


    private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String s) {
            if (adapter != null) {
                adapter.getFilter().filter(s.toLowerCase());
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            if (adapter != null) {
                if (s.trim().isEmpty()) {
                    adapter.deleteFilter();
                } else {
                    adapter.getFilter().filter(s.toLowerCase());
                }
            }

            return true;
        }

    };



    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class ListRequestListenerDeliverys implements RequestListener<DeliverysManager.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            if(cause.contains("404")){
                Toast.makeText(getApplicationContext(), "Data empty.", Toast.LENGTH_LONG).show();

            }
            if(progressBar!=null){
                progressBar.setVisibility(View.GONE);
            }
        }
        @Override
        public void onRequestSuccess(DeliverysManager.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateUI(deliverys);
            if(progressBar!=null){
                progressBar.setVisibility(View.GONE);
            }
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

        }
    }




}
