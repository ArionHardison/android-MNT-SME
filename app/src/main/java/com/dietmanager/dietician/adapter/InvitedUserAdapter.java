package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.assignchef.AssignChefItem;
import com.dietmanager.dietician.model.initeduser.InvitedUserItem;

import java.util.List;

public class InvitedUserAdapter extends RecyclerView.Adapter<InvitedUserAdapter.MyViewHolder> {

    private Context context;
    private List<InvitedUserItem> list;

    public InvitedUserAdapter(List<InvitedUserItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public InvitedUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invite_user, parent, false);
        return new InvitedUserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvitedUserAdapter.MyViewHolder holder, final int position) {
        InvitedUserItem item = list.get(position);
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
        if(item.getUser().getMapAddress()!=null)
            holder.address.setText(item.getUser().getMapAddress());
        holder.rating.setText(item.getUser().getRating());

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

    public void setList(List<InvitedUserItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address,rating;
        LinearLayout itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            rating = view.findViewById(R.id.rating);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }
    }
}