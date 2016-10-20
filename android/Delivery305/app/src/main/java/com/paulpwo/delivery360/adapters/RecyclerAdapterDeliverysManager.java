package com.paulpwo.delivery360.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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
import com.paulpwo.delivery360.manager.DetailDelivery;
import com.paulpwo.delivery360.models.DeliverysManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by paulpwo on 11/7/16.
 */

public class RecyclerAdapterDeliverysManager extends RecyclerView.Adapter<RecyclerAdapterDeliverysManager.RecyclerViewHolder> implements Filterable {


    List<DeliverysManager> deliverysList;
    List<DeliverysManager> filterDeliverysList;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    private RecyclerAdapterDeliverysManager mSelfAdapter;
    public RecyclerAdapterDeliverysManager(Context context, List<DeliverysManager> deliverysList) {
        this.deliverysList = deliverysList;
        this.filterDeliverysList = deliverysList;
        this.mContext = context;
        this.mSelfAdapter = this;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_deliveries,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        DeliverysManager temp = filterDeliverysList.get(position);
        holder._time.setText(temp.getTime());
        holder._address.setText(temp.getDelivery_address());
        String status = temp.getStatus();
        holder._restaurant.setText(temp.getName_restaurant());
        holder._driver.setText(temp.getName_driver());
        holder._id=temp.getId();

        // setAnimation(mContext,holder._container, position);

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
        deliverysList.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem(DeliverysManager deliverys) {
        deliverysList.add(deliverys);
        notifyItemInserted(deliverysList.size());
    }
    public void clearData() {
        int size = this.deliverysList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.deliverysList.remove(0);
            }
            deleteFilter();

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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        private SparseBooleanArray selectedItems;
        TextView _timeDriver;
        String _id;
        TextView _time;
        TextView _address;
        TextView _driver;
        TextView _restaurant;
        View _back;
        RelativeLayout _container;
        ImageView _status;
        Context contex;
        int pos=0;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            _timeDriver = (TextView) itemView.findViewById(R.id.tvTimeDrive);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);
            _address = (TextView) itemView.findViewById(R.id.tvName);
            _driver = (TextView) itemView.findViewById(R.id.tvDriver);
            _restaurant = (TextView) itemView.findViewById(R.id.tvAddress);
            _back = (ImageView) itemView.findViewById(R.id.img_back);
            _container = (RelativeLayout) itemView.findViewById(R.id.item_layout_container);
            _status = (ImageView)  itemView.findViewById(R.id.imageView_status);

            contex = itemView.getContext();
            itemView.setOnClickListener(this);
            _container.setOnClickListener(this);
            _container.setSelected(true);
        }

        @Override
        public void onClick(View v) {

            Bundle mb = new Bundle();
            mb.putString("id", _id+"");
            Intent i = new Intent(v.getContext(), DetailDelivery.class);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterDeliverysList = (ArrayList<DeliverysManager>) results.values;
                mSelfAdapter.notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<DeliverysManager> tempFilter = new ArrayList<DeliverysManager>();

                Iterator<DeliverysManager> iterator = deliverysList.iterator();
                while(iterator.hasNext()){
                    DeliverysManager temp = iterator.next();
                    String timeConstraint=constraint.toString().replace(".","");
                    if(temp.delivery_address.toLowerCase().contains(constraint) ||
                            temp.getName_restaurant().toLowerCase().contains(constraint) ||
                            temp.getName_driver().toLowerCase().contains(constraint) ||
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
        filterDeliverysList = deliverysList;
        mSelfAdapter.notifyDataSetChanged();
    }
}
