package com.paulpwo.delivery305.driver;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery305.request.RetrofitSpaceRequestDeliveries;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.adapters.RecyclerAdapterDriverDeliveryAvailable;
import com.paulpwo.delivery305.models.DriverDeliveryAvailable;
import com.paulpwo.delivery305.service.serviceDefault;
import com.paulpwo.delivery305.utils.DividerItemDecoration;
import com.paulpwo.delivery305.utils.Helpers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriverListAvailable.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverListAvailable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverListAvailable extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final SpiceManager spiceManager = new SpiceManager(serviceDefault.class );
    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    private  RecyclerAdapterDriverDeliveryAvailable adapter;
    private DriverDeliveryAvailable.List deliverys;
    RecyclerView recyclerView;
    public DriverListAvailable() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverListAvailable.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverListAvailable newInstance(String param1, String param2) {
        DriverListAvailable fragment = new DriverListAvailable();
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
        spiceManager.start(getContext());
        callWebService();
    }
    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_list_available, container, false);

        callWebService();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onRefresh() {
        callWebService();
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

    private void callWebService(){
        if(Helpers.isOnline(getContext())){
            if(progressBar != null){
                progressBar.setVisibility(View.VISIBLE);
            }
            RetrofitSpaceRequestDeliveries queryUnit =
                    new RetrofitSpaceRequestDeliveries();

            getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                    new ListRetrofitSpaceRequestDeliveries());
        }else{
            Toast.makeText(getContext(), "Check internet connection and re entry", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateList(DriverDeliveryAvailable.List driverDeliveryAvailable){
       recyclerView= (RecyclerView) view.findViewById(R.id.my_recycler_view_avaliable);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        adapter=new RecyclerAdapterDriverDeliveryAvailable(
                getContext(),driverDeliveryAvailable);

        recyclerView.setAdapter(adapter);
        this.deliverys = driverDeliveryAvailable;
    }


    // ============================================================================================
    // INNER CLASSES
    // ============================================================================================
    private  class ListRetrofitSpaceRequestDeliveries implements RequestListener<DriverDeliveryAvailable.List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            try {
                String cause = spiceException.getCause().toString();
                getSpiceManager().cancelAllRequests();
                if(cause.contains("404")){
                    if(adapter !=null){
                        adapter.clearData();
                    }

                }
                progressBar.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){

            }

        }
        @Override
        public void onRequestSuccess(DriverDeliveryAvailable.List deliverys) {
            try {
            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.INVISIBLE);
            }catch (Exception e){

            }
        }
    }




    public void updateUI(){
        Log.d("MyService", "BroadcastReceiver on Activity OK");
        callWebService();
    }

    private class DoBackgroundTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... arg0) {
            return null;
        }

    }
}
