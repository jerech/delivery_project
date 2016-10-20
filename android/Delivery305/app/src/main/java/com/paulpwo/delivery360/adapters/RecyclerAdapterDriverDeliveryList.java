package com.paulpwo.delivery360.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.driver.DriverDetailDelivery;
import com.paulpwo.delivery360.models.DriverDelivery;
import com.paulpwo.delivery360.utils.Helpers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by paulpwo on 12/7/16.
 */

public class RecyclerAdapterDriverDeliveryList extends
        RecyclerView.Adapter<RecyclerAdapterDriverDeliveryList.RecyclerViewHolder>  implements Filterable{


    List<DriverDelivery> driverDeliveries;
    List<DriverDelivery> filterDriverDeliveries;
    RecyclerAdapterDriverDeliveryList mSelfAdapter;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    public RecyclerAdapterDriverDeliveryList(Context context,List<DriverDelivery> driverDeliveries) {
        this.driverDeliveries = driverDeliveries;
        this.filterDriverDeliveries=driverDeliveries;
        this.mContext = context;
        this.mSelfAdapter=this;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_driver_delivery_list,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder._time.setText(filterDriverDeliveries.get(position).getTime());
        holder._address.setText(filterDriverDeliveries.get(position).getAddress());
        String status = filterDriverDeliveries.get(position).getStatus();
        holder._restaurant.setText(filterDriverDeliveries.get(position).getRestaurant());
        holder._id.setText(filterDriverDeliveries.get(position).getId());

        if (status.contains("1")){
            String tmp = filterDriverDeliveries.get(position).getTime_driver();
            if(tmp!=null){

                tmp = Helpers.getInstance().getMinutesString(tmp);

                holder._timeDriver.setText(tmp);
                if(     tmp.contains("5") ||
                        tmp.contains("4")  ||
                        tmp.contains("0") ||
                        tmp.contains("03") ||
                        tmp.contains("02") ||
                        tmp.contains("01") ||
                        tmp.contains("00")){
                    holder._timeDriver.setBackgroundResource(R.drawable.my_button_green);
                }
                if(     tmp.contains("10") ||
                        tmp.contains("09") ||
                        tmp.contains("08") ||
                        tmp.contains("07") ||
                        tmp.contains("06")){
                    holder._timeDriver.setBackgroundResource(R.drawable.my_button_yellow);

                }
                if(     tmp.contains("15") ||
                        tmp.contains("14") ||
                        tmp.contains("13") ||
                        tmp.contains("12") ||
                        tmp.contains("11")){
                    holder._timeDriver.setBackgroundResource(R.drawable.my_button_orange);

                }



            }



            // holder._back.setBackgroundResource(R.color.item_active);

            Picasso.with(mContext)
                    .load(R.drawable.ic_event_available_black_18dp)
                    .into(holder._status);
        }else{
            holder._timeDriver.setText(null);

            Picasso.with(mContext)
                    .load(R.drawable.ic_history_black_18dp)
                    .into(holder._status);
        }
       // setAnimation(mContext,holder._container, position);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if(filterDriverDeliveries!=null) {
            return filterDriverDeliveries.size();
        }else{
            return 0;
        }
    }
    public void removeItem(int position) {
        filterDriverDeliveries.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem( DriverDelivery deliverys) {
        driverDeliveries.add(deliverys);
        notifyItemInserted(driverDeliveries.size());
    }
    public void clearData() {
        int size = this.driverDeliveries.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.driverDeliveries.remove(0);
            }
            filterDriverDeliveries=driverDeliveries;
            this.notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(Context context, View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setStartOffset(position*240);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView _timeDriver;
        TextView _id;
        TextView _time;
        TextView _address;
        TextView _restaurant;
        View _back;
        RelativeLayout _container;
        ImageView _status;
        Context contex;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            _timeDriver = (TextView) itemView.findViewById(R.id.tvTimeDrive);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);
            _address = (TextView) itemView.findViewById(R.id.tvName);
            _restaurant = (TextView) itemView.findViewById(R.id.tvAddress);
            _back = (ImageView) itemView.findViewById(R.id.img_back);
            _container = (RelativeLayout) itemView.findViewById(R.id.item_layout_container);
            _status = (ImageView)  itemView.findViewById(R.id.imageView_status);
            _id = (TextView)  itemView.findViewById(R.id.id_delivery);

            contex = itemView.getContext();
            itemView.setOnClickListener(this);
            _container.setOnClickListener(this);

            _container.setSelected(true);
        }

        @Override
        public void onClick(View v) {

            Bundle mb = new Bundle();
            DriverDelivery delivery=null;
            mb.putString("id", _id.getText().toString());
            Intent i = new Intent(v.getContext(), DriverDetailDelivery.class);
            i.putExtras(mb);



            for(DriverDelivery temp:RecyclerAdapterDriverDeliveryList.this.filterDriverDeliveries){
                if(temp.getId().equals(_id.getText().toString())){
                    delivery=temp;
                }
            }
            if(delivery!=null){
                delivery.setId_driver(Helpers.getInstance().readID(contex));
                i.putExtra("delivery", delivery);
            }
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterDriverDeliveries = (ArrayList<DriverDelivery>) results.values;
                mSelfAdapter.notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                ArrayList<DriverDelivery> tempFilter = new ArrayList<DriverDelivery>();

                Iterator<DriverDelivery> iterator = driverDeliveries.iterator();
                while(iterator.hasNext()){
                    DriverDelivery temp = iterator.next();
                    String timeConstraint=constraint.toString().replace(".","");
                    if(temp.getAddress().toLowerCase().contains(constraint) ||
                            temp.getRestaurant().toLowerCase().contains(constraint) ||
                            temp.getNote().toLowerCase().contains(constraint) ||
                            temp.getTime().toLowerCase().contains(timeConstraint)){
                        tempFilter.add(temp);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = tempFilter;

                return results;
            }
        };
    }

    public void deleteFilter(){
        filterDriverDeliveries = driverDeliveries;
        mSelfAdapter.notifyDataSetChanged();
    }

    public void setDriverDeliveries(List<DriverDelivery> driverDeliveries){
        this.driverDeliveries=driverDeliveries;
        this.driverDeliveries=filterDriverDeliveries;
        notifyDataSetChanged();;
    }
}

