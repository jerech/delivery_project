package com.paulpwo.delivery305.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.paulpwo.delivery305.adapters.RecyclerAdapterNoticeManager;
import com.paulpwo.delivery305.models.NoticeManager;
import com.paulpwo.delivery305.utils.db.HelpersDB;
import com.paulpwo.delivery305.R;

import java.util.List;

public class ManagerNoticeList extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener{


    RecyclerAdapterNoticeManager adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<NoticeManager> noticeManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_notify_decline_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        noticeManager = new NoticeManager.List();
        updateList();

    }

    private void updateList(){
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this));
       adapter = new RecyclerAdapterNoticeManager(getApplicationContext(),
                getAllData());
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        swipeRefreshLayout.setRefreshing(false);
    }
    public List<NoticeManager> getAllData() {
        noticeManager.clear();
        SQLiteDatabase db = HelpersDB.getInstance().getDB(this);
        String[] columns = {"_id","title", "id_delivery", "id_restaurant", "body"};
        Cursor cursor = db.query("notice_manager", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new Games object and retrieve the data from the cursor to be stored in this Games object
                NoticeManager tmp = new NoticeManager();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank Games object to contain our data
                tmp.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                tmp.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                tmp.setId_delivery(cursor.getInt(cursor.getColumnIndex("id_delivery")));
                tmp.setId_restaurant(cursor.getInt(cursor.getColumnIndex("id_restaurant")));
                tmp.setBody(cursor.getString(cursor.getColumnIndex("body")));
                noticeManager.add(tmp);
            } while (cursor.moveToNext());
        }
        return noticeManager;
    }

    @Override
    public void onRefresh() {
        updateList();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateList();

    }
}
