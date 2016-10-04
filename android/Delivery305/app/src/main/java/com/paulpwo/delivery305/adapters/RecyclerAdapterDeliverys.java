package com.paulpwo.delivery305.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.github.florent37.viewanimator.ViewAnimator;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.models.Deliverys;
import com.paulpwo.delivery305.restaurant.Restaurant_detail_driver;
import com.paulpwo.delivery305.utils.Helpers;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by paulpwo on 11/7/16.
 */

public class RecyclerAdapterDeliverys extends RecyclerView.Adapter<RecyclerAdapterDeliverys.RecyclerViewHolder> implements Filterable {


    List<Deliverys> deliverysList;
    List<Deliverys> filterDeliverysList;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    private RecyclerAdapterDeliverys mSelfAdapter;
    public RecyclerAdapterDeliverys(Context context,List<Deliverys> deliverysList) {
        this.deliverysList = deliverysList;
        this.filterDeliverysList = deliverysList;
        this.mContext = context;
        this.mSelfAdapter = this;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_1_restauran,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        String status = filterDeliverysList.get(position).getStatus();
        holder._id.setText(filterDeliverysList.get(position).getId());
        holder._time.setText(filterDeliverysList.get(position).getTime());
        holder._address.setText(filterDeliverysList.get(position).getDelivery_address());
        String name = filterDeliverysList.get(position).getName_driver();
        holder._driver.setText(name);


        if (name != null){
            String tmp = deliverysList.get(position).getTime_driver();
            tmp = Helpers.getInstance().getMinutesString(tmp);

            holder._timeDriver.setText(tmp);

            holder._timeDriver.setText(tmp);
            if(     tmp.contains("5") ||
                    tmp.contains("4")  ||
                    tmp.contains("0") ||
                    tmp.contains("3") ||
                    tmp.contains("2") ||
                    tmp.contains("1") ||
                    tmp.contains("0")){
                holder._timeDriver.setBackgroundResource(R.drawable.my_button_green);
                if(status.contains("1")){
                    final Handler mHandler = new Handler();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewAnimator
                                    .animate(holder._timeDriver)
                                    .scale(1f, 0.5f, 1f)
                                    .accelerate()
                                    .duration(500)
                                    .repeatCount(2)
                                    .start();

                        }
                    });
                }

            }
            if(     tmp.contains("10") ||
                    tmp.contains("9") ||
                    tmp.contains("8") ||
                    tmp.contains("7") ||
                    tmp.contains("6")){
                holder._timeDriver.setBackgroundResource(R.drawable.my_button_yellow);

            }
            if(     tmp.contains("15") ||
                    tmp.contains("14") ||
                    tmp.contains("13") ||
                    tmp.contains("12") ||
                    tmp.contains("11")){
                holder._timeDriver.setBackgroundResource(R.drawable.my_button_orange);

            }



            //holder._back.setBackgroundResource(R.color.item_active);

            Picasso.with(mContext)
                    .load(R.drawable.ic_event_available_black_18dp)
                    .into(holder._status);
        }else{
            holder._timeDriver.setText(null);

            Picasso.with(mContext)
                    .load(R.drawable.ic_history_black_18dp)
                    .into(holder._status);
            holder._driver.setText("We are looking for a driver for you ...!");

                final Handler mHandler = new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                       /* ViewAnimator
                                .animate(holder._timeDriver)
                                .scale(1f, 0.5f, 1f)
                                .accelerate()
                                .duration(700)
                                .repeatCount(4)
                                .start(); */
                        ViewAnimator
                                .animate(holder._driver)
                                .alpha(0,1)
                                .duration(700)
                                .repeatCount(2)
                                .start();
                        ViewAnimator
                                .animate(holder._status)
                                .rotation(-360)
                                .repeatCount(2)
                                .start();
                    }
                });




        }
      //  setAnimation(mContext,holder._container, position);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return filterDeliverysList.size();
    }
    public void removeItem(int position) {
        filterDeliverysList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem( Deliverys deliverys) {
        deliverysList.add(deliverys);
        notifyItemInserted(deliverysList.size());
    }
    public void clearData() {
        int size = this.deliverysList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.deliverysList.remove(0);
            }

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
            animation.setStartOffset(position * Constants.ANIMATE_ITEM_RECYCLER_VIEW_TIME);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterDeliverysList = (ArrayList<Deliverys>) results.values;
                mSelfAdapter.notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                int filterDay = Integer.parseInt(constraint.toString());

                ArrayList<Deliverys> tempFilter = new ArrayList<Deliverys>();

                Iterator<Deliverys> iterator = deliverysList.iterator();
                while(iterator.hasNext()){
                    Deliverys temp = iterator.next();
                    Calendar calendar = Calendar.getInstance();

                    //Ej Oct 03 2016 01:47 PM
                    SimpleDateFormat curFormatter = new SimpleDateFormat("MMM dd yyyy hh:mm a");
                    Log.d("TAG", "Date Delivery:" + temp.getTime());
                    try {

                        Date dateObj = curFormatter.parse(temp.getTime());
                        calendar.setTime(dateObj);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Log.d("TAG","Filter day:"+filterDay+", date delivery"+calendar.get(Calendar.DAY_OF_YEAR));
                    if(filterDay == calendar.get(Calendar.DAY_OF_YEAR)){
                        tempFilter.add(temp);
                    }

                }

                FilterResults results = new FilterResults();
                results.values = tempFilter;

                return results;
            }
        };
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView _timeDriver;
        TextView _time;
        TextView _address;
        TextView _driver;
        TextView _id;
        View _back;
        RelativeLayout _container;
        ImageView _status;
        Context contex;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            _timeDriver = (TextView) itemView.findViewById(R.id.tvTimeDrive);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);
            _address = (TextView) itemView.findViewById(R.id.tvName);
            _driver = (TextView) itemView.findViewById(R.id.tvDrive);
            _back = (ImageView) itemView.findViewById(R.id.img_back);
            _id = (TextView) itemView.findViewById(R.id.id);
            _container = (RelativeLayout) itemView.findViewById(R.id.item_layout_container);
            _status = (ImageView)  itemView.findViewById(R.id.imageView_status);
            contex = itemView.getContext();
            itemView.setOnClickListener(this);
            _container.setOnClickListener(this);
            contex = itemView.getContext();
            itemView.setOnClickListener(this);
            _container.setOnClickListener(this);
            _back.setOnClickListener(this);
            _address.setOnClickListener(this);
            _status.setOnClickListener(this);
            _container.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            if (_timeDriver.getText().length() > 0 ){
                Bundle mb = new Bundle();
                mb.putString("id", _id.getText().toString());
                Intent i = new Intent(v.getContext(), Restaurant_detail_driver.class);
                i.putExtras(mb);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                v.getContext().startActivity(i);
            }


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
