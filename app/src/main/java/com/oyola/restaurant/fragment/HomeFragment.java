package com.oyola.restaurant.fragment;


import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.LoginActivity;
import com.oyola.restaurant.adapter.RequestAdapter;
import com.oyola.restaurant.adapter.RequestHeaderAdapter;
import com.oyola.restaurant.controller.GetProfile;
import com.oyola.restaurant.controller.ProfileListener;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.IncomingOrders;
import com.oyola.restaurant.model.OngoingHistoryModel;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.Profile;
import com.oyola.restaurant.model.SectionHeaderItem;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Constants;
import com.oyola.restaurant.utils.Utils;

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


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.incoming_rv)
    RecyclerView incomingRv;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;

    private RequestHeaderAdapter adapter;
    List<Order> orderList;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternet;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "HomeFragment";
    String email, password;
    @BindView(R.id.shop_img)
    ImageView shopImg;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    @BindView(R.id.resturant_rating)
    AppCompatRatingBar resturant_rating;
    private Handler homeHandler = new Handler();
    private boolean isVisible = true;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isInternet) {
                // getIncomingOrders();
            } else {
                Utils.displayMessage(activity, getString(R.string.oops_no_internet));
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        prepareAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderList = new ArrayList<>();
        context = getContext();
        activity = getActivity();
        connectionHelper = new ConnectionHelper(context);
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) scrollRange = appBarLayout.getTotalScrollRange();
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    collapsingToolbar.setTitle(GlobalData.profile.getName());
                } else if (isShow) {
                    isShow = false;
                    collapsingToolbar.setTitle("");
                }
            }
        });
    }

    private void updateUI(Profile profile) {
        if (profile != null && profile.getDefaultBanner() != null)
            Glide.with(context)
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
            resturant_rating.setRating(profile.getRating());
    }


    private void prepareAdapter() {
        if (incomingRv != null) {
            adapter = new RequestHeaderAdapter(getContext());
            incomingRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            incomingRv.setHasFixedSize(true);
            incomingRv.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        getIncomingOrders();
        homeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isInternet) {
                    if (isVisible && incomingRv != null) {
                        getIncomingOrders();
                        homeHandler.postDelayed(this, 3000);
                    }
                }
            }
        }, 3000);
        getProfile();


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.BROADCAST.UPDATE_ORDERS));
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
                    if (response.body().getOrders() != null &&
                            !response.body().getOrders().isEmpty() && response.body().getOrders().size() > 0) {
                        if (incomingRv != null && llNoRecords != null) {
                            incomingRv.setVisibility(View.VISIBLE);
                            llNoRecords.setVisibility(View.GONE);
                        }
                        orderList.clear();
                        orderList.addAll(response.body().getOrders());
                        splitUpList(orderList);
                    } else {
                        if (incomingRv != null && llNoRecords != null) {
                            incomingRv.setVisibility(View.GONE);
                            llNoRecords.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                        if (response.code() == 401) {
                            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            activity.finish();
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
        List<SectionHeaderItem> sectionHeaderItemList = new ArrayList<>();

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
            sectionHeaderItemList.add(new SectionHeaderItem("SCHEDULED ORDERS", scheduledOrders));
        }
        if (onGoingOrders.size() > 0) {
            sectionHeaderItemList.add(new SectionHeaderItem("ASAP ORDERS", onGoingOrders));
        }
        adapter.setRequestItemList(sectionHeaderItemList);
    }

    @Override
    public void onSuccess(Profile profile) {
        try {
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
