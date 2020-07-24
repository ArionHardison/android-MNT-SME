package com.oyola.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.HistoryActivity;
import com.oyola.restaurant.activity.LoginActivity;
import com.oyola.restaurant.adapter.HistoryAdapter;
import com.oyola.restaurant.model.HistoryModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelOrderFragment extends BaseFragment {

    public static PastVisitFragment.CancelledListListener cancelledListListener;
    @BindView(R.id.cancel_rv)
    RecyclerView cancelRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    List<Order> orderList = new ArrayList<>();
    HistoryAdapter historyAdapter;
    private Unbinder unbinder;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public CancelOrderFragment() {
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

    public void setCancelledListListener(PastVisitFragment.CancelledListListener cancelledListListener) {
        this.cancelledListListener = cancelledListListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelled_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupAdapter();

        getHistory();

        return view;
    }

    private void setupAdapter() {
        historyAdapter = new HistoryAdapter(orderList, context);
        cancelRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        cancelRv.setHasFixedSize(true);
        cancelRv.setAdapter(historyAdapter);
    }

    /*@Override
    public void onResume() {
        super.onResume();
//        getHistory();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                        if (historyModel.getCANCELLED() != null && historyModel.getCANCELLED().size() > 0) {
                            if (llNoRecords != null)
                                llNoRecords.setVisibility(View.GONE);
                            if (cancelRv != null)
                                cancelRv.setVisibility(View.VISIBLE);
                            orderList = historyModel.getCANCELLED();
                            sortOrdersToDescending(orderList);
                            historyAdapter.setList(orderList);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            if (llNoRecords != null)
                                llNoRecords.setVisibility(View.VISIBLE);
                            if (cancelRv != null)
                                cancelRv.setVisibility(View.GONE);
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
