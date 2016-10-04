package com.paulpwo.delivery305.driver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.paulpwo.delivery305.adapters.RecyclerAdapterDriverDeliveryList;
import com.paulpwo.delivery305.request.RetrofitSpiceRequestDriverDeliveryList;
import com.paulpwo.delivery305.utils.db.HelpersDB;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.models.DriverDelivery;
import com.paulpwo.delivery305.utils.DividerItemDecoration;

public class Driver_History extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {


    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;
    private RecyclerAdapterDriverDeliveryList adapter;
    private DriverDelivery.List deliverys;
    RecyclerView recyclerView;
    private RetrofitSpiceRequestDriverDeliveryList queryUnit;
    private ProgressBar progressBar;


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
        loadDB();

    }
    @Override
    public void onStart() {
        super.onStart();
        loadDB();
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

    }
    @Override
    public void onRefresh() {
        loadDB();
       // updateUI();
        //callWebService();
    }

}
