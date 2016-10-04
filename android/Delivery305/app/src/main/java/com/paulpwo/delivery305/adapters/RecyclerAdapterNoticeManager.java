package com.paulpwo.delivery305.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.paulpwo.delivery305.models.NoticeManager;
import com.paulpwo.delivery305.R;
import com.paulpwo.delivery305.config.Constants;
import com.paulpwo.delivery305.manager.ManagerDetailNotice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulpwo on 13/7/16.
 */
@Keep
public class RecyclerAdapterNoticeManager  extends RecyclerView.Adapter<RecyclerAdapterNoticeManager.RecyclerViewHolder>  {
    List<NoticeManager> noticeManager;
    int count=0;
    List<String> setColores = new ArrayList<String>();
    private int lastPosition = -1;
    private Context mContext;
    public RecyclerAdapterNoticeManager(Context context,List<NoticeManager> noticeManager) {
        this.noticeManager = noticeManager;
        this.mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_notice_manager,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    /*    String tmp = noticeManager.get(position).get_timer();
        holder._timer.setText(tmp);

        holder._time.setText(noticeManager.get(position).get_time());
        holder._address.setText(noticeManager.get(position).get_address());
        holder._driver.setText(noticeManager.get(position).get_driver());
        if (tmp != ""){
            holder._back.setBackgroundResource(R.color.item_active);

            Picasso.with(mContext)
                    .load(R.drawable.ic_event_available_black_18dp)
                    .into(holder._status);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.ic_history_black_18dp)
                    .into(holder._status);
        }*/
        //setAnimation(mContext,holder._container, position);


        holder.title.setText(noticeManager.get(position).getTitle());
        holder.tvId.setText(noticeManager.get(position).getId().toString());
        holder.description.setText(noticeManager.get(position).getBody());
        holder.id_delivery.setText(noticeManager.get(position).getId_delivery().toString());
        holder.id_restaurant.setText(noticeManager.get(position).getId_restaurant().toString());
        holder.id = noticeManager.get(position).getId();
        //holder.title.setText(noticeManager.get(position).getTitle());
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return noticeManager.size();
    }
    public void removeItem(int position) {
        noticeManager.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem( NoticeManager noticeManager) {

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

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {

        CardView cv;
        TextView title;
        TextView description;
        TextView id_restaurant;
        TextView id_delivery;
        TextView tvId;
        Integer id;
       // ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            id_delivery = (TextView) itemView.findViewById(R.id.id_delivery);
            id_restaurant = (TextView) itemView.findViewById(R.id.id_restaurant);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle mb = new Bundle();
            mb.putString("id_delivery", id_delivery.getText().toString());
            mb.putString("id_db", id.toString());

             Intent i = new Intent(v.getContext(), ManagerDetailNotice.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtras(mb);
            v.getContext().startActivity(i);


        }



        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
