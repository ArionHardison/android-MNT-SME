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
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SubscribedMemberAdapter extends RecyclerView.Adapter<SubscribedMemberAdapter.MyViewHolder> {

    private Context context;
    private List<SubscribeItem> list;

    public SubscribedMemberAdapter(List<SubscribeItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribe_member_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SubscribeItem item = list.get(position);
        if(item.getUser()!=null) {
            holder.userName.setText(item.getUser().getName());
            if (item.getUser().getAvatar() != null) {
                Glide.with(context)
                        .load(AppConfigure.BASE_URL+item.getUser().getAvatar())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.man)
                                .error(R.drawable.man))
                        .into(holder.userImg);
            }
            if(item.getUser().getMapAddress()!=null)
                holder.address.setText(item.getUser().getMapAddress());
            //Instantiating the SimpleDateFormat class
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat dateStringFormat = new SimpleDateFormat("dd/MM/yyyy");
            //Parsing the given String to Date object
            try {
                if (item.getUser().getSubscribePlans() != null && item.getUser().getSubscribePlans().getCreatedAt() != null && item.getUser().getSubscribePlans().getExpiryDate() != null) {
                    Date dateStart = formatter.parse(item.getUser().getSubscribePlans().getCreatedAt());
                    holder.tvStartDate.setText(dateStringFormat.format(dateStart));
                    Date dateExpiry = formatter.parse(item.getUser().getSubscribePlans().getExpiryDate());
                    holder.tvEndDate.setText(dateStringFormat.format(dateExpiry));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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

    public void setList(List<SubscribeItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address,tvStartDate, tvEndDate;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            tvStartDate = view.findViewById(R.id.tvStartDate);
            tvEndDate = view.findViewById(R.id.tvEndDate);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }
    }
}