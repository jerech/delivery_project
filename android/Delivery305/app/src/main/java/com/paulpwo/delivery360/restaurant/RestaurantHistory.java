package com.paulpwo.delivery360.restaurant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.adapters.RecyclerAdapterDeliverys;
import com.paulpwo.delivery360.base.BaseFragmentSpice;
import com.paulpwo.delivery360.driver.Driver_History;
import com.paulpwo.delivery360.models.Deliverys;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDeliveryHistory;
import com.paulpwo.delivery360.utils.DividerItemDecoration;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantHistory extends BaseFragmentSpice implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerAdapterDeliverys adapter;
    private Deliverys.List deliverys;


    static String startDate="";
    static String endDate="";
    protected static MaterialCalendarView calendarView;
    private Button btnDate;
    static SimpleDateFormat formatDate;

    public RestaurantHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantHistory newInstance(String param1, String param2) {
        RestaurantHistory fragment = new RestaurantHistory();
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

        setHasOptionsMenu(true);

        Calendar calendar = Calendar.getInstance();
        formatDate = new SimpleDateFormat("yyyy-MM-dd");
        endDate = formatDate.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate=formatDate.format(calendar.getTime());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_restaurant_history, container, false);



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Delivery305 - RESTAURANT");



        //loadDB();
        swipeRefreshLayout.setRefreshing(true);

        btnDate = (Button) view.findViewById(R.id.btn_dialog_calendar);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
                dialogFragment.setmManagerHistory(RestaurantHistory.this);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "Select Date");

            }
        });

        if(!endDate.isEmpty()) {
            btnDate.setText("From " + startDate + " to " + endDate);
        }else{
            btnDate.setText("" + startDate);

        }

        // start listening for refresh local file list in
        return view;
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
        callWebService();
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


    public void updateUI(){
        Log.d("MyService", "BroadcastReceiver on Activity OK");
        Log.d("MyService", "BroadcastReceiver on Activity OK");

        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(true);
        }
        if(!endDate.isEmpty()) {
            btnDate.setText("From " + startDate + " to " + endDate);
        }else {
            btnDate.setText("" + startDate);
        }
        callWebService();
    }


    private void callWebService() {
        if (Helpers.isOnline(getContext())) {
            String id = Helpers.getInstance().readID(getContext());
            String apiKey = Helpers.getInstance().readApikey(getContext());

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

            RetrofitSpiceRequestDeliveryHistory queryUnit =
                    new RetrofitSpiceRequestDeliveryHistory(id, apiKey,startDateNew,endDateNew);

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new ListLoginRequestListenerDeliverys());
        }else{
            Toast.makeText(getContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateList(Deliverys.List deliverys){

        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_restaurant_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        adapter=new RecyclerAdapterDeliverys(getContext(),deliverys);
        recyclerView.setAdapter(adapter);
        this.deliverys = deliverys;


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
    public void onResume(){
        super.onResume();
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(true);
        callWebService();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings_logout:

                // Not implemented here
                return false;
            case R.id.action_calendar:
                SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
                dialogFragment.setmManagerHistory(this);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "Select Date");

                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }

    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateSelectedListener, OnRangeSelectedListener, View.OnClickListener{

        private RestaurantHistory mHistory;
        private Button button;

        public void setmManagerHistory(RestaurantHistory mManagerHistory){
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
