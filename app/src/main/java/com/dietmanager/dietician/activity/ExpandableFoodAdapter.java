package com.dietmanager.dietician.activity;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.model.food.FoodItem;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kotlin.jvm.internal.Intrinsics;


public class ExpandableFoodAdapter extends BaseExpandableListAdapter {

    private Context context;
    public static Integer finalCount = 0;
    private List<String> expandableListTitle;
    private HashMap<String, List<FoodItem>> expandableListDetail;
    private IExpandableClickListener listener;

    public ExpandableFoodAdapter(Context context, List<String> expandableListTitle,
                                   HashMap<String, List<FoodItem>> expandableListDetail,IExpandableClickListener listener) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.listener = listener;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);


    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FoodItem foodItem = (FoodItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.food_list_item, null);
        }
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.food_list_item, null);
            TextView tvFoodTitle,tvFoodDescription,tvFoodPrice,notFound;
            CardView cardItem;
            ImageView imgFood;
            ImageView imgSelected;

            imgFood = convertView.findViewById(R.id.img_food);
            tvFoodTitle = convertView.findViewById(R.id.tv_food_title);
            tvFoodPrice = convertView.findViewById(R.id.tv_food_price);
            tvFoodDescription = convertView.findViewById(R.id.tv_food_description);
            cardItem = convertView.findViewById(R.id.card_item);
            notFound = convertView.findViewById(R.id.tvNotFound);
            imgSelected = convertView.findViewById(R.id.imgSelected);

            if(foodItem.getId()!=0) {
                notFound.setVisibility(View.GONE);
                cardItem.setVisibility(View.VISIBLE);
                if(foodItem.getChecked()==1)
                    imgSelected.setVisibility(View.VISIBLE);
                else
                    imgSelected.setVisibility(View.GONE);
                tvFoodTitle.setText(String.valueOf(foodItem.getName()));
                tvFoodDescription.setText(String.valueOf(foodItem.getDescription()));
                tvFoodPrice.setText(String.valueOf(foodItem.getPrice()));
                if (foodItem.getAvatar() != null)
                    Glide.with(context).load(foodItem.getAvatar())
                            .apply(new RequestOptions().centerCrop().placeholder(R.drawable.shimmer_bg).error(R.drawable.shimmer_bg).dontAnimate()).into(imgFood);

                cardItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(foodItem.getChecked()==0)
                            listener.onItemClicked(foodItem);
                    }
                });
            }
            else {
                cardItem.setVisibility(View.GONE);
                notFound.setVisibility(View.VISIBLE);
            }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_food_header, null);
        }
        if (isExpanded) {
            ((TextView) convertView.findViewById(R.id.txt_header_name)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            ((ImageView) convertView.findViewById(R.id.img_up_down)).setColorFilter(ContextCompat.getColor(context, R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
            convertView.findViewById(R.id.llFoodHeader).setBackground(ContextCompat.getDrawable(context, R.drawable.ultramarine_bg_curved));
        } else {
            convertView.findViewById(R.id.llFoodHeader).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_primary_border));
            ((TextView) convertView.findViewById(R.id.txt_header_name)).setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            ((ImageView) convertView.findViewById(R.id.img_up_down)).setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        convertView.findViewById(R.id.img_up_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCreateNewClicked(listTitle);
            }
        });
        TextView listTitleTextView = convertView.findViewById(R.id.txt_header_name);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public interface IExpandableClickListener{
        void onCreateNewClicked(String title);
        void onItemClicked(FoodItem foodItem);
    }
}
