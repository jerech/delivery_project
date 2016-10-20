package com.paulpwo.delivery360.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulpwo.delivery360.R;
import com.paulpwo.delivery360.models.DriverDelivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulpwo on 15/7/16.
 */

public class RecyclerAdapterDeliveryDetail extends
        RecyclerView.Adapter<RecyclerAdapterDeliveryDetail.RecyclerViewHolder>  {


    DriverDelivery driverDeliveries;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    public RecyclerAdapterDeliveryDetail(Context context,DriverDelivery driverDeliveries) {
        this.driverDeliveries = driverDeliveries;
        this.mContext = context;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detail_delivery,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {


        holder._restaurant_name.setText(driverDeliveries.getRestaurant());
        holder._restaurant_address.setText(driverDeliveries.getRestaurant_address());
        holder._delivery_address.setText(driverDeliveries.getAddress());
        holder._txtNote.setText(driverDeliveries.getNote());
        holder._time.setText(driverDeliveries.getTime());

        setAnimation(mContext,holder._container, position);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return 1;
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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView _restaurant_name;
        TextView _restaurant_address;
        TextView _delivery_address;
        TextView _txtNote;
        TextView _time;
        View _back;
        RelativeLayout _container;
        ImageView _status;
        Context contex;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            _restaurant_name = (TextView) itemView.findViewById(R.id.name);
            _restaurant_address = (TextView) itemView.findViewById(R.id.driver_address);
            _delivery_address = (TextView) itemView.findViewById(R.id.delivery_address);
            _txtNote = (TextView) itemView.findViewById(R.id.txtNote);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);
            _back = (ImageView) itemView.findViewById(R.id.img_back);
            _container = (RelativeLayout) itemView.findViewById(R.id.item_layout_container);
            _status = (ImageView)  itemView.findViewById(R.id.imageView_status);
            contex = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Intent intent = new Intent(v.getContext(), ActivityDetail.class);
            // intent.putExtra("id", textView_id.getText().toString());
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // v.getContext().startActivity(intent);
           // Toast.makeText(v.getContext(), "Item: " + _address.getText().toString(), Toast.LENGTH_SHORT).show();

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

