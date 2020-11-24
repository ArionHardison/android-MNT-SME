package com.dietmanager.dietician.fragment;

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

import com.dietmanager.dietician.activity.OrderRequestDetailActivity;
import com.dietmanager.dietician.model.userrequest.OrderingredientItem;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.HistoryActivity;
import com.dietmanager.dietician.activity.LoginActivity;
import com.dietmanager.dietician.adapter.HistoryAdapter;
import com.dietmanager.dietician.model.HistoryModel;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.io.Serializable;
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
public class CancelOrderFragment extends BaseFragment implements HistoryAdapter.IUserRequestListener {

    public static PastVisitFragment.CancelledListListener cancelledListListener;
    @BindView(R.id.cancel_rv)
    RecyclerView cancelRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    List<UserRequestItem> orderList = new ArrayList<>();
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
        historyAdapter = new HistoryAdapter(orderList, context,this);
        cancelRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        cancelRv.setHasFixedSize(true);
        cancelRv.setAdapter(historyAdapter);
    }


    @Override
    public void onUserRequestItemClicked(UserRequestItem userRequestItem) {
        Intent intent = new Intent(activity, OrderRequestDetailActivity.class);
        intent.putExtra("userRequestItem", (Serializable)userRequestItem);
        intent.putExtra("hideAssignChef", true);
        startActivity(intent);
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
        Call<List<UserRequestItem>> call = apiInterface.getHistory("CANCELLED");
        call.enqueue(new Callback<List<UserRequestItem>>() {
            @Override
            public void onResponse(Call<List<UserRequestItem>> call, Response<List<UserRequestItem>> response) {
                HistoryActivity.dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    List<UserRequestItem> historyModel = response.body();
                    if (historyModel != null) {
                        if (historyModel.size() > 0) {
                            if (llNoRecords != null)
                                llNoRecords.setVisibility(View.GONE);
                            if (cancelRv != null)
                                cancelRv.setVisibility(View.VISIBLE);
                            orderList = historyModel;
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

    public interface CancelledListListener {
        void setCancelledListener(List<UserRequestItem> cancelledOrder);
    }
}
