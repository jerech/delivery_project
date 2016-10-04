package com.paulpwo.delivery305.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.paulpwo.delivery305.manager.Manager_detail_driver;
import com.paulpwo.delivery305.models.DriversWorking;
import com.paulpwo.delivery305.restaurant.Restaurant_detail_driver;
import com.paulpwo.delivery305.utils.Helpers;
import com.paulpwo.delivery305.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulpwo on 17/7/16.
 */

public class RecyclerAdapterDriversWorking  extends
        RecyclerView.Adapter<RecyclerAdapterDriversWorking.RecyclerViewHolder>  {


    List<DriversWorking> driversWorkings;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    public RecyclerAdapterDriversWorking(Context context,List<DriversWorking> driversWorkings) {
        this.driversWorkings = driversWorkings;
        this.mContext = context;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_drivers_working,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

 /*       TextView _name;
        TextView _time;
        TextView _address;
        TextView _count;*/
        holder._id.setText(driversWorkings.get(position).getId());
        holder._name.setText(driversWorkings.get(position).getFirst_name() + " " +driversWorkings.get(position).getLast_name() );
        holder._time.setText(driversWorkings.get(position).getDate_online());
        holder._address.setText(driversWorkings.get(position).getAddress());
        String tmp =driversWorkings.get(position).getCount_delierys();
        holder._count.setText(tmp);

        int count = Helpers.getInstance().getInteger(tmp);

        if (Helpers.isBetween(count, 1, 4)) {
            holder._count.setBackgroundResource(R.drawable.my_button_green);
        }else
        if (Helpers.isBetween(count, 5, 9)) {
            holder._count.setBackgroundResource(R.drawable.my_button_yellow);
        }else
        if (Helpers.isBetween(count, 10, 14)) {
            holder._count.setBackgroundResource(R.drawable.my_button_orange);
        }else
        if (Helpers.isBetween(count, 15, 100000)) {
            holder._count.setBackgroundResource(R.drawable.my_button_red);
        }else
       if (count ==0){

           holder._count.setBackgroundResource(R.drawable.my_button_gray);

        }

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return driversWorkings.size();
    }
    public void removeItem(int position) {
        driversWorkings.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem( DriversWorking deliverys) {
        driversWorkings.add(deliverys);
        notifyItemInserted(driversWorkings.size());
    }
    public void clearData() {
        int size = this.driversWorkings.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.driversWorkings.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView _id;
        TextView _name;
        TextView _time;
        TextView _address;
        TextView _count;
        Context contex;
        RelativeLayout _container;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);

            _name = (TextView) itemView.findViewById(R.id.tvName);
            _address = (TextView) itemView.findViewById(R.id.tvAddress);
            _count = (TextView) itemView.findViewById(R.id.tvCount);
            _id = (TextView) itemView.findViewById(R.id.id);
            _container = (RelativeLayout) itemView.findViewById(R.id.item_layout_container);
            contex = itemView.getContext();
            itemView.setOnClickListener(this);
            _container.setOnClickListener(this);
            _container.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            Bundle mb = new Bundle();
            mb.putString("id", _id.getText().toString());
            Intent i = new Intent(v.getContext(), Manager_detail_driver.class);
            i.putExtras(mb);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            v.getContext().startActivity(i);

        }
        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v     The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *              the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // saveBackground = ((ColorDrawable) v.getBackground()).getColor();
                // v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.sangolqui_2touch));
            }
            return false;
        }
    }
}


