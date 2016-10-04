package com.paulpwo.delivery305.manager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.models.DriversWorking;
import com.paulpwo.delivery305.request.RetrofitRequestDriversWorking;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.adapters.RecyclerAdapterDriversWorking;
import com.paulpwo.delivery305.base.BaseSpiceActivity;
import com.paulpwo.delivery305.utils.DividerItemDecoration;

public class ManagerViewDrivers extends BaseSpiceActivity /*implements SwipeRefreshLayout.OnRefreshListener*/ {
    RecyclerView recyclerView;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private RetrofitRequestDriversWorking queryUnit;
    private RecyclerAdapterDriversWorking adapter;
    private DriversWorking.List deliverys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view_drivers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);*/

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        callWebService();
    }







/*    @Override
    public void onRefresh() {
        callWebService();
    }*/

    private void callWebService() {
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
/*
        String id = Helpers.getInstance().readID(getContext());
        String apiKey = Helpers.getInstance().readApikey(getContext());*/

        queryUnit =
                new RetrofitRequestDriversWorking();


        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new ListLoginRequestListenerDriverList());
    }
    private void updateList(DriversWorking.List driverDeliveryLists) {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_driver_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ManagerViewDrivers.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(ManagerViewDrivers.this));

        adapter = new RecyclerAdapterDriversWorking(
                ManagerViewDrivers.this, driverDeliveryLists);

        recyclerView.setAdapter(adapter);
        this.deliverys = driverDeliveryLists;
    }



    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public class ListLoginRequestListenerDriverList implements RequestListener<DriversWorking.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String cause = spiceException.getCause().toString();
            getSpiceManager().cancelAllRequests();
            if (cause.contains("404")) {
                if (adapter != null) {
                    adapter.clearData();
                }

            }
            progressBar.setVisibility(View.INVISIBLE);
           // swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(DriversWorking.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
            progressBar.setVisibility(View.INVISIBLE);
            // Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            //swipeRefreshLayout.setRefreshing(false);

        }
    }


}
