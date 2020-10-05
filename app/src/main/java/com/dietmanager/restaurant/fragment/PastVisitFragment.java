package com.dietmanager.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.activity.HistoryActivity;
import com.dietmanager.restaurant.activity.LoginActivity;
import com.dietmanager.restaurant.adapter.HistoryAdapter;
import com.dietmanager.restaurant.model.HistoryModel;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.model.ServerError;
import com.dietmanager.restaurant.network.ApiClient;
import com.dietmanager.restaurant.network.ApiInterface;
import com.dietmanager.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastVisitFragment extends BaseFragment {

    public static CancelledListListener cancelledListListener;
    @BindView(R.id.past_rv)
    RecyclerView pastRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<Order> orderList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public PastVisitFragment() {
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
        return inflater.inflate(R.layout.fragment_past_visit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupAdapter();
        getHistory();
    }

    private void setupAdapter() {
        historyAdapter = new HistoryAdapter(orderList, context);
        pastRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        pastRv.setHasFixedSize(true);
        pastRv.setAdapter(historyAdapter);
    }

    private void getHistory() {
        HistoryActivity.showDialog();
        Call<HistoryModel> call = apiInterface.getHistory();
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                HistoryActivity.dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    HistoryModel historyModel = response.body();
                    if (historyModel != null) {
                        if (historyModel.getCOMPLETED() != null && historyModel.getCOMPLETED().size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            pastRv.setVisibility(View.VISIBLE);
                            orderList = historyModel.getCOMPLETED();
                            sortOrdersToDescending(orderList);
                            historyAdapter.setList(orderList);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            pastRv.setVisibility(View.GONE);
                        }
                        if (cancelledListListener != null)
                            if (historyModel.getCANCELLED() != null && historyModel.getCANCELLED().size() > 0) {
                                cancelledListListener.setCancelledListener(historyModel.getCANCELLED());
                            } else {
                                cancelledListListener.setCancelledListener(new ArrayList<Order>());
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
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                HistoryActivity.dismissDialog();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    public interface CancelledListListener {
        void setCancelledListener(List<Order> cancelledOrder);
    }
}
