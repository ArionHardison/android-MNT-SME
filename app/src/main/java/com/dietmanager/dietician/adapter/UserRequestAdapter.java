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
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.dietmanager.dietician.utils.Utils;

import java.util.List;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.MyViewHolder> {

    private Context context;
    private IUserRequestListener listener;
    private List<UserRequestItem> list;

    public UserRequestAdapter(List<UserRequestItem> list, Context con, IUserRequestListener listener) {
        this.list = list;
        this.context = con;
        this.listener = listener;
    }

    @Override
    public UserRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_request, parent, false);
        return new UserRequestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserRequestAdapter.MyViewHolder holder, final int position) {
        UserRequestItem item = list.get(position);

        if (item.getStatus().equalsIgnoreCase("ORDERED"))
            holder.tvHeader.setVisibility(View.VISIBLE);
        else
            holder.tvHeader.setVisibility(View.GONE);

        holder.tvFoodName.setText(Utils.toFirstCharUpperAll(item.getUser().getName()));
        if (item.getUser().getSubscribePlans() != null && item.getUser().getSubscribePlans().getSubscription() != null)
            holder.tvSubscription.setText(Utils.toFirstCharUpperAll(item.getUser().getSubscribePlans().getSubscription().getTitle()));
        if (item.getCustomerAddress() != null)
            holder.tvFoodDescription.setText(item.getCustomerAddress().getMapAddress());
        Glide.with(context)
                .load(AppConfigure.BASE_URL + item.getUser().getAvatar())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man))
                .into(holder.foodImg);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserRequestItemClicked(item);
            }
        });

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

    public void setList(List<UserRequestItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodName, tvFoodDescription, tvHeader, tvSubscription;
        CardView itemLayout;
        ImageView foodImg;

        public MyViewHolder(View view) {
            super(view);
            tvFoodName = view.findViewById(R.id.tv_food_name);
            tvFoodDescription = view.findViewById(R.id.tv_food_description);
            itemLayout = view.findViewById(R.id.item_layout);
            foodImg = view.findViewById(R.id.food_img);
            tvSubscription = view.findViewById(R.id.tv_subscription);
            tvHeader = view.findViewById(R.id.tvHeader);
        }
    }

    public interface IUserRequestListener {
        void onUserRequestItemClicked(UserRequestItem userRequestItem);
    }
}