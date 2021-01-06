package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.SubscribeRequestResponse;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SubscribeRequestAdapter extends RecyclerView.Adapter<SubscribeRequestAdapter.MyViewHolder> {

    private Context context;
    private List<SubscribeRequestResponse> list;

    public SubscribeRequestAdapter(List<SubscribeRequestResponse> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribe_request_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SubscribeRequestResponse item = list.get(position);
            holder.userName.setText(item.getName());
            holder.email.setText(item.getEmail());
            holder.mobile.setText("+"+item.getCountryCode()+item.getMobile());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void remove(Order item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<SubscribeRequestResponse> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, email,mobile;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            email = view.findViewById(R.id.email);
            mobile = view.findViewById(R.id.mobile);
        }
    }
}