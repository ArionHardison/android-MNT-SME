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
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.dietmanager.dietician.utils.Utils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private IUserRequestListener listener;
    private List<UserRequestItem> list;

    public HistoryAdapter(List<UserRequestItem> list, Context con, IUserRequestListener listener) {
        this.list = list;
        this.context = con;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UserRequestItem order = list.get(position);
        if (order.getCustomerAddress() != null) {
            if (order.getCustomerAddress().getMapAddress() != null) {
                holder.address.setText((order.getCustomerAddress().getBuilding() != null ? order.getCustomerAddress().getBuilding() + ", " : "") +
                        order.getCustomerAddress().getMapAddress());
            }
        }
        holder.price.setText(/*context.getString(R.string.currency_value)*/GlobalData.profile.getCurrency() + "" + order.getTotal());

        holder.status.setText(order.getStatus());
        if (order.getUser() != null) {
            String name = Utils.toFirstCharUpperAll(order.getUser().getName());
            holder.userName.setText(name);
            Glide.with(context).load(AppConfigure.BASE_URL+order.getUser().getAvatar())
                    .apply(new RequestOptions().placeholder(R.drawable.man).error(R.drawable.man).dontAnimate()).into(holder.userImg);
        }
        String payment_mode="Card";
        if (order.getPaymentMode()!=null) {
            payment_mode = order.getPaymentMode();
        }
        holder.paymentMode.setText(Utils.toFirstCharUpperAll(payment_mode));
        /*holder.itemLayout.setOnClickListener(v -> {
            GlobalData.selectedOrder = order;
            Intent intent = new Intent(context, OrderRequestDetailActivity.class);
            intent.putExtra("userRequestItem", (Serializable)order);
            context.startActivity(intent);
        });*/

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserRequestItemClicked(order);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(UserRequestItem item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(UserRequestItem item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<UserRequestItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address, paymentMode, price,status;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            price = view.findViewById(R.id.price);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            itemLayout = view.findViewById(R.id.item_layout);
            status = view.findViewById(R.id.status);
            userImg = view.findViewById(R.id.user_img);
        }

    }

    public interface IUserRequestListener{
        void onUserRequestItemClicked(UserRequestItem userRequestItem);
    }
}