package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;

import java.util.ArrayList;
import java.util.List;

public class AssignChefListAdapter extends RecyclerView.Adapter<AssignChefListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private IAssignChefListener listener;
    private List<AssignChefItem> list;
    private List<AssignChefItem> filterItems =new ArrayList<>();
    public AssignChefListAdapter(List<AssignChefItem> list, Context con,IAssignChefListener listener) {
        this.list = list;
        filterItems.clear();
        filterItems.addAll(list);
        this.context = con;
        this.listener = listener;
    }

    @Override
    public AssignChefListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assign_chef_list, parent, false);
        return new AssignChefListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignChefListAdapter.MyViewHolder holder, final int position) {
        AssignChefItem item = list.get(position);
        holder.userName.setText(item.getName());
        if(item.getAvatar()!=null){
            Glide.with(context)
                    .load(item.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(holder.userImg);
        }
        holder.address.setText(item.getAddress());
        holder.rating.setText(item.getRating());

        holder.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAssignChefClicked(item);
            }
        });
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
                FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = list;
                return filterResults;
            } else {
                List<AssignChefItem> filteredList = new ArrayList<AssignChefItem>();
                for (AssignChefItem row : filterItems) {
                    if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                    }
                }
                list = filteredList;
            }

            FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = list;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<AssignChefItem>)results.values;
            notifyDataSetChanged();
        }
    };

    public void remove(Order item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<AssignChefItem> list) {
        this.list = list;
        filterItems.clear();
        filterItems.addAll(list);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address,rating;
        LinearLayout itemLayout;
        ImageView userImg;
        Button btnAssign;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            rating = view.findViewById(R.id.rating);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
            btnAssign = view.findViewById(R.id.btnAssign);
        }
    }

    public interface IAssignChefListener{
        void onAssignChefClicked(AssignChefItem assignChefItem);
    }
}