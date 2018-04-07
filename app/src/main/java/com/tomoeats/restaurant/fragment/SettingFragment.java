package com.tomoeats.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.adapter.SettingAdapter;
import com.tomoeats.restaurant.model.Setting;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingFragment extends Fragment {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.setting_rv)
    RecyclerView settingRv;
    Unbinder unbinder;

    Context context;
    Activity activity;
    List<Setting> settingArrayList;
    SettingAdapter settingAdapter;
    @BindView(R.id.title)
    TextView title;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        activity = getActivity();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title.setText(R.string.restaurant);
        settingArrayList = new ArrayList<>();
        settingArrayList.add(new Setting(getString(R.string.history), R.drawable.ic_timer_timing_tool));
        settingArrayList.add(new Setting(getString(R.string.edit_restaurant), R.drawable.ic_edit));
        settingArrayList.add(new Setting(getString(R.string.edit_timing), R.drawable.ic_edit_time));
        settingArrayList.add(new Setting(getString(R.string.deliveries), R.drawable.ic_delivery_truck));
        settingArrayList.add(new Setting(getString(R.string.change_password), R.drawable.ic_padlock));
        settingArrayList.add(new Setting(getString(R.string.logout), R.drawable.logout));
        settingArrayList.add(new Setting(getString(R.string.delete_account), R.drawable.trash));

        settingRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        settingRv.setItemAnimator(new DefaultItemAnimator());
        settingRv.setHasFixedSize(true);
        settingAdapter = new SettingAdapter(settingArrayList, context,activity);
        settingRv.setAdapter(settingAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
