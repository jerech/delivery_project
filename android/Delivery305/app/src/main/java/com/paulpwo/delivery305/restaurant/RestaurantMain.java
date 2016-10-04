package com.paulpwo.delivery305.restaurant;

import android.content.Context;
import android.content.IntentFilter;
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
import android.widget.Button;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.adapters.RecyclerAdapterDeliverys;
import com.paulpwo.delivery305.base.BaseFragmentSpice;
import com.paulpwo.delivery305.models.Deliverys;
import com.paulpwo.delivery305.request.RetrofitSpiceRequestDelivery;
import com.paulpwo.delivery305.utils.DividerItemDecoration;
import com.paulpwo.delivery305.utils.Helpers;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.utils.HelpersUpdateDBTick;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantMain#newInstance} factory method to
 * create an instance of this fragment.
 */


public class RestaurantMain extends BaseFragmentSpice implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener, DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerAdapterDeliverys adapter;

    private IntentFilter myFilter;
    private Deliverys.List deliverys;

    private Button btnSelectDate;
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat formatDate;


    public RestaurantMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantMain.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantMain newInstance(String param1, String param2) {
        RestaurantMain fragment = new RestaurantMain();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_main, container, false);
        //setupList();
        callWebService();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Delivery ED - RESTAURANT");
        myFilter = new IntentFilter();
        myFilter.addAction("NOMBRE_DE_NUESTRA_ACTION");
        // start listening for refresh local file list in

        btnSelectDate = (Button)view.findViewById(R.id.btnSelectDate);
        btnSelectDate.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),false);

        formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = formatDate.format(calendar.getTime());
        btnSelectDate.setText("DATE");

        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        callWebService();

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
            this.context = context;
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
        btnSelectDate.setText("DATE");
        callWebService();
    }

    @Override
    public void onClick(View view) {
        datePickerDialog.show(getChildFragmentManager(),"DATE");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        String nowDate = formatDate.format(calendar.getTime());
        btnSelectDate.setText(nowDate);

        adapter.getFilter().filter("" + calendar.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/> Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        String nowDate = formatDate.format(calendar.getTime());
        btnSelectDate.setText(nowDate);

        mItemAdapter.getFilter().filter("" + calendar.get(Calendar.DAY_OF_YEAR));
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void callWebService() {
        if (Helpers.isOnline(getContext())) {
            String id = Helpers.getInstance().readID(getContext());
            String apiKey = Helpers.getInstance().readApikey(getContext());

            RetrofitSpiceRequestDelivery queryUnit =
                    new RetrofitSpiceRequestDelivery(id, apiKey);

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new ListLoginRequestListenerDeliverys());
        } else{
        Toast.makeText(getContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupRecycler(){

    }
    private void updateList(Deliverys.List deliverys){

        if(this.deliverys!=null) {
            if (deliverys.size() == this.deliverys.size()) {
                return;
            }
        }


        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_restaurant);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter=new RecyclerAdapterDeliverys(getContext(),deliverys);
        recyclerView.setAdapter(adapter);
        this.deliverys = deliverys;



        String nowDate = btnSelectDate.getText().toString();
        if(!nowDate.equalsIgnoreCase("DATE")) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(formatDate.parse(nowDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            adapter.getFilter().filter("" + calendar.get(Calendar.DAY_OF_YEAR));
        }

    }
    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================


    public  class ListLoginRequestListenerDeliverys implements RequestListener<Deliverys.List> {

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
        public void onRequestSuccess(Deliverys.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void updateUI(){
        Log.d("MyService", "BroadcastReceiver on Activity OK");
      /*  if(deliverys != null){
            ArrayList<String[]> list = new ArrayList<String[]>();

            int t = deliverys.size();
            for(int i = 0; i< t ; i++){
                String tmp = deliverys.get(i).getTime_driver();
                final String tmp2 = Helpers.getInstance().getMinutesInteger(tmp);

                deliverys.get(i).setTime_driver(tmp2);
                String[] item = {deliverys.get(i).getId(), tmp2 };
                list.add(item);
            }
            adapter.notifyDataSetChanged();
            JSONArray jsArray = new JSONArray(list);

            new DoBackgroundTask().execute(jsArray);
        }*/
        callWebService();

    }





    private class DoBackgroundTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... arg0) {

            JSONArray jsArray = (JSONArray)   arg0[0];
            HelpersUpdateDBTick.getInstance().UpdateTimer_Delivery(
                    Helpers.getInstance().readID(getContext()) ,jsArray.toString());



            return null;
        }

    }
}
