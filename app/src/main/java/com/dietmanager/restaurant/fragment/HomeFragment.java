package com.dietmanager.restaurant.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.restaurant.helper.GlobalData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.activity.LoginActivity;
import com.dietmanager.restaurant.adapter.IncomingStickyAdapter;
import com.dietmanager.restaurant.controller.GetProfile;
import com.dietmanager.restaurant.controller.ProfileListener;
import com.dietmanager.restaurant.helper.ConnectionHelper;
import com.dietmanager.restaurant.helper.CustomDialog;
import com.dietmanager.restaurant.helper.stickyadapter.StickyHeaderLayoutManager;
import com.dietmanager.restaurant.model.IncomingOrders;
import com.dietmanager.restaurant.model.OngoingHistoryModel;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.model.Profile;
import com.dietmanager.restaurant.model.ServerError;
import com.dietmanager.restaurant.network.ApiClient;
import com.dietmanager.restaurant.network.ApiInterface;
import com.dietmanager.restaurant.utils.Constants;
import com.dietmanager.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ProfileListener {


    @BindView(R.id.incoming_rv)
    RecyclerView incomingRv;
    @BindView(R.id.img_shop)
    ImageView shopImg;
    @BindView(R.id.tv_shop_name)
    TextView shopName;
    @BindView(R.id.tv_shop_address)
    TextView shopAddress;
    @BindView(R.id.ratingBar)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.group_no_data)
    Group layoutNoRecords;

    private IncomingStickyAdapter adapter;
    private List<Order> orderList;
    private ConnectionHelper connectionHelper;
    private CustomDialog customDialog;
    private boolean isInternet;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private String TAG = "HomeFragment";
    private String email, password;

    private Handler homeHandler = new Handler();
    private boolean isVisible = true;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isInternet) {
                // getIncomingOrders();
            } else {
                Utils.displayMessage(getActivity(), getString(R.string.oops_no_internet));
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isInternet) {
                if (isVisible && incomingRv != null) {
                    getIncomingOrders();
                    homeHandler.postDelayed(this, 3000);
                }
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_updated, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        adapter = new IncomingStickyAdapter(getContext());
        incomingRv.setLayoutManager(new StickyHeaderLayoutManager());
        incomingRv.setItemAnimator(new DefaultItemAnimator());
        incomingRv.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderList = new ArrayList<>();
        connectionHelper = new ConnectionHelper(getContext());
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(getContext());
    }

    private void updateUI(Profile profile) {
        if (profile != null && profile.getDefaultBanner() != null)
            Glide.with(getContext())
                    .load(profile.getDefaultBanner())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate())
                    .into(shopImg);
        if (profile != null && profile.getName() != null)
            shopName.setText(profile.getName());
        if (profile != null && profile.getAddress() != null)
            shopAddress.setText(profile.getMapsAddress());
        if (profile != null && profile.getRating() != null)
            ratingBar.setRating(profile.getRating());
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        getIncomingOrders();
        homeHandler.postDelayed(runnable, 5000);
        getProfile();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.BROADCAST.UPDATE_ORDERS));
    }

    @Override
    public void onPause() {
        super.onPause();
        homeHandler.removeCallbacks(runnable);
    }

    private void getProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            new GetProfile(apiInterface, this);
        }
    }

    private void getIncomingOrders() {
        //customDialog.show();
        Call<IncomingOrders> call = apiInterface.getIncomingOrders("ordered");
        call.enqueue(new Callback<IncomingOrders>() {
            @Override
            public void onResponse(Call<IncomingOrders> call, Response<IncomingOrders> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    if (!Utils.isNullOrEmpty(response.body().getOrders())) {
                        incomingRv.setVisibility(View.VISIBLE);
                        layoutNoRecords.setVisibility(View.GONE);
                        orderList.clear();
                        orderList.addAll(response.body().getOrders());
                        splitUpList(orderList);
                    } else {
                        incomingRv.setVisibility(View.GONE);
                        layoutNoRecords.setVisibility(View.VISIBLE);
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(getActivity(), serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            getActivity().finish();
                        }
                    } catch (JsonSyntaxException e) {
//                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<IncomingOrders> call, Throwable t) {
                customDialog.dismiss();
//                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    private void splitUpList(List<Order> orderList) {
        List<OngoingHistoryModel> onGoingHistoryList = new ArrayList<>();

        List<Order> scheduledOrders = new ArrayList<>();
        List<Order> onGoingOrders = new ArrayList<>();

        for (int i = 0, size = orderList.size(); i < size; i++) {
            Order order = orderList.get(i);
            if (order != null) {
                if (order.getScheduleStatus() != null && order.getScheduleStatus() == 1) {
                    scheduledOrders.add(order);
                } else {
                    onGoingOrders.add(order);
                }
            }
        }

        if (scheduledOrders.size() > 0) {
            onGoingHistoryList.add(new OngoingHistoryModel("SCHEDULED ORDERS", scheduledOrders));
        }
        if (onGoingOrders.size() > 0) {
            onGoingHistoryList.add(new OngoingHistoryModel("ASAP ORDERS", onGoingOrders));
        }
        adapter.setStickyItemList(onGoingHistoryList);
    }

    @Override
    public void onSuccess(Profile profile) {
        try {
            GlobalData.profile = profile;
            updateUI(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String error) {
        Log.e(TAG, error);
    }
}
