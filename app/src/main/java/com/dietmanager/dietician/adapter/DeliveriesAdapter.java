package com.dietmanager.dietician.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.Address;
import com.dietmanager.dietician.model.Invoice;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.Shop;
import com.dietmanager.dietician.model.Transporter;
import com.dietmanager.dietician.model.User;

import java.util.ArrayList;
import java.util.List;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.MyViewHolder> {
    private List<Order> orderList;

    public DeliveriesAdapter() {
        orderList = new ArrayList<>();
    }

    public void setList(List<Order> itemList) {
        if (itemList == null) {
            return;
        }
        orderList.clear();
        orderList.addAll(itemList);
        notifyDataSetChanged();
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
        Order order = orderList.get(position);
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
                holder.address.setText((address.getBuilding() != null ? address.getBuilding() + ", " : "") +
                        address.getMapAddress());
            } else {
                if (order.getShop().getMapsAddress() != null) {
                    holder.address.setText(order.getShop().getMapsAddress());
                }
            }

            Invoice invoice = order.getInvoice();
            if (invoice != null && invoice.getPayable() != 0) {
                holder.totalAmt.setText(GlobalData.profile.getCurrency() + String.valueOf(invoice.getPayable()));
            }

            if (order.getStatus() != null) {
                holder.statusTxt.setText(order.getStatus());
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

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
