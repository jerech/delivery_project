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
import android.widget.Toast;

import com.paulpwo.delivery360.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pwol on 22/2/16.
 */
public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.RecyclerViewHolder> {

    List<ContributorDemo> contributorList;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    public RecyclerAdapter2(Context context,List<ContributorDemo> contributorList) {
        this.contributorList = contributorList;
        this.mContext = context;
 /*       setColores.add("#FFFA1818"); // ROJO
        setColores.add("#FF36E9FD"); // AZUL CLARO
        setColores.add("#FFFDAA36"); // NARANJA
        setColores.add("#FFFA1818"); // ROJO
        setColores.add("#FFFCE430"); // AMARILLO
        setColores.add("#FF30FC7E"); // VERDE AQUA*/

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_1_restauran,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        String tmp = contributorList.get(position).get_timer();
        holder._timer.setText(tmp);

        holder._time.setText(contributorList.get(position).get_time());
        holder._address.setText(contributorList.get(position).get_address());
        holder._driver.setText(contributorList.get(position).get_driver());
        if (tmp != ""){
            holder._back.setBackgroundResource(R.color.item_active);

            Picasso.with(mContext)
                    .load(R.drawable.ic_event_available_black_18dp)
                    .into(holder._status);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.ic_history_black_18dp)
                    .into(holder._status);
        }
        setAnimation(mContext,holder._container, position);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return contributorList.size();
    }
    public void removeItem(int position) {
        contributorList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem( ContributorDemo contributor) {
        contributorList.add(contributor);
        notifyItemInserted(contributorList.size());
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

        TextView _timer;
        TextView _time;
        TextView _address;
        TextView _driver;
        View _back;
        RelativeLayout _container;
        ImageView _status;
        Context contex;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            _timer = (TextView) itemView.findViewById(R.id.tvTimeDrive);
            _time = (TextView) itemView.findViewById(R.id.tvOnlineTime);
            _address = (TextView) itemView.findViewById(R.id.tvName);
            _driver = (TextView) itemView.findViewById(R.id.tvDrive);
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
            Toast.makeText(v.getContext(), "Item: " + _address.getText().toString(), Toast.LENGTH_SHORT).show();

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

