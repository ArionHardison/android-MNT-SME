package com.pakupaku.outlet.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pakupaku.outlet.R;
import com.pakupaku.outlet.helper.GlobalData;
import com.pakupaku.outlet.model.Address;
import com.pakupaku.outlet.model.Invoice;
import com.pakupaku.outlet.model.Order;
import com.pakupaku.outlet.model.Shop;
import com.pakupaku.outlet.model.Transporter;
import com.pakupaku.outlet.model.User;

import java.util.List;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.MyViewHolder> {
    private List<Order> list;
    private Context context;
    private Activity activity;

    public DeliveriesAdapter(List<Order> list, Context con) {
        this.list = list;
        this.context = con;
        this.activity = activity;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deliveries_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Order order = list.get(position);
        if (order != null) {

            Transporter transporter = order.getTransporter();
            if (transporter != null && transporter.getName() != null) {
                holder.deliveryPersonName.setText(transporter.getName());
                holder.deliveryPersonName.setVisibility(View.VISIBLE);
            } else {
                holder.deliveryPersonName.setVisibility(View.GONE);
            }

            Shop shop = order.getShop();
            if (shop != null && shop.getName() != null) {
                holder.shopName.setText(shop.getName());
            }

            User user = order.getUser();
            if (user != null && user.getName() != null) {
                holder.userName.setText(user.getName());
            }

            Address address = order.getAddress();
            if (address != null && address.getMapAddress() != null) {
                holder.address.setText(address.getMapAddress());
            }

            Invoice invoice = order.getInvoice();
            if (invoice != null && invoice.getNet() != 0) {
                holder.totalAmt.setText(GlobalData.profile.getCurrency() + String.valueOf(invoice.getNet()));
            }

            if (order.getStatus() != null) {
                holder.statusTxt.setText(order.getStatus());
            }
        }
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


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address, deliveryPersonName, statusTxt, shopName, totalAmt;
        CardView itemLayout;

        MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            deliveryPersonName = view.findViewById(R.id.delivery_person_name);
            itemLayout = view.findViewById(R.id.item_layout);
            statusTxt = view.findViewById(R.id.status_txt);
            totalAmt = view.findViewById(R.id.total_amnt);
            shopName = view.findViewById(R.id.shop_name);
        }
    }

}
