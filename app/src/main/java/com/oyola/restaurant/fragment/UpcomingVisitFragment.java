package com.oyola.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.HistoryActivity;
import com.oyola.restaurant.adapter.OnGoingStickyAdapter;
import com.oyola.restaurant.helper.stickyadapter.StickyHeaderLayoutManager;
import com.oyola.restaurant.model.IncomingOrders;
import com.oyola.restaurant.model.OngoingHistoryModel;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingVisitFragment extends BaseFragment {

    @BindView(R.id.upcoming_rv)
    RecyclerView upcomingRv;

    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private Unbinder unbinder;
    private Context context;
    private Activity activity;

    private OnGoingStickyAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public UpcomingVisitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity == null)
            activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_visit, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OnGoingStickyAdapter(getContext());
        upcomingRv.setLayoutManager(new StickyHeaderLayoutManager());
        upcomingRv.setItemAnimator(new DefaultItemAnimator());
        upcomingRv.setAdapter(adapter);
        getOnGoingOrders();
    }

    private void splitUpList(List<Order> orderList) {
        List<OngoingHistoryModel> onGoingHistoryList = new ArrayList<>();

        List<Order> scheduledOrders = new ArrayList<>();
        List<Order> onGoingOrders = new ArrayList<>();

        for (int i = 0, size = orderList.size(); i < size; i++) {
            Order order = orderList.get(i);
            if (order != null) {
                if (order.getScheduleStatus() != null && order.getScheduleStatus() == 1 &&
                        (order.getStatus().equalsIgnoreCase("RECEIVED") ||
                                order.getStatus().equalsIgnoreCase("SCHEDULED") ||
                                order.getStatus().equalsIgnoreCase("PICKUP_USER"))) {
                    scheduledOrders.add(order);
                } else {
                    onGoingOrders.add(order);
                }
            }
        }

        if (scheduledOrders.size() > 0) {
            sortOrdersToDescending(scheduledOrders);
            onGoingHistoryList.add(new OngoingHistoryModel("SCHEDULED ORDERS", scheduledOrders));
        }
        if (onGoingOrders.size() > 0) {
            sortOrdersToDescending(onGoingOrders);
            onGoingHistoryList.add(new OngoingHistoryModel("ONGOING ORDERS", onGoingOrders));
        }
        adapter.setStickyItemList(onGoingHistoryList);
    }

    private void getOnGoingOrders() {
        HistoryActivity.showDialog();
        Call<IncomingOrders> call = apiInterface.getIncomingOrders("processing");
        call.enqueue(new Callback<IncomingOrders>() {
            @Override
            public void onResponse(Call<IncomingOrders> call, Response<IncomingOrders> response) {
                HistoryActivity.dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    if (response.body() != null) {
                        IncomingOrders incomingOrders = response.body();
                        if (incomingOrders.getOrders() != null && incomingOrders.getOrders().size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            upcomingRv.setVisibility(View.VISIBLE);
                            orderList.addAll(incomingOrders.getOrders());
                            splitUpList(orderList);
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            upcomingRv.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<IncomingOrders> call, Throwable t) {
                HistoryActivity.dismissDialog();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}