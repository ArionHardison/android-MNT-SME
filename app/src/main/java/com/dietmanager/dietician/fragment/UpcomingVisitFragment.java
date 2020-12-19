package com.dietmanager.dietician.fragment;

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

import com.dietmanager.dietician.activity.LoginActivity;
import com.dietmanager.dietician.activity.OrderRequestDetailActivity;
import com.dietmanager.dietician.adapter.HistoryAdapter;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.HistoryActivity;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingVisitFragment extends BaseFragment implements HistoryAdapter.IUserRequestListener {

    public static PastVisitFragment.CancelledListListener cancelledListListener;
    @BindView(R.id.upcoming_rv)
    RecyclerView upcomingRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<UserRequestItem> orderList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private Context context;
    private Activity activity;
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
        return inflater.inflate(R.layout.fragment_upcoming_visit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupAdapter();
        getHistory();
    }

    private void setupAdapter() {
        historyAdapter = new HistoryAdapter(orderList, context,this);
        upcomingRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        upcomingRv.setHasFixedSize(true);
        upcomingRv.setAdapter(historyAdapter);
    }

    @Override
    public void onUserRequestItemClicked(UserRequestItem userRequestItem) {
        Intent intent = new Intent(activity, OrderRequestDetailActivity.class);
        intent.putExtra("userRequestItem", (Serializable)userRequestItem);
        intent.putExtra("hideAssignChef", true);
        startActivity(intent);
    }

    private void getHistory() {
        HistoryActivity.showDialog();
        Call<List<UserRequestItem>> call = apiInterface.getHistory("SCHEDULED");
        call.enqueue(new Callback<List<UserRequestItem>>() {
            @Override
            public void onResponse(Call<List<UserRequestItem>> call, Response<List<UserRequestItem>> response) {
                HistoryActivity.dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    List<UserRequestItem> historyModel = response.body();
                    if (historyModel != null) {
                        if (historyModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            upcomingRv.setVisibility(View.VISIBLE);
                            orderList = historyModel;
                            sortOrdersToDescending(orderList);
                            historyAdapter.setList(orderList);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            upcomingRv.setVisibility(View.GONE);
                        }
                        if (cancelledListListener != null)
                            if (historyModel != null && historyModel.size() > 0) {
                                cancelledListListener.setCancelledListener(historyModel);
                            } else {
                                cancelledListListener.setCancelledListener(new ArrayList<UserRequestItem>());
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
            public void onFailure(Call<List<UserRequestItem>> call, Throwable t) {
                HistoryActivity.dismissDialog();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }
}