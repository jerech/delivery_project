package com.paulpwo.delivery360.manager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.LineDataProvider;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.base.BaseSpiceActivity;
import com.paulpwo.delivery360.models.DeliverysManager;
import com.paulpwo.delivery360.request.RetrofitSpiceRequestDeliveryHistoryManager;
import com.paulpwo.delivery360.utils.Helpers;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeremias on 5/10/16.
 */

public class ManagerDeliveryHistoty extends BaseSpiceActivity{

    private DeliverysManager.List deliverys;

    static SimpleDateFormat formatDate;
    static String startDate="";
    static String endDate="";
    static String driverSelected="";
    static String restaurantSelected="";
    private Button btnDate;
    private Spinner spnRestaurants;
    ArrayAdapter<String> adapterRestaurants;
    ArrayList<String> arrSpnDrivers;
    private Spinner spnDrivers;
    ArrayAdapter<String> adapterDrivers;
    ArrayList<String> arrSpnRestaurants;
    protected static MaterialCalendarView calendarView;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private LinearLayout progressBar;

    private TextView tvTotal;
    private LinearLayout loRestaurants;
    private LinearLayout loDriveries;
    private LinearLayout loTimes;

    private LineChart chartDatosRed;

    private ArrayList<HolderDriver> holderDrivers=new ArrayList<>();
    private ArrayList<HolderRestaurant> holderRestaurants=new ArrayList<>();
    private ArrayList<HolderSchedule> holderSchedules=new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_deliveries_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Stats Deliveries");
        ButterKnife.bind(this);


        formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        endDate = formatDate.format(calendar.getTime())+" 23:59:59";
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate=formatDate.format(calendar.getTime())+" 00:00:00";


        initView();
        updateUI();
    }

    private void initView(){
        btnDate = (Button) findViewById(R.id.btn_dialog_calendar);
        spnDrivers = (Spinner) findViewById(R.id.spnDrivers);
        spnRestaurants = (Spinner) findViewById(R.id.spnRestaurants);

        progressBar = (LinearLayout) findViewById(R.id.progressBar);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        loDriveries = (LinearLayout) findViewById(R.id.loDataDrivers);
        loRestaurants = (LinearLayout) findViewById(R.id.loDataRestaurants);
        loTimes = (LinearLayout) findViewById(R.id.loTimes);
        chartDatosRed = (LineChart) findViewById(R.id.chart);

    }

    @Override
    public void onResume(){
        super.onResume();

    }


    public void updateUI(){
        Log.d("MyService", "BroadcastReceiver on Activity OK");

        if(progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        if(!endDate.isEmpty()) {
            btnDate.setText("From " + startDate.split(" ")[0] + " to " + endDate.split(" ")[0]);
        }else{
            btnDate.setText("" + startDate.split(" ")[0]);
            endDate=startDate.split(" ")[0]+" 23:59:59";
        }

        //driverSelected="";
        //restaurantSelected="";
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

    private void updateData(DeliverysManager.List deliverys){
        //this.deliverys=deliverys;
        if(arrSpnDrivers==null) {
            arrSpnDrivers = new ArrayList<>();
            arrSpnDrivers.add("All");
        }
        if(arrSpnRestaurants==null) {
            arrSpnRestaurants = new ArrayList<>();
            arrSpnRestaurants.add("All");
        }


        if(deliverys!=null){
            groupData(deliverys);
            tvTotal.setText(String.valueOf(deliverys.size()));

            loDriveries.removeAllViews();
            loRestaurants.removeAllViews();
            loTimes.removeAllViews();

            for(HolderRestaurant restaurant:holderRestaurants){
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_delivery_history, null);
                TextView tvName= (TextView) linearLayout.findViewById(R.id.tvData);
                TextView tvCan = (TextView) linearLayout.findViewById(R.id.tvTotal);
                TextView tvExtra = (TextView) linearLayout.findViewById(R.id.tvExtra);
                tvName.setText(restaurant.name+":");
                tvCan.setText(String.valueOf(restaurant.can));

                if(restaurant.daysWithoutDelivery>=2){
                    tvExtra.setVisibility(View.VISIBLE);
                    tvExtra.setText(" ("+restaurant.daysWithoutDelivery+" days without deliveries)");
                }
                loRestaurants.addView(linearLayout);
                if(adapterRestaurants==null) {
                    arrSpnRestaurants.add(restaurant.name);
                }
            }

            for(HolderDriver driver:holderDrivers){
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_delivery_history, null);
                TextView tvName= (TextView) linearLayout.findViewById(R.id.tvData);
                TextView tvCan = (TextView) linearLayout.findViewById(R.id.tvTotal);
                tvName.setText(driver.name+":");
                tvCan.setText(String.valueOf(driver.can));
                loDriveries.addView(linearLayout);
                if(adapterDrivers==null) {
                    arrSpnDrivers.add(driver.name);
                }
            }

            for(HolderSchedule schedule:holderSchedules){
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_delivery_history, null);
                TextView tvName= (TextView) linearLayout.findViewById(R.id.tvData);
                TextView tvCan = (TextView) linearLayout.findViewById(R.id.tvTotal);
                tvName.setText(schedule.name+":");
                tvCan.setText(String.valueOf(schedule.can));
                loTimes.addView(linearLayout);
            }



        }


        if(adapterRestaurants==null) {
            adapterRestaurants = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, arrSpnRestaurants);
            adapterRestaurants.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnRestaurants.setAdapter(adapterRestaurants);
            spnRestaurants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String str = parent.getItemAtPosition(position).toString();
                    if (!str.equals("All")) {
                        restaurantSelected = str;

                    } else {
                        restaurantSelected = "";
                    }
                    filterData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    restaurantSelected = "";
                }
            });
        }

        if(adapterDrivers==null) {
            adapterDrivers = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, arrSpnDrivers);
            adapterDrivers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnDrivers.setAdapter(adapterDrivers);
            spnDrivers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String str = parent.getItemAtPosition(position).toString();
                    if (!str.equals("All")) {
                        driverSelected = str;
                    } else {
                        driverSelected = "";
                    }
                    filterData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    driverSelected = "";
                }
            });
        }





    }

    private void filterData(){

        DeliverysManager.List list=new DeliverysManager.List();

        for (DeliverysManager deliverysManager:deliverys){
            boolean agreggate=true;

            if(!restaurantSelected.isEmpty()){
                if(deliverysManager.getName_restaurant().equalsIgnoreCase(restaurantSelected)){
                    agreggate=agreggate&&true;
                }else{
                    agreggate=agreggate&&false;
                }
            }else{
                agreggate=agreggate&&true;
            }

            if(!driverSelected.isEmpty()){
                if(deliverysManager.getName_driver().equalsIgnoreCase(driverSelected)){
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

        updateData(list);

        createGraffic(list);
    }
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
            setDeliverys(deliverys);
            filterData();

            if(progressBar!=null){
                progressBar.setVisibility(View.GONE);
            }
            //Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

        }
    }

    @OnClick(R.id.btn_dialog_calendar)
    void onSimpleCalendarDialogClick() {
        SimpleCalendarDialogFragment dialogFragment = new SimpleCalendarDialogFragment();
        dialogFragment.setmManagerHistory(this);
        dialogFragment.show(getSupportFragmentManager(), "Select Date");
    }

    @OnClick(R.id.btn_view_deliveries)
    void onViewDeliveriesClick() {
        Intent intent = new Intent(this, ListDeliveries.class);
        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);
        intent.putExtra("restaurant", restaurantSelected);
        intent.putExtra("driver", driverSelected);
        startActivity(intent);
    }

    protected void setDeliverys(DeliverysManager.List deliverys){
        this.deliverys=deliverys;
    }


    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateSelectedListener, OnRangeSelectedListener, View.OnClickListener{

        private ManagerDeliveryHistoty mManagerHistory;
        private Button button;

        public void setmManagerHistory(ManagerDeliveryHistoty mManagerHistory){
            this.mManagerHistory=mManagerHistory;
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
            mManagerHistory.updateUI();
            this.dismiss();
        }
    }

    private void groupData(DeliverysManager.List deliverys){
        ArrayList<Integer> idsDriver=new ArrayList<>();
        ArrayList<Integer> idsRestaurants=new ArrayList<>();

        holderDrivers=new ArrayList<>();
        holderRestaurants=new ArrayList<>();
        holderSchedules=new ArrayList<>();

        holderSchedules.add(new HolderSchedule("10:00 to 15:00",0,""));
        holderSchedules.add(new HolderSchedule("15:00 to 19:00",0,""));
        holderSchedules.add(new HolderSchedule("19:00 to 23:00",0,""));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (DeliverysManager deliverysManager:deliverys){

            if(!idsDriver.contains(Integer.valueOf(deliverysManager.getId_driver()))){
                idsDriver.add(Integer.valueOf(deliverysManager.getId_driver()));
                holderDrivers.add(new HolderDriver(Integer.valueOf(deliverysManager.getId_driver()),
                                                    deliverysManager.getName_driver(),
                                                    1));
            }else{
                for (HolderDriver holderDriver:holderDrivers){
                    if(holderDriver.id==Integer.valueOf(deliverysManager.getId_driver())){
                        holderDriver.can+=1;
                    }
                }
            }

            if(!idsRestaurants.contains(Integer.valueOf(deliverysManager.getId_restaurant()))){
                idsRestaurants.add(Integer.valueOf(deliverysManager.getId_restaurant()));
                holderRestaurants.add(new HolderRestaurant(Integer.valueOf(deliverysManager.getId_restaurant()),
                        deliverysManager.getName_restaurant(),
                        1, deliverysManager.getTime()));
            }else{
                for (HolderRestaurant holderRestaurant:holderRestaurants){
                    if(holderRestaurant.id==Integer.valueOf(deliverysManager.getId_restaurant())){
                        holderRestaurant.can+=1;
                    }
                }
            }


            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(format.parse(deliverysManager.getTime()));
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                if(hour>=10&&hour<15){
                    holderSchedules.get(0).can++;
                }else if(hour>=15&&hour<19){
                    holderSchedules.get(1).can++;
                }else if(hour>=19&&hour<23){
                    holderSchedules.get(2).can++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }


    private class HolderDriver{
        int id;
        String name;
        int can;

        public HolderDriver(int id, String name, int can) {
            this.id = id;
            this.name = name;
            this.can = can;
        }
    }

    private class HolderRestaurant{
        int id;
        String name;
        int can;
        int daysWithoutDelivery;

        public HolderRestaurant(int id, String name, int can, String time) {
            this.id = id;
            this.name = name;
            this.can = can;
            this.daysWithoutDelivery=0;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar=Calendar.getInstance();
            try {
                calendar.setTime(format.parse(time));
                int diff=Calendar.getInstance().get(Calendar.DAY_OF_YEAR)-calendar.get(Calendar.DAY_OF_YEAR);

                this.daysWithoutDelivery=diff;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class HolderSchedule{
        String name;
        int can;
        String description;

        public HolderSchedule(String name, int can, String description) {
            this.name = name;
            this.can = can;
            this.description = description;
        }
    }

    private class HolderDataGraffic{
        String date;
        int can;

        public HolderDataGraffic(String date, int can) {
            this.date = date;
            this.can = can;
        }
    }



    private void createGraffic(DeliverysManager.List deliverys){

        ArrayList<HolderDataGraffic> newList=new ArrayList<>();
        ArrayList<String> dates=new ArrayList<>();

        for (DeliverysManager deliverysManager:deliverys){
            String date=deliverysManager.getTime().split(" ")[0];
            if(dates.contains(date)){
                for (HolderDataGraffic holderDataGraffic:newList){
                    if(holderDataGraffic.date.equalsIgnoreCase(date)){
                        holderDataGraffic.can++;
                    }
                }
            }else{
                dates.add(date);
                newList.add(new HolderDataGraffic(date, 1));
            }
        }

        //Armamos el grafico de datosRed
        ArrayList<Entry> valsY = new ArrayList<Entry>();
        ArrayList<String> valsX = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int indice=0;
        for (int index=(newList.size()-1);index>=0;index--) {
            HolderDataGraffic temp = newList.get(index);
            valsY.add(new Entry(Float.parseFloat(temp.can+""), indice));
            valsX.add(temp.date+"");
            indice++;
        }


        chartDatosRed.setMinimumHeight(280);
        chartDatosRed.setDescription("");

        chartDatosRed.setDragEnabled(true);
        chartDatosRed.setScaleEnabled(true);
        chartDatosRed.setDrawGridBackground(true);

        XAxis x = chartDatosRed.getXAxis();
        x.setEnabled(true);

        YAxis y = chartDatosRed.getAxisLeft();
        y.setLabelCount(6, false);
        y.setStartAtZero(false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(getResources().getColor(R.color.colorAccent));

        chartDatosRed.getAxisRight().setEnabled(false);
        LineDataSet lineDataSet = new LineDataSet(valsY, "");
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setLabel("");
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setCircleSize(0);

        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);

        lineDataSet.setHighLightColor(Color.rgb(255, 117, 117));
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData data = new LineData(valsX, lineDataSet);
        data.setHighlightEnabled(false);
        chartDatosRed.setData(data);
        chartDatosRed.invalidate();
    }



}
