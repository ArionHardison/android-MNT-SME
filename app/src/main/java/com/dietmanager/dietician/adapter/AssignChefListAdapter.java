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
import com.dietmanager.dietician.model.subscribe.SubscribeItem;

import java.util.List;

public class AssignChefListAdapter extends RecyclerView.Adapter<AssignChefListAdapter.MyViewHolder> {

    private Context context;
    private List<SubscribeItem> list;

    public AssignChefListAdapter(List<SubscribeItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public AssignChefListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assign_chef_list, parent, false);
        return new AssignChefListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignChefListAdapter.MyViewHolder holder, final int position) {
        SubscribeItem item = list.get(position);
        holder.userName.setText(item.getUser().getName());
        if(item.getUser().getAvatar()!=null){
            Glide.with(context)
                    .load(AppConfigure.BASE_URL+item.getUser().getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(holder.userImg);
        }
        holder.address.setText(item.getUser().getMapAddress());

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

        TextView userName, address;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }
    }
}