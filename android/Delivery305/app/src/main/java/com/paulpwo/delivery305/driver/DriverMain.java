package com.paulpwo.delivery305.driver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.adapters.RecyclerAdapterDriverDeliveryList;
import com.paulpwo.delivery305.request.RetrofitSpiceRequestDriverDeliveryList;
import com.paulpwo.delivery305.utils.db.HelpersDB;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.base.BaseFragmentSpice;
import com.paulpwo.delivery305.models.DriverDelivery;
import com.paulpwo.delivery305.utils.DividerItemDecoration;
import com.paulpwo.delivery305.utils.Helpers;
import com.paulpwo.delivery305.utils.HelpersUpdateDBTick;

import org.json.JSONArray;

import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverMain extends BaseFragmentSpice implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;
    private RecyclerAdapterDriverDeliveryList adapter;
    private DriverDelivery.List deliverys;
    RecyclerView recyclerView;
    private RetrofitSpiceRequestDriverDeliveryList queryUnit;


    public DriverMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverMain.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverMain newInstance(String param1, String param2) {
        DriverMain fragment = new DriverMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_driver_main, container, false);

        //callWebService();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        if(Helpers.getInstance().readIsManager(getContext()) != 0){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Delivery ED - MANAGER");
        }else{
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Delivery ED - DRIVER");
        }


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        loadDB();


        return view;
    }



    public void setRecycler(){


        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));




        recyclerView.setAdapter(adapter);

    }
    private void loadDB(){
        if (deliverys != null){
            deliverys.clear();

        }else{
            this.deliverys = new DriverDelivery.List();
        }


        SQLiteDatabase db = HelpersDB.getInstance().getDB(getContext());
        String[] columns = {"id","address", "note", "time", "time_driver","status",
                "id_restaurant", "restaurant","phone_restaurant","restaurant_address"};
        Cursor cursor = db.query("deliveries", columns, "status=1", null, null, null, null);
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
                    getContext(), deliverys);
            adapter.notifyDataSetChanged();

            setRecycler();
        }
        if(deliverys.size()==0){
            if(adapter != null){
                adapter.clearData();
                adapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
       // loadDB();
        updateUI();
        //callWebService();
    }

    @OnClick(R.id.progressBar)
    public void onClick() {
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void callWebService() {
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }

        String id = Helpers.getInstance().readID(getContext());
        String apiKey = Helpers.getInstance().readApikey(getContext());

        queryUnit =
                new RetrofitSpiceRequestDriverDeliveryList(id, apiKey);


        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new ListLoginRequestListenerDriverList());
    }

    private void updateList(DriverDelivery.List driverDeliveryLists) {


    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public class ListLoginRequestListenerDriverList implements RequestListener<DriverDelivery.List> {

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
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(DriverDelivery.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


    public void updateUI() throws RuntimeException {

        Log.d("MyService", "BroadcastReceiver on Activity OK");
        loadDB();

    }

    private class DoBackgroundTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... arg0) {

            JSONArray jsArray = (JSONArray) arg0[0];
            HelpersUpdateDBTick.getInstance().UpdateTimer_Delivery(
                    Helpers.getInstance().readID(getContext()), jsArray.toString());


            return null;
        }

    }


}
