package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.model.followers.Followers;

import java.util.ArrayList;
import java.util.List;

public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Followers> list;
    private List<Followers> filterItems =new ArrayList<>();
    public FollowersListAdapter(List<Followers> list, Context con) {
        this.list = list;
        filterItems.clear();
        filterItems.addAll(list);
        this.context = con;
    }

    @Override
    public FollowersListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_followers_list, parent, false);
        return new FollowersListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FollowersListAdapter.MyViewHolder holder, final int position) {
        Followers item = list.get(position);
        holder.userName.setText(item.getName());
            Glide.with(context)
                    .load(AppConfigure.BASE_URL+item.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(holder.userImg);
        if(item.getMapAddress()!=null)
            holder.address.setText(item.getMapAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString.isEmpty()) {
                list=filterItems;
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            } else {
                List<Followers> filteredList = new ArrayList<Followers>();
                for (Followers row : filterItems) {
                    if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                    }
                }
                list = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = list;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<Followers>)results.values;
            notifyDataSetChanged();
        }
    };

    public void setList(List<Followers> list) {
        this.list = list;
        filterItems.clear();
        filterItems.addAll(list);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address;
        LinearLayout itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }
    }

    public interface IDietitianListener{
        void onDietitianClicked(Followers Followers);
    }
}