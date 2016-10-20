package com.paulpwo.delivery360.manager;

import android.content.Context;
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

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.adapters.RecyclerAdapterDriverDeliveryList;
import com.paulpwo.delivery360.base.BaseFragmentSpice;
import com.paulpwo.delivery360.models.DriverDelivery;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDriverDeliveryList;
import com.paulpwo.delivery360.utils.DividerItemDecoration;
import com.paulpwo.delivery360.utils.Helpers;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.utils.HelpersUpdateDBTick;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagerMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagerMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerMain extends BaseFragmentSpice implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private RecyclerAdapterDriverDeliveryList adapter;
    private DriverDelivery.List deliverys;

    public ManagerMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagerMain.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagerMain newInstance(String param1, String param2) {
        ManagerMain fragment = new ManagerMain();
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
        view = inflater.inflate(R.layout.fragment_master_main, container, false);
        callWebService();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Delivery305 - MANAGER");

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

    private void callWebService() {

        String id = Helpers.getInstance().readID(getContext());
        String apiKey = Helpers.getInstance().readApikey(getContext());

        RetrofitSpiceRequestDriverDeliveryList queryUnit =
                new RetrofitSpiceRequestDriverDeliveryList(id, apiKey);

        getSpiceManager().execute(queryUnit, "com.paulpwo.delivery305", DurationInMillis.ONE_SECOND,
                new ListLoginRequestListenerDriverList());
    }

    private void updateList(DriverDelivery.List driverDeliveryLists) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        adapter = new RecyclerAdapterDriverDeliveryList(
                getContext(), driverDeliveryLists);

        recyclerView.setAdapter(adapter);
        this.deliverys = driverDeliveryLists;
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

            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(DriverDelivery.List deliverys) {

            Log.v("Resul", deliverys.toString());
            updateList(deliverys);
           // Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        }
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
        if(deliverys != null){
            ArrayList<String[]> list = new ArrayList<String[]>();

            int t = deliverys.size();
            for(int i = 0; i< t ; i++){
                String tmp = deliverys.get(i).getTime_driver();
                String tmp2 = null;
                try {
                    tmp2 = Helpers.getInstance().getMinutesInteger(tmp);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                deliverys.get(i).setTime_driver(tmp2);
                String[] item = {deliverys.get(i).getId(), tmp2 };
                list.add(item);
            }
            adapter.notifyDataSetChanged();
            JSONArray jsArray = new JSONArray(list);

            new DoBackgroundTask().execute(jsArray);
        }


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
