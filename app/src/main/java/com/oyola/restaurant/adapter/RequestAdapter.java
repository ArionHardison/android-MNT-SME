package com.oyola.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.RequestAcceptActivity;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    private List<Order> list;

    public RequestAdapter(List<Order> list, Context con) {
        this.list = list;
        this.context = con;
        this.activity = activity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Order order = list.get(position);
        if (order.getAddress() != null) {
            if (order.getAddress().getMapAddress() != null) {
                holder.address.setText(order.getAddress().getMapAddress());
            }
        }
        try {
            if (order.getCreatedAt() != null) {
                holder.orderDate.setText(Utils.getDate(order.getCreatedAt()));
                holder.orderTime.setText(Utils.getTime(order.getCreatedAt()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (order.getPickUpRestaurant() != null) {
            if (order.getPickUpRestaurant() == 0) {
                holder.orderType.setText(context.getString(R.string.order_type_delivery));
            } else if (order.getPickUpRestaurant() == 1) {
                holder.orderType.setText(context.getString(R.string.order_type_takeaway));
                holder.address.setVisibility(View.GONE);
            } else {
                holder.orderType.setText(context.getString(R.string.order_type_delivery));
            }
        }
        //Default Status and color
        String status = context.getResources().getString(R.string.dispute_created);
        holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

        if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
            status = context.getResources().getString(R.string.incoming);
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        holder.status.setText(status);
        String name = Utils.toFirstCharUpperAll(order.getUser().getName());
        String payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());

        holder.userName.setText(name);
        holder.paymentMode.setText(payment_mode);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.selectedOrder = list.get(position);
                context.startActivity(new Intent(context, RequestAcceptActivity.class));
            }
        });
        Glide.with(context).load(order.getUser().getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(holder.userImg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Order item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Order item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address, paymentMode, orderTime, orderDate, status, orderType;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            orderDate = view.findViewById(R.id.order_date);
            orderTime = view.findViewById(R.id.order_time);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            status = view.findViewById(R.id.status);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
            orderType = view.findViewById(R.id.order_type);
        }
    }

}
